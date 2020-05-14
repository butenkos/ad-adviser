package com.butenkos.ad.sdk.adviser.service.data;

import com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDaoImpl;
import com.butenkos.ad.sdk.adviser.model.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdNetworkDataCacheImplTest {

  @Test
  public void returnsEmptyCollectionsWhenNoDataFound() {
    final AdNetworkData data = new MutableAdNetworkDataImpl();
    final AdNetworkDataCache cache = new AdNetworkDataCacheImpl(new AdNetworkDataDaoImpl(null), data);
    assertTrue(cache.get(Country.SCOTLAND).isEmpty());
    assertTrue(cache.get(Country.SCOTLAND, AdType.INTERSTITIAL).isEmpty());
  }

  @Test
  public void entryCountIsZeroWhenEmpty() {
    final AdNetworkData data = new MutableAdNetworkDataImpl();
    final AdNetworkDataCache cache = new AdNetworkDataCacheImpl(new AdNetworkDataDaoImpl(null), data);
    assertEquals(0, cache.getEntriesCount());
  }

  @Test
  public void canServeData() {
    final MutableAdNetworkData data = new MutableAdNetworkDataImpl();
    final AdNetwork one = new AdNetwork("one", (byte) 5);
    data.put(Country.URUGUAY, AdType.INTERSTITIAL, one);
    final AdNetwork two = new AdNetwork("two", (byte) 7);
    data.put(Country.SAR, AdType.VIDEO, two);
    final AdNetworkDataCache cache = new AdNetworkDataCacheImpl(new AdNetworkDataDaoImpl(null), data);
    assertEquals(2, cache.getEntriesCount());
    assertEquals(1, cache.get(Country.URUGUAY, AdType.INTERSTITIAL).size());
    assertEquals(1, cache.get(Country.SAR, AdType.VIDEO).size());
    assertEquals(one.getName(), cache.get(Country.URUGUAY, AdType.INTERSTITIAL).get(0).getName());
    assertEquals(two.getName(), cache.get(Country.SAR, AdType.VIDEO).get(0).getName());
  }

  @Test
  public void doesNotAllowNullsInConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new AdNetworkDataCacheImpl(null, null));
    assertThrows(IllegalArgumentException.class, () -> new AdNetworkDataCacheImpl(
        new AdNetworkDataDaoImpl(null) , null
        )
    );
    assertThrows(IllegalArgumentException.class, () -> new AdNetworkDataCacheImpl(
            null , new MutableAdNetworkDataImpl()
        )
    );
  }


}