package com.butenkos.ad.sdk.adviser.service.converter;

import com.butenkos.ad.sdk.adviser.model.domain.Country;
import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestConverterImpl implements RequestConverter {
  private static final Logger LOG = LoggerFactory.getLogger(RequestConverterImpl.class);

  @Override
  public RequestData convertRequest(AdNetworkAdviseRequest request) {
    LOG.debug("convertRequest(), {}", request);
    final Country country = Country.findByCode(request.getCountryCode().toUpperCase());
    return new RequestData(
        country,
        request.getOperatingSystem(),
        request.getOperatingSystemVersion(),
        request.getApplicationName(),
        request.getAgeRestriction()
    );
  }
}
