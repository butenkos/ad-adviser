package com.butenkos.ad.sdk.adviser.service.validation;

import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestValidatorImplTest {
  private static final RequestValidator VALIDATOR = new RequestValidatorImpl(
      Validation.buildDefaultValidatorFactory().getValidator()
  );
  private AdNetworkAdviseRequest request;

  @BeforeEach
  public void prepareRequest() {
    request = prepareValidRequest();
  }

  @Test
  public void validRequestPassesThrough() {
    assertDoesNotThrow(() -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void countryMustNotBeNull() {
    request.setCountryCode(null);
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void countryMustNotBeEmpty() {
    request.setCountryCode("");
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void appNameMustNotBeNull() {
    request.setApplicationName(null);
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void appNameMustNotBeEmpty() {
    request.setApplicationName(" ");
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void osMustNotBeNull() {
    request.setOperatingSystem(null);
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void osMustNotBeEmpty() {
    request.setOperatingSystem("");
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void osVersionMustNotBeNull() {
    request.setOperatingSystem(null);
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void osVersionMustNotBeEmpty() {
    request.setOperatingSystem("");
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void ageCanBeGreaterThanZero() {
    request.setAgeRestriction(120);
    assertDoesNotThrow(() -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void ageCanBeZero() {
    request.setAgeRestriction(0);
    assertDoesNotThrow(() -> VALIDATOR.validateRequest(request));
  }

  @Test
  public void ageCannotBeLessThanZero() {
    request.setAgeRestriction(-1);
    assertThrows(BeanValidationException.class, () -> VALIDATOR.validateRequest(request));
  }

  private AdNetworkAdviseRequest prepareValidRequest() {
    final AdNetworkAdviseRequest request = new AdNetworkAdviseRequest();
    request.setAgeRestriction(0);
    request.setApplicationName("Tom");
    request.setCountryCode("Neverland");
    request.setOperatingSystem("TheBestSystem");
    request.setOperatingSystemVersion("TheNewestVersion");
    return request;
  }
}