package com.butenkos.ad.sdk.adviser.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryTest {

  @Test
  public void canFindCountryByCode() {
    assertEquals(Country.AUSTRALIA, Country.findByCode("AUS"));
    assertEquals(Country.CANADA, Country.findByCode("CAN"));
  }

  @Test
  public void throwsExceptionWhenNullPassed() {
    assertThrows(IllegalArgumentException.class, () -> Country.findByCode(null));
  }

  @Test
  public void throwsExceptionWhenEmptyStringPassed() {
    assertThrows(RuntimeException.class, () -> Country.findByCode(null));
  }

}