package com.butenkos.ad.sdk.adviser.model.domain;

import java.util.List;
import java.util.Map;

/**
 * Structure designed to hold all the current AdNetwork information in memory at runtime.
 */
public interface AdNetworkData {
  Map<AdType, List<AdNetwork>> get(Country country);

  List<AdNetwork> get(Country country, AdType adType);

  int getEntriesCount();

  String getBatchJobId();
}
