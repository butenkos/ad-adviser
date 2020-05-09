package com.butenkos.ad.sdk.supplier.dao;

import com.butenkos.ad.sdk.supplier.model.AdNetworkData;

public interface AdNetworkDataDao {
  AdNetworkData getByBatchJobId(String batchJobId);
}
