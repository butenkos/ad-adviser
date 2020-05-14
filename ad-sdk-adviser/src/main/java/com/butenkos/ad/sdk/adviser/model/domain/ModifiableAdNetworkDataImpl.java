package com.butenkos.ad.sdk.adviser.model.domain;

import java.util.*;

import static com.butenkos.ad.sdk.adviser.util.NullChecker.checkNotNull;

/**
 * Not thread safe, mutable, should be used only for extraction of the data from result set.
 * Then it should be wrapped in {@code ImmutableAdNetworkDataImpl}
 *
 * @see com.butenkos.ad.sdk.adviser.model.domain.AdNetworkData
 * @see com.butenkos.ad.sdk.adviser.model.domain.ImmutableAdNetworkData
 */
public class ModifiableAdNetworkDataImpl implements ModifiableAdNetworkData {
  private final Map<Country, Map<AdType, List<AdNetwork>>> countryToTypeToNetwork =
      new EnumMap<>(Country.class);
  private int entriesCount = 0;
  private String batchJobId;

  @Override
  public Map<AdType, List<AdNetwork>> get(Country country) {
    checkNotNull(country);
    return getAdTypeToAdNetworkListMap(country);
  }

  @Override
  public List<AdNetwork> get(Country country, AdType adType) {
    checkNotNull(country, adType);
    return Optional.ofNullable(get(country).get(adType)).orElseGet(Collections::emptyList);
  }

  @Override
  public int getEntriesCount() {
    return entriesCount;
  }

  @Override
  public String getBatchJobId() {
    return batchJobId;
  }

  @Override
  public void put(Country country, AdType adType, AdNetwork adNetwork) {
    checkNotNull(country, adType, adNetwork);
    countryToTypeToNetwork.computeIfAbsent(country, k -> new EnumMap<>(AdType.class));
    countryToTypeToNetwork.get(country).computeIfAbsent(adType, k -> new ArrayList<>());
    countryToTypeToNetwork.get(country).get(adType).add(adNetwork);
    entriesCount++;
  }

  @Override
  public void setBatchJobId(String batchJobId) {
    this.batchJobId = batchJobId;
  }

  private Map<AdType, List<AdNetwork>> getAdTypeToAdNetworkListMap(Country country) {
    return Optional.ofNullable(countryToTypeToNetwork.get(country))
        .orElseGet(Collections::emptyMap);
  }

  @Override
  public String toString() {
    return "AdNetworkDataImpl{" +
        "entriesCount=" + entriesCount +
        ", batchJobId='" + batchJobId + '\'' +
        '}';
  }
}
