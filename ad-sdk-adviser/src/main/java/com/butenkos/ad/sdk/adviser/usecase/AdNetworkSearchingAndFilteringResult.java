package com.butenkos.ad.sdk.adviser.usecase;

import com.butenkos.ad.sdk.adviser.model.domain.AdType;

import java.util.List;

/**
 * data structure to keep the result of searching and filtering ad networks
 */
public class AdNetworkSearchingAndFilteringResult {
  private final List<String> adNetworkNames;
  private final AdType adType;
  private final boolean fallbackUsed;

  AdNetworkSearchingAndFilteringResult(List<String> adNetworkNames, AdType adType, boolean fallbackUsed) {
    this.adNetworkNames = adNetworkNames;
    this.adType = adType;
    this.fallbackUsed = fallbackUsed;
  }

  AdNetworkSearchingAndFilteringResult(List<String> adNetworkNames, AdType adType) {
    this(adNetworkNames, adType, false);
  }

  public List<String> getAdNetworkNames() {
    return adNetworkNames;
  }

  public AdType getAdType() {
    return adType;
  }

  public boolean isFallbackUsed() {
    return fallbackUsed;
  }
}
