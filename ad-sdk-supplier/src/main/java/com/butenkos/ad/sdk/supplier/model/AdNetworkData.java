package com.butenkos.ad.sdk.supplier.model;

import java.util.List;
import java.util.Map;

public interface AdNetworkData {
  Map<AdType, List<AdNetwork>> get(Country country);

  List<AdNetwork> get(Country country, AdType adType);
}
