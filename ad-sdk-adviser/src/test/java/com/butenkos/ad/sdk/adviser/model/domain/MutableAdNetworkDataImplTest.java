package com.butenkos.ad.sdk.adviser.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableAdNetworkDataImplTest {

  @Test
  public void emptyMutableAdNetworkDataNeverReturnsNull() {
    final MutableAdNetworkData mutableData = new MutableAdNetworkDataImpl();

    assertDoesNotThrow(() -> mutableData.get(Country.JAPAN));
    assertDoesNotThrow(() -> mutableData.get(Country.JAPAN, AdType.INTERSTITIAL));
    assertTrue(mutableData.get(Country.JAPAN).isEmpty());
    assertTrue(mutableData.get(Country.JAPAN, AdType.INTERSTITIAL).isEmpty());
  }
}