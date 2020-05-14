package com.butenkos.ad.sdk.adviser.model.domain;

import com.butenkos.ad.sdk.adviser.util.NullChecker;

import java.util.Arrays;

import static com.butenkos.ad.sdk.adviser.model.domain.Region.*;

/*
 * enum representing country, in which device is being use.
 * It also has a notion of the region, which helps to search AdNetwork data
 * for the countries of the same region, if nothing was found for the
 * country specified in the request
 *
 * For simplicity just few countries are available
 */
public enum Country {
  ANGOLA("AGO", AFRICA),
  ALGERIA("DZA", AFRICA),
  MOROCCO("MAR", AFRICA),
  NIGERIA("NGA", AFRICA),
  SAR("ZAF", AFRICA),
  BRAZIL("BRA", SOUTH_AMERICA),
  ARGENTINA("ARG", SOUTH_AMERICA),
  URUGUAY("URY", SOUTH_AMERICA),
  CHILE("CHL", SOUTH_AMERICA),
  ECUADOR("ECU", SOUTH_AMERICA),
  USA("USA", NORTH_AMERICA),
  CANADA("CAN", NORTH_AMERICA),
  JAPAN("JPN", ASIA),
  CHINA("CHN", ASIA),
  ITALY("ITA", EUROPE),
  SPAIN("ESP", EUROPE),
  ENGLAND("ENG", EUROPE),
  RUSSIA("RUS", EUROPE),
  SLOVENIA("SVN", EUROPE),
  AUSTRIA("AUT", EUROPE),
  GERMANY("DEU", EUROPE),
  FRANCE("FRA", EUROPE),
  SCOTLAND("SCO", EUROPE),
  IRELAND("IRL", EUROPE),
  AUSTRALIA("AUS", OCEANIA),
  NEW_ZEALAND("NZL", OCEANIA);

  private final String code;
  private final Region region;

  Country(String code, Region region) {
    this.code = code;
    this.region = region;
  }

  public String getCode() {
    return this.code;
  }

  public Region getRegion() {
    return region;
  }

  public static Country findByCode(String code) {
    NullChecker.checkNotNull(code);
    if ("".equals(code.trim())) {
      throw new CountryNotFoundException(code);
    }
    return Arrays.stream(values())
        .filter(country -> country.code.equalsIgnoreCase(code))
        .findFirst()
        .orElseThrow(() -> new CountryNotFoundException(code));
  }

  public static final class CountryNotFoundException extends RuntimeException {
    CountryNotFoundException(String code) {
      super("country for the following code was not found: " + code);
    }
  }
}
