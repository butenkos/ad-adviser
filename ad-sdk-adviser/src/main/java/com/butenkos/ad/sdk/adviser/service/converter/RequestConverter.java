package com.butenkos.ad.sdk.adviser.service.converter;

import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;

public interface RequestConverter {
  RequestData convertRequest(AdNetworkAdviseRequest request);
}
