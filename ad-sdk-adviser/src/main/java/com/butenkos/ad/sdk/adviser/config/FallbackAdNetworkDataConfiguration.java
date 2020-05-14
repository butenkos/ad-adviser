package com.butenkos.ad.sdk.adviser.config;

import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.domain.Region;

import java.util.List;

public interface FallbackAdNetworkDataConfiguration {
  List<String> getAdNetworkNames(Region region, AdType adType);
}
