package com.butenkos.ad.sdk.adviser.model.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableAdNetworkDataTest {

  @Test
  public void oneCannotPutNewEntriesInReturnedAdTypeToAdNetworkMap() {
    final MutableAdNetworkData mutableData = new MutableAdNetworkDataImpl();
    mutableData.put(Country.URUGUAY, AdType.INTERSTITIAL, new AdNetwork("one", (byte) 4));
    final ImmutableAdNetworkData immutableData = new ImmutableAdNetworkData(mutableData);

    assertThrows(
        UnsupportedOperationException.class,
        () -> immutableData.get(Country.URUGUAY).put(
            AdType.INTERSTITIAL, Collections.singletonList(new AdNetwork(" two", (byte) 88))
        )
    );
  }

  @Test
  public void oneCannotAddNewAdDataToReturnedListOfNetworkData() {
    final MutableAdNetworkData mutableData = new MutableAdNetworkDataImpl();
    mutableData.put(Country.URUGUAY, AdType.INTERSTITIAL, new AdNetwork("one", (byte) 4));
    final ImmutableAdNetworkData immutableData = new ImmutableAdNetworkData(mutableData);

    assertThrows(
        UnsupportedOperationException.class,
        () -> immutableData.get(Country.URUGUAY).get(AdType.INTERSTITIAL).add(new AdNetwork(" two", (byte) 88))
    );
  }
}