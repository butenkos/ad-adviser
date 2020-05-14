package com.butenkos.ad.sdk.adviser.service.converter;

import com.butenkos.ad.sdk.adviser.model.domain.Country;
import com.butenkos.ad.sdk.adviser.model.domain.Country.CountryNotFoundException;
import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestConverterImplTest {

  @Test
  public void passesWithValidCountryCode() {
    final AdNetworkAdviseRequest request = createValidRequest();
    request.setCountryCode(Country.JAPAN.getCode());
    assertDoesNotThrow(() -> new RequestConverterImpl().convertRequest(request));
  }

  @Test
  public void failsOnUnknownCountry() {
    final AdNetworkAdviseRequest request = createValidRequest();
    request.setCountryCode("SomeUnknownCountry");
    assertThrows(CountryNotFoundException.class, () -> new RequestConverterImpl().convertRequest(request));
  }

  private AdNetworkAdviseRequest createValidRequest() {
    final AdNetworkAdviseRequest request = new AdNetworkAdviseRequest();
    request.setCountryCode(Country.ARGENTINA.getCode());
    request.setOperatingSystem("OldSystem");
    request.setOperatingSystemVersion("WithEvenOlderVersion");
    request.setApplicationName("Tom");
    request.setAgeRestriction(0);

    return request;
  }


}