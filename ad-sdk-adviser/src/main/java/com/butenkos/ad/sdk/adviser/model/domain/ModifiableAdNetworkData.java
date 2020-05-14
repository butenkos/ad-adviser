package com.butenkos.ad.sdk.adviser.model.domain;

/**
 * implementations of this interface should be used exclusively in DAO
 * during fetching the data from the DB in order to avoid cache pollution
 * For other cases please use {@code ImmutableAdNetworkData}
 *
 * @see com.butenkos.ad.sdk.adviser.model.domain.ImmutableAdNetworkData
 */
public interface ModifiableAdNetworkData extends AdNetworkData {
  void put(Country country, AdType adType, AdNetwork adNetwork);

  void setBatchJobId(String batchJobId);
}
