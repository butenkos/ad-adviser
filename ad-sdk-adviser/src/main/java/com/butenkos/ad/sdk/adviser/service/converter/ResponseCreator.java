package com.butenkos.ad.sdk.adviser.service.converter;

import com.butenkos.ad.sdk.adviser.model.response.AdNetworkAdviseResponse;
import com.butenkos.ad.sdk.adviser.usecase.AdNetworkSearchingAndFilteringResult;

import java.util.List;
import java.util.Set;

public interface ResponseCreator {
  AdNetworkAdviseResponse createResponse(
      List<AdNetworkSearchingAndFilteringResult> results,
      Set<String> adNetworkNamesToAvoid
  );
}
