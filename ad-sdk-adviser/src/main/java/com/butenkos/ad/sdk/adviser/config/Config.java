package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDao;
import com.butenkos.ad.sdk.adviser.model.domain.AdNetworkData;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCacheImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public AdNetworkDataCache getAdNetworkDataCache(AdNetworkDataDao adNetworkDataDao) {
    //populating cache on application startup
    final AdNetworkData mostRecentData = adNetworkDataDao.getMostRecent();
    return new AdNetworkDataCacheImpl(adNetworkDataDao, mostRecentData);
  }

}
