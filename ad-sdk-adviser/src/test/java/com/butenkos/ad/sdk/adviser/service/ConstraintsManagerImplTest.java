package com.butenkos.ad.sdk.adviser.service;

import com.butenkos.ad.sdk.adviser.model.domain.AdNetwork;
import com.butenkos.ad.sdk.adviser.model.domain.Country;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;
import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManager;
import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.butenkos.ad.sdk.adviser.model.domain.Country.*;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class ConstraintsManagerImplTest {
  private static final List<AdNetwork> AD_NETWORKS = Collections.unmodifiableList(
      Stream.of(
          new AdNetwork("ONE", (byte) 50),
          new AdNetwork("TWO", (byte) 40),
          new AdNetwork("THREE", (byte) 30),
          new AdNetwork("FOUR", (byte) 20),
          new AdNetwork("FIVE", (byte) 10)
      ).collect(Collectors.toList())
  );
  private static final List<String> AD_NETWORK_NAMES = Collections.unmodifiableList(
      AD_NETWORKS.stream()
          .map(AdNetwork::getName)
          .collect(Collectors.toList())
  );
  private DummyConstraintsConfig config;

  @BeforeEach
  public void prepareAdNetworkListData() {
    config = new DummyConstraintsConfig();
  }

  @Test
  public void whenConstraintsDataIsEmptyNoNetworksFilteredOut() {
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        ARGENTINA, "SomeSystem", "SomeVersion", "SomeApp", 99
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);

    assertEquals(AD_NETWORK_NAMES.size(), filteredNetworksList.size());
    assertTrue(filteredNetworksList.addAll(AD_NETWORK_NAMES));
  }

  @Test
  public void canFilterOutNetworksWhichHasAgeRestrictions() {
    final List<String> mustBeFilteredOut = AD_NETWORK_NAMES.subList(0, 2);
    final int minAge = 6;
    config.setAgeRestriction(createRestriction("" + minAge, mustBeFilteredOut));
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        ALGERIA, "", "", "", minAge - 1
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);

    assertEquals(AD_NETWORK_NAMES.size() - mustBeFilteredOut.size(), filteredNetworksList.size());
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(0)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(1)));
    assertTrue(filteredNetworksList.containsAll(AD_NETWORK_NAMES.subList(mustBeFilteredOut.size(), AD_NETWORK_NAMES.size())));
  }

  @Test
  public void canFilterOutNetworksWhichHasCountryRestrictions() {
    final List<String> mustBeFilteredOut = AD_NETWORK_NAMES.subList(0, 3);
    final Country countryWhereAdNetworkNotAllowed = AUSTRALIA;
    config.setBannedInCountry(createRestriction(countryWhereAdNetworkNotAllowed.getCode(), mustBeFilteredOut));
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        countryWhereAdNetworkNotAllowed, "", "", "", 3
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);

    assertEquals(AD_NETWORK_NAMES.size() - mustBeFilteredOut.size(), filteredNetworksList.size());
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(0)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(1)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(2)));
    assertTrue(filteredNetworksList.containsAll(AD_NETWORK_NAMES.subList(mustBeFilteredOut.size(), AD_NETWORK_NAMES.size())));
  }

  @Test
  public void canFilterOutNetworksWhichHasOperatingSystemRestrictions() {
    final List<String> mustBeFilteredOut = AD_NETWORK_NAMES.subList(3, AD_NETWORK_NAMES.size());
    final String notAllowedOperatingSystem = "Android";
    config.setOsRestriction(createRestriction(notAllowedOperatingSystem, mustBeFilteredOut));
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        ALGERIA, notAllowedOperatingSystem, "", "", 3
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);

    assertEquals(AD_NETWORK_NAMES.size() - mustBeFilteredOut.size(), filteredNetworksList.size());
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(3)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(4)));
    assertTrue(filteredNetworksList.containsAll(AD_NETWORK_NAMES.subList(0, mustBeFilteredOut.size())));
  }

  @Test
  public void canFilterOutNetworksWhichHasOperatingSystemVersionRestrictions() {
    final List<String> mustBeFilteredOut = AD_NETWORK_NAMES.subList(2, 4);
    config.setOsVersionRestriction(createRestriction("Android6.0", mustBeFilteredOut));
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        BRAZIL, "Android", "6.0", "", 99
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);

    assertEquals(AD_NETWORK_NAMES.size() - mustBeFilteredOut.size(), filteredNetworksList.size());
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(2)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(3)));
    assertTrue(
        filteredNetworksList.containsAll(
            AD_NETWORK_NAMES.stream().filter(o -> !mustBeFilteredOut.contains(o)).collect(Collectors.toList())
        )
    );
  }

  @Test
  public void canFilterOutNetworksByMultipleCriteria() {
    config.setBannedInCountry(createRestriction(CHINA.getCode(), singletonList(AD_NETWORK_NAMES.get(0))));
    config.setAgeRestriction(createRestriction("" + 10, singletonList(AD_NETWORK_NAMES.get(1))));
    config.setOsVersionRestriction(createRestriction("Android999", singletonList(AD_NETWORK_NAMES.get(2))));
    config.setApplicationRestriction(createRestriction("SuperMegaApp", singletonList(AD_NETWORK_NAMES.get(3))));
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final RequestData requestData = new RequestData(
        CHINA, "Android", "999", "SuperMegaApp", 9
    );
    final List<String> filteredNetworksList = applyConstrainsToAdNetworkList(manager, requestData);
    assertEquals(1, filteredNetworksList.size());
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(0)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(1)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(2)));
    assertFalse(filteredNetworksList.contains(AD_NETWORK_NAMES.get(3)));
    assertTrue(filteredNetworksList.contains(AD_NETWORK_NAMES.get(4)));
    assertEquals(1, filteredNetworksList.size());
  }

  @Test
  public void testNoAdNetworkConflictsAndNoNetworkRemovedFromListWhenNoConflictsDataConfigured() {
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    assertEquals(AD_NETWORKS, manager.eliminateAdNetworkConflicts(AD_NETWORKS));
  }

  @Test
  public void testAdNetworkConflictsWhenCorrespondingConflictsDataConfigured() {
    config.setConflictingAdNetworks(
        createRestriction(AD_NETWORKS.get(0).getName(), singletonList(AD_NETWORKS.get(2).getName()))
    );
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final List<AdNetwork> resolvedNetworkList = manager.eliminateAdNetworkConflicts(AD_NETWORKS);
    assertEquals(AD_NETWORKS.size() - 1, resolvedNetworkList.size());
    assertFalse(resolvedNetworkList.contains(AD_NETWORKS.get(2)));
  }

  @Test
  public void testWhenAdNetworkConflictThenFilterOutOnesWithLowestRating() {
    config.setConflictingAdNetworks(
        createRestriction(
            AD_NETWORKS.get(1).getName(),
            Arrays.asList(AD_NETWORKS.get(0).getName(), AD_NETWORKS.get(2).getName()))
    );
    final ConstraintsManager manager = new ConstraintsManagerImpl(config);
    final List<AdNetwork> noMoreConflicts = manager.eliminateAdNetworkConflicts(AD_NETWORKS);
    assertTrue(AD_NETWORKS.get(1).getScore() < AD_NETWORKS.get(0).getScore());
    assertTrue(AD_NETWORKS.get(2).getScore() < AD_NETWORKS.get(1).getScore());
    assertEquals(AD_NETWORKS.size() - 2, noMoreConflicts.size());
    assertTrue(noMoreConflicts.contains(AD_NETWORKS.get(0)));
    assertFalse(noMoreConflicts.contains(AD_NETWORKS.get(1)));
    assertFalse(noMoreConflicts.contains(AD_NETWORKS.get(2)));
  }

  private List<String> applyConstrainsToAdNetworkList(ConstraintsManager manager, RequestData requestData) {
    return AD_NETWORK_NAMES.stream()
        .filter(adNetwork -> !manager.getAdNetworkNamesToAvoid(requestData).contains(adNetwork))
        .collect(toList());
  }

  private Map<String, Set<String>> createRestriction(String key, Collection<String> adNetworks) {
    final Set<String> networks = new HashSet<>(adNetworks);
    networks.addAll(adNetworks);
    final Map<String, Set<String>> map = new HashMap<>();
    map.put(key, networks);
    return map;
  }

}