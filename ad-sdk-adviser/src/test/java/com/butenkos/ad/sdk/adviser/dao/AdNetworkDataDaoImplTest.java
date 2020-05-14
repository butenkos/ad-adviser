package com.butenkos.ad.sdk.adviser.dao;

import com.butenkos.ad.sdk.adviser.BaseIntegrationTest;
import com.butenkos.ad.sdk.adviser.helper.TestDataCreator;
import com.butenkos.ad.sdk.adviser.model.domain.AdNetwork;
import com.butenkos.ad.sdk.adviser.model.domain.AdNetworkData;
import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.domain.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@Sql({"/sql/schema.sql"})
class AdNetworkDataDaoImplTest extends BaseIntegrationTest {

  @Autowired
  private AdNetworkDataDao dataDao;
  @Autowired
  private TestDataCreator testDataCreator;

  @Test
  public void listsOfAdNetworksAlreadySortedInDescendingOrderWhenFetchedFromDatabase() {
    final List<String> batchJobIds = testDataCreator.generateTestDataInDb(1, Country.SLOVENIA);
    final AdNetworkData data = dataDao.getByBatchJobId(batchJobIds.get(0));
    assertTrue(isSortedDescendingByScore(data.get(Country.SLOVENIA, AdType.BANNER)));
    assertTrue(isSortedDescendingByScore(data.get(Country.SLOVENIA, AdType.INTERSTITIAL)));
    assertTrue(isSortedDescendingByScore(data.get(Country.SLOVENIA, AdType.VIDEO)));
  }

  private boolean isSortedDescendingByScore(List<AdNetwork> adNetworkList) {
    for (int i = 0; i < adNetworkList.size() - 1; i++) {
      if (adNetworkList.get(i).getScore() < adNetworkList.get(i + 1).getScore()) {
        return false;
      }
    }
    return true;
  }
}