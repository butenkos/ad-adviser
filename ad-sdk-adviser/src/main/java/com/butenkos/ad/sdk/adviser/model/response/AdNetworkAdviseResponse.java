package com.butenkos.ad.sdk.adviser.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * POJO representation of JSON response.
 */
@SuppressWarnings("unused")
public class AdNetworkAdviseResponse {
  private List<String> bannerAdNetworks;
  private List<String> interstitialAdNetworks;
  private List<String> videoAdNetworks;
  @Schema(
      name = "warning",
      description = "Notifies, whether fallback list of ad networks was sent, and, also the set of networks "
          + "which cannot be served due to configured restrictions (except for conflicting networks)"
  )
  private String warning;

  public List<String> getBannerAdNetworks() {
    return bannerAdNetworks;
  }

  public void setBannerAdNetworks(List<String> bannerAdNetworks) {
    this.bannerAdNetworks = bannerAdNetworks;
  }

  public List<String> getInterstitialAdNetworks() {
    return interstitialAdNetworks;
  }

  public void setInterstitialAdNetworks(List<String> interstitialAdNetworks) {
    this.interstitialAdNetworks = interstitialAdNetworks;
  }

  public List<String> getVideoAdNetworks() {
    return videoAdNetworks;
  }

  public void setVideoAdNetworks(List<String> videoAdNetworks) {
    this.videoAdNetworks = videoAdNetworks;
  }

  public String getWarning() {
    return warning;
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }
}
