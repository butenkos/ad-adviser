package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDao;
import com.butenkos.ad.sdk.adviser.model.domain.ImmutableAdNetworkData;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCacheImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public AdNetworkDataCache getAdNetworkDataCache(AdNetworkDataDao adNetworkDataDao) {
    //populating cache on application startup
    return new AdNetworkDataCacheImpl(adNetworkDataDao, new ImmutableAdNetworkData(adNetworkDataDao.getMostRecent()));
  }

}
