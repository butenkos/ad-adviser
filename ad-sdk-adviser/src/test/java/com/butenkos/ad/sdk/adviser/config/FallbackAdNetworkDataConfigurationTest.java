package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.BaseIntegrationTest;
import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.domain.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FallbackAdNetworkDataConfigurationTest extends BaseIntegrationTest {

  @Autowired
  private FallbackAdNetworkDataConfiguration config;

  //make sure values are configured in application.properties
  @Test
  public void returnsConfiguredDataForKnownRegionAndType() {
    assertFalse(config.getAdNetworkNames(Region.EUROPE, AdType.INTERSTITIAL).isEmpty());
    assertFalse(config.getAdNetworkNames(Region.ASIA, AdType.VIDEO).isEmpty());
  }

  @Test
  public void failsWhenNullPassedAsArgument() {
    assertThrows(IllegalArgumentException.class, () -> config.getAdNetworkNames(null, AdType.INTERSTITIAL));
    assertThrows(IllegalArgumentException.class, () -> config.getAdNetworkNames(Region.ASIA, null));
    assertThrows(IllegalArgumentException.class, () -> config.getAdNetworkNames(null, null));
  }
}