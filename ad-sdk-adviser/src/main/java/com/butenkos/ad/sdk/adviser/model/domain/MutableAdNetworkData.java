package com.butenkos.ad.sdk.adviser.model.domain;

import java.util.List;
import java.util.Map;

/**
 * implementations of this interface should be used exclusively in DAO
 * during fetching the data from the DB in order to avoid cache pollution
 * For other cases please use {@code ImmutableAdNetworkData}
 *
 * @see com.butenkos.ad.sdk.adviser.model.domain.ImmutableAdNetworkData
 */
public interface MutableAdNetworkData extends AdNetworkData {
  Map<Country, Map<AdType, List<AdNetwork>>> getAllContents();

  void put(Country country, AdType adType, AdNetwork adNetwork);

  void setBatchJobId(String batchJobId);
}
