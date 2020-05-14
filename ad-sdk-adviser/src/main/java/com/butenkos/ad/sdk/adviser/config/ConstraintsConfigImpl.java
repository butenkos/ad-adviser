package com.butenkos.ad.sdk.adviser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Configuration
@RefreshScope
public class ConstraintsConfigImpl implements ConstraintsConfig {
  @Value("#{${ad.network.restriction.banned-in-country}}")
  private Map<String, Set<String>> countryRestriction;
  @Value("#{${ad.network.restriction.age}}")
  private Map<String, Set<String>> ageRestriction;
  @Value("#{${ad.network.restriction.os}}")
  private Map<String, Set<String>> osRestriction;
  @Value("#{${ad.network.restriction.os-version}}")
  private Map<String, Set<String>> osVersionRestriction;
  @Value("#{${ad.network.restriction.app}}")
  private Map<String, Set<String>> applicationRestriction;
  @Value("#{${ad.network.restriction.conflict}}")
  private Map<String, Set<String>> conflictingAdNetworks;

  /**
   * Seems like there is a bug in Spring Boot - it allows property evaluation by @Value inside the constructor
   * only on application startup, but it fails to resolve parameters when 'refresh' event occurs.
   * Normally this configuration should be read from the DB.
   */
  //  public ConstraintsConfig(
//      @Value("#{${network.restriction.banned-in-country}}") Map<String, String> bannedInCountrySourceMap,
//      @Value("#{${network.restriction.age}}") Map<String, String> ageRestrictionSourceMap,
//      @Value("#{${network.restriction.os}}") Map<String, String> osRestrictionSourceMap,
//      @Value("#{${network.restriction.os-version}}") Map<String, String> osVersionRestrictionSourceMap,
//      @Value("#{${network.restriction.app}}") Map<String, String> applicationRestrictionSourceMap,
//      @Value("#{${network.restriction.conflict}}") Map<String, String> conflictingAdNetworksSourceMap
//  ) {
//    this.bannedInCountry = parseBannedInCountry(bannedInCountrySourceMap);
//    this.ageRestriction = parseAgeRestriction(ageRestrictionSourceMap);
//    this.osRestriction = parseRegularMap(osRestrictionSourceMap);
//    this.osVersionRestriction = parseRegularMap(osVersionRestrictionSourceMap);
//    this.applicationRestriction = parseRegularMap(applicationRestrictionSourceMap);
//    this.conflictingAdNetworks = parseRegularMap(conflictingAdNetworksSourceMap);
//
//  }
//
//  private Map<Country, Set<String>> parseBannedInCountry(Map<String, String> sourceMap) {
//    final Map<Country, Set<String>> parsedMap = new EnumMap<>(Country.class);
//    sourceMap.forEach(
//        (key, value) -> parsedMap.put(
//            Country.findByCode(key),
//            new HashSet<>(Arrays.asList(value.split(",")))
//        )
//    );
//    return parsedMap;
//  }
//
//  private Map<Integer, Set<String>> parseAgeRestriction(Map<String, String> sourceMap) {
//    final Map<Integer, Set<String>> parsedMap = new HashMap<>();
//    sourceMap.forEach(
//        (key, value) -> {
//          parsedMap.put(Integer.parseInt(key), new HashSet<>(Arrays.asList(value.split(","))));
//        }
//    );
//    return parsedMap;
//  }
//
//  private Map<String, Set<String>> parseRegularMap(Map<String, String> sourceMap) {
//    final Map<String, Set<String>> parsedMap = new HashMap<>();
//    sourceMap.forEach(
//        (key, value) -> {
//          parsedMap.put(key, new HashSet<>(Arrays.asList(value.split(","))));
//        }
//    );
//    return parsedMap;
//  }
  //Android: 'Facebook,Tapjoy', Startapp: 'Vungle,AppBrain'
  @Override
  public Map<String, Set<String>> getCountryRestriction() {
    return Collections.unmodifiableMap(Optional.ofNullable(countryRestriction).orElse(Collections.emptyMap()));
  }

  @Override
  public Map<String, Set<String>> getAgeRestriction() {
    return Collections.unmodifiableMap(Optional.ofNullable(ageRestriction).orElse(Collections.emptyMap()));
  }

  @Override
  public Map<String, Set<String>> getOsRestriction() {
    return Collections.unmodifiableMap(Optional.ofNullable(osRestriction).orElse(Collections.emptyMap()));
  }

  @Override
  public Map<String, Set<String>> getOsVersionRestriction() {
    return Collections.unmodifiableMap(Optional.ofNullable(osVersionRestriction).orElse(Collections.emptyMap()));
  }

  @Override
  public Map<String, Set<String>> getApplicationRestriction() {
    return Collections.unmodifiableMap(Optional.ofNullable(applicationRestriction).orElse(Collections.emptyMap()));
  }

  @Override
  public Map<String, Set<String>> getConflictingAdNetworks() {
    return Collections.unmodifiableMap(Optional.ofNullable(conflictingAdNetworks).orElse(Collections.emptyMap()));
  }

  @Override
  public String toString() {
    return "ConstraintsConfigImpl{" +
        "bannedInCountry=" + countryRestriction +
        ", ageRestriction=" + ageRestriction +
        ", osRestriction=" + osRestriction +
        ", osVersionRestriction=" + osVersionRestriction +
        ", applicationRestriction=" + applicationRestriction +
        ", conflictingAdNetworks=" + conflictingAdNetworks +
        '}';
  }
}
