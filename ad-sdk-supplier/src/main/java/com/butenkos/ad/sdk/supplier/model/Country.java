package com.butenkos.ad.sdk.supplier.model;

import java.util.Arrays;

public enum Country {
  ANGOLA("AGO"),
  ALGERIA("DZA"),
  MOROCCO("MAR"),
  NIGERIA("NGA"),
  SAR("ZAF"),
  BRAZIL("BRA"),
  ARGENTINA("ARG"),
  URUGUAY("URY"),
  CHILE("CHL"),
  ECUADOR("ECU"),
  ITALY("ITA"),
  SPAIN("ESP"),
  ENGLAND("ENG"),
  RUSSIA("RUS"),
  SLOVENIA("SVN"),
  AUSTRIA("AUT"),
  GERMANY("DEU"),
  FRANCE("FRA"),
  SCOTLAND("SCO"),
  IRELAND("IRL"),
  AUSTRALIA("AUS"),
  NEW_ZEALAND("NZL");

  private final String code;

  Country(String code) {
    this.code = code;
  }

//  public String getCode() {
//    return this.code;
//  }

  public static Country findByCode(String code) {
    return Arrays.stream(values())
        .filter(country -> country.code.equals(code))
        .findFirst()
        .orElseThrow(() -> new CountryNotFoundException(code));
  }

  private static final class CountryNotFoundException extends RuntimeException {
    CountryNotFoundException(String code) {
      super("country for the following code was not found: " + code);
    }
  }
}
