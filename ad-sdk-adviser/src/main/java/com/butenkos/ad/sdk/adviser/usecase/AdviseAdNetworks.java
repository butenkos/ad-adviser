package com.butenkos.ad.sdk.adviser.usecase;

import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import com.butenkos.ad.sdk.adviser.model.response.AdNetworkAdviseResponse;

public interface AdviseAdNetworks {
  AdNetworkAdviseResponse giveAdvice(AdNetworkAdviseRequest request);
}
