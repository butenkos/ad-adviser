package com.butenkos.ad.sdk.adviser.dao;

import com.butenkos.ad.sdk.adviser.model.domain.ModifiableAdNetworkData;

/**
 * Implementation of the interface is responsible to populate an instance of {@code AdNetworkDataCache}
 */
public interface AdNetworkDataDao {
  /**
   * used to read the data of the specific batch job
   *
   * @param batchJobId - unique batch job identifier
   * @return - all the AdNetwork data collected during specified batch job
   */
  ModifiableAdNetworkData getByBatchJobId(String batchJobId);

  /**
   * used to read the data the most recent batch job
   *
   * @return - all the AdNetwork data collected during the most recent
   */
  ModifiableAdNetworkData getMostRecent();
}
