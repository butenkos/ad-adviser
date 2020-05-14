//package com.butenkos.ad.sdk.adviser.usecase;
//
//import com.butenkos.ad.sdk.adviser.config.FallbackAdNetworkDataConfiguration;
//import com.butenkos.ad.sdk.adviser.config.FallbackAdNetworkDataConfigurationImpl;
//import com.butenkos.ad.sdk.adviser.dao.AdNetworkDataDaoImpl;
//import com.butenkos.ad.sdk.adviser.model.domain.*;
//import com.butenkos.ad.sdk.adviser.service.DummyConstraintsConfig;
//import com.butenkos.ad.sdk.adviser.service.converter.RequestConverterImpl;
//import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
//import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCacheImpl;
//import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManagerImpl;
//import org.junit.jupiter.api.Test;
//
//
//class AdviseAdNetworksImplTest {
//
//  //  @Autowired
////  private AdNetworkDataCache cache;
////  @Autowired
////  private FallbackAdNetworkDataConfiguration configuration;
//
//  @Test
//  public void foo() {
//    ModifiableAdNetworkData networkData = new ModifiableAdNetworkDataImpl();
//    networkData.put(Country.ENGLAND, AdType.INTERSTITIAL, new AdNetwork("SomeNetwork", (byte) 6));
//    networkData.put(Country.ENGLAND, AdType.BANNER, new AdNetwork("SomeOtherNetwork", (byte) 9));
//    networkData.put(Country.ENGLAND, AdType.VIDEO, new AdNetwork("SomeTotallyDifferentNetwork", (byte) 99));
//    final AdNetworkDataCache cache = new AdNetworkDataCacheImpl(new AdNetworkDataDaoImpl(null), networkData);
//
//    final FallbackAdNetworkDataConfiguration configuration=new DummyConstraintsConfig();
//    final AdviseAdNetworks adviser = new AdviseAdNetworksImpl(
//        (request) -> {
//        },
//        new RequestConverterImpl(),
//        cache,
//        new ConstraintsManagerImpl(new DummyConstraintsConfig()),
//        configuration
//    );
//  }
//
//}