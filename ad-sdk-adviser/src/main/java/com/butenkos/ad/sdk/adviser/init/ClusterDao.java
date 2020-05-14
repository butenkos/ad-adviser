package com.butenkos.ad.sdk.adviser.init;

import java.util.List;

/**
 * Registers and unregisters the application in 'cluster' by means of
 * saving its {@code ApplicationStatus} and URL to specified table in DB
 */
public interface ClusterDao {
  void registerInCluster(String applicationUrl);

  void unregisterInCluster(String applicationUrl);

  List<String> getOtherRunningInstances(String applicationUrl);
}
