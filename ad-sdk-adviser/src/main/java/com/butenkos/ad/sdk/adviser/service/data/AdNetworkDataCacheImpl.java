package com.butenkos.ad.sdk.adviser.service.data;

import com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDao;
import com.butenkos.ad.sdk.adviser.model.domain.*;
import com.butenkos.ad.sdk.adviser.util.NullChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Lightweight cache implementation.
 * Since the cached data is updated rarely and all at once, and all the stored data is immutable,
 * there is no need to bring some caching framework in place.
 * Implementation based on the {@code EnumMap}, which makes it very performant in getting
 * data out of it.
 *
 * @see ModifiableAdNetworkDataImpl
 */
public class AdNetworkDataCacheImpl implements AdNetworkDataCache {
  private final static Logger LOG = LoggerFactory.getLogger(AdNetworkDataCacheImpl.class);
  private final AdNetworkDataDao dataDao;
  private transient AdNetworkData adNetworkData;

  public AdNetworkDataCacheImpl(AdNetworkDataDao dataDao, AdNetworkData adNetworkData) {
    NullChecker.checkNotNull(dataDao, adNetworkData);
    this.dataDao = dataDao;
    this.adNetworkData = new ImmutableAdNetworkData(adNetworkData);
  }

  @Override
  public void update(String batchJobId) {
    LOG.info("trying to update cache data, batchJobId {}", batchJobId);
    final AdNetworkData updatedData = dataDao.getByBatchJobId(batchJobId);
    saveUpdatedDataOrStayWithOldOne(updatedData);
  }

  @Override
  public void updateWithMostRecentData() {
    LOG.info("trying to update cache data with the most recent data");
    final AdNetworkData updatedData = dataDao.getMostRecent();
    saveUpdatedDataOrStayWithOldOne(updatedData);
  }

  @Override
  public String getCacheStatistics() {
    final String countryStats = Arrays.stream(Country.values())
        .map(country -> {
          final Map<AdType, List<AdNetwork>> map = adNetworkData.get(country);
          final int banner = map.getOrDefault(AdType.BANNER, emptyList()).size();
          final int interstitial = map.getOrDefault(AdType.INTERSTITIAL, emptyList()).size();
          final int video = map.getOrDefault(AdType.VIDEO, emptyList()).size();
          return String.format(
              "%s (%s):%n\tbanner: %s%n\tinterstitial: %s%n\tvideo: %s",
              country,
              country.getCode(),
              banner,
              interstitial,
              video
          );
        }).collect(Collectors.joining("\n"));
    return String.format(
        "Batch Job ID: %s, total entries count: %s. Statistics by counties:%n%s",
        this.adNetworkData.getBatchJobId(),
        this.adNetworkData.getEntriesCount(),
        countryStats
    );
  }

  @Override
  public String printContents() {
    final String cacheContents = Arrays.stream(Country.values())
        .map(country -> {
          final Map<AdType, List<AdNetwork>> map = adNetworkData.get(country);
          return String.format(
              "%s (%s):%n\tbanner: %s%n\tinterstitial: %s%n\tvideo: %s",
              country,
              country.getCode(),
              map.getOrDefault(AdType.BANNER, emptyList()),
              map.getOrDefault(AdType.INTERSTITIAL, emptyList()),
              map.getOrDefault(AdType.VIDEO, emptyList())
          );
        }).collect(Collectors.joining("\n"));
    return String.format(
        "Batch Job ID: %s, total entries count: %s.%n%s",
        this.adNetworkData.getBatchJobId(),
        this.adNetworkData.getEntriesCount(),
        cacheContents
    );
  }

  private void saveUpdatedDataOrStayWithOldOne(AdNetworkData updatedData) {
    if (null == updatedData || updatedData.getEntriesCount() == 0) {
      final String errorMessage =
          "failed to update AdNetworksData cache, old data is being used, updatedData = " + updatedData;
      LOG.error(errorMessage);
      throw new FailedToUpdateCachedAdNetworksDataException(errorMessage);
    } else {
      this.adNetworkData = new ImmutableAdNetworkData(updatedData);
      LOG.info("AdNetworksData cache updated successfully, it contains {} entries", updatedData.getEntriesCount());
    }
  }

  @Override
  public Map<AdType, List<AdNetwork>> get(Country country) {
    return adNetworkData.get(country);
  }

  @Override
  public List<AdNetwork> get(Country country, AdType adType) {
    return adNetworkData.get(country, adType);
  }

  @Override
  public int getEntriesCount() {
    return adNetworkData.getEntriesCount();
  }

  @Override
  public String getBatchJobId() {
    return adNetworkData.getBatchJobId();
  }

  @Override
  public String toString() {
    return "AdNetworkDataCacheImpl{" +
        "adNetworkData=" + adNetworkData +
        '}';
  }
}
