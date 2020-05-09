package com.butenkos.ad.sdk.supplier.model;

import java.util.*;

import static com.butenkos.ad.sdk.supplier.util.NullChecker.checkNotNull;

public class AdNetworkDataImpl implements ModifiableAdNetworkData {
  private final Map<Country, Map<AdType, List<AdNetwork>>> countryToTypeToNetwork =
      new EnumMap<>(Country.class);

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
  public void put(Country country, AdType adType, AdNetwork adNetwork) {
    checkNotNull(country, adType, adNetwork);
    countryToTypeToNetwork.computeIfAbsent(country, k -> new EnumMap<>(AdType.class));
    countryToTypeToNetwork.get(country).computeIfAbsent(adType, k -> new ArrayList<>());
    countryToTypeToNetwork.get(country).get(adType).add(adNetwork);
  }

  private Map<AdType, List<AdNetwork>> getAdTypeToAdNetworkListMap(Country country) {
    return Optional.ofNullable(countryToTypeToNetwork.get(country))
        .orElseGet(Collections::emptyMap);
  }

  @Override
  public String toString() {
    return "AdNetworkDataImpl{" + "countryToTypeToNetwork=" + countryToTypeToNetwork + '}';
  }
}
