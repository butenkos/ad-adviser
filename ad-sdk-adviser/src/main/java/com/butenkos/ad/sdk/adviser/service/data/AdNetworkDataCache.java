package com.butenkos.ad.sdk.adviser.service.data;

import com.butenkos.ad.sdk.adviser.model.domain.AdNetworkData;

/**
 * {@code AdNetworkDataCache} wraps {@code AdNetworkData} and allows
 * to update data on demand
 *
 * @see com.butenkos.ad.sdk.adviser.model.domain.AdNetworkData
 */
public interface AdNetworkDataCache extends AdNetworkData {
  void update(String batchJobId);

  void updateWithMostRecentData();

  String getCacheStatistics();

  String printContents();
}
