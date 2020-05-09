package com.butenkos.ad.sdk.supplier.model;

public interface ModifiableAdNetworkData extends AdNetworkData
{
  void put(Country country, AdType adType, AdNetwork adNetwork);
}
