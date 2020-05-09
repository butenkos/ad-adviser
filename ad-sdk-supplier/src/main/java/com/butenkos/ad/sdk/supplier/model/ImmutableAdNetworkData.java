package com.butenkos.ad.sdk.supplier.model;

import java.util.List;
import java.util.Map;

import static com.butenkos.ad.sdk.supplier.util.NullChecker.checkNotNull;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public class ImmutableAdNetworkData implements AdNetworkData {
  private final AdNetworkData adNetworkData;

  public ImmutableAdNetworkData(AdNetworkData adNetworkData) {
    checkNotNull(adNetworkData);
    this.adNetworkData = adNetworkData;
  }

  @Override
  public Map<AdType, List<AdNetwork>> get(Country country) {
    return unmodifiableMap(adNetworkData.get(country));
  }

  @Override
  public List<AdNetwork> get(Country country, AdType adType) {
    return unmodifiableList(adNetworkData.get(country, adType));
  }

  @Override
  public String toString() {
    return "ImmutableAdNetworkData{" + "adNetworkData=" + adNetworkData + '}';
  }
}
