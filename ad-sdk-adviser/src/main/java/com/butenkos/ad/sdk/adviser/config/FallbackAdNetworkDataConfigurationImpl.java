package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.domain.Region;
import com.butenkos.ad.sdk.adviser.util.NullChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * awkward implementation due to limitation of storing datastructures in .properties file
 */
@Configuration
@RefreshScope
public class FallbackAdNetworkDataConfigurationImpl implements FallbackAdNetworkDataConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(FallbackAdNetworkDataConfigurationImpl.class);
  @Value("#{${ad.network.fallback.africa}}")
  private Map<String, List<String>> africa;
  @Value("#{${ad.network.fallback.asia}}")
  private Map<String, List<String>> asia;
  @Value("#{${ad.network.fallback.europe}}")
  private Map<String, List<String>> europe;
  @Value("#{${ad.network.fallback.north_america}}")
  private Map<String, List<String>> northAmerica;
  @Value("#{${ad.network.fallback.south_america}}")
  private Map<String, List<String>> southAmerica;
  @Value("#{${ad.network.fallback.oceania}}")
  private Map<String, List<String>> oceania;


  @Override
  public List<String> getAdNetworkNames(Region region, AdType adType) {
    NullChecker.checkNotNull(region, adType);
    switch (region) {
      case AFRICA:
        return getFallbackAdNetworkNamesForSpecifiedAdType(africa, adType);
      case ASIA:
        return getFallbackAdNetworkNamesForSpecifiedAdType(asia, adType);
      case EUROPE:
        return getFallbackAdNetworkNamesForSpecifiedAdType(europe, adType);
      case NORTH_AMERICA:
        return getFallbackAdNetworkNamesForSpecifiedAdType(northAmerica, adType);
      case SOUTH_AMERICA:
        return getFallbackAdNetworkNamesForSpecifiedAdType(southAmerica, adType);
      case OCEANIA:
        return getFallbackAdNetworkNamesForSpecifiedAdType(oceania, adType);
      default:
        LOG.warn("unknown region {}", region);
        return Collections.emptyList();
    }
  }

  private List<String> getFallbackAdNetworkNamesForSpecifiedAdType(Map<String, List<String>> fallbacks, AdType adType) {
    return Collections.unmodifiableList(
        Optional.ofNullable(fallbacks)
            .orElse(Collections.emptyMap())
            .getOrDefault(adType.toString(), Collections.emptyList())
    );
  }

  @Override
  public String toString() {
    return "FallbackAdNetworkDataConfigurationImpl{" +
        "africa=" + africa +
        ", asia=" + asia +
        ", europe=" + europe +
        ", northAmerica=" + northAmerica +
        ", southAmerica=" + southAmerica +
        ", oceania=" + oceania +
        '}';
  }
}
