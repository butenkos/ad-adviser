package com.butenkos.ad.sdk.adviser.service.validation;

import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;

public interface RequestValidator {
  void validateRequest(AdNetworkAdviseRequest request);
}
