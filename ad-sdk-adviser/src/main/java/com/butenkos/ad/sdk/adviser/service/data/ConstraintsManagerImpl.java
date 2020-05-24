package com.butenkos.ad.sdk.adviser.service.data;

import com.butenkos.ad.sdk.adviser.config.ConstraintsConfig;
import com.butenkos.ad.sdk.adviser.model.domain.AdNetwork;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * For the sake of simplicity, instead of using some kind of JMS implementations or some bus,
 * it was decided to use Spring feature of automatic reloading of the configuration defined in
 * properties or yaml files. Due to limitations of this approach in storing structured data in
 * the properties/yaml file, implementation of {@code ConstraintsConfigImpl} and
 * {@code ConstraintsManagerImpl} is pretty awkward.
 *
 * @see com.butenkos.ad.sdk.adviser.config.ConstraintsConfig
 * @see com.butenkos.ad.sdk.adviser.config.ConstraintsConfigImpl
 * @see ConstraintsManager
 */
@Service
public class ConstraintsManagerImpl implements ConstraintsManager {
  private static final Logger LOG = LoggerFactory.getLogger(ConstraintsManagerImpl.class);
  private final ConstraintsConfig config;

  @Autowired
  public ConstraintsManagerImpl(ConstraintsConfig config) {
    this.config = config;
  }

  @Override
  public Set<String> getAdNetworkNamesToAvoid(RequestData requestData) {
    LOG.info("getAdNetworkNamesToAvoid(), request data {}", requestData);
    final Set<String> adNetworks = new HashSet<>();
    adNetworks.addAll(config.getCountryRestriction().getOrDefault(requestData.country.getCode(), emptySet()));
    adNetworks.addAll(getNetworksRestrictedByAge(requestData));
    adNetworks.addAll(config.getOsRestriction().getOrDefault(requestData.operatingSystem, emptySet()));
    adNetworks.addAll(
        config.getOsVersionRestriction()
            .getOrDefault(requestData.operatingSystem + requestData.operatingSystemVersion, emptySet())
    );
    adNetworks.addAll(config.getApplicationRestriction().getOrDefault(requestData.applicationName, emptySet()));
    LOG.info("getAdNetworkNamesToAvoid(), networks to avoid {}", adNetworks);
    return adNetworks;
  }

  private Set<String> getNetworksRestrictedByAge(RequestData requestData) {
    final Set<String> networks = new HashSet<>();
    config.getAgeRestriction().keySet().stream()
        .map(Integer::parseInt)
        .filter(key -> requestData.ageRestriction < key)
        .collect(Collectors.toList())
        .stream()
        .map(key -> config.getAgeRestriction().getOrDefault(key.toString(), emptySet()))
        .forEach(networks::addAll);
    return networks;
  }

  /**
   * It is possible to configure one or more "conflicting" AdNetworks for every AdNetworks in the properties
   * file this way: network.restriction.conflict={AdMob: 'AdMobSDK', Chartboost: 'Vungle,AppBrain'}
   * For simplicity, current implementation of resolving such conflicts doesn't take into account reflectiveness
   * (if Chartboost conflicts with Vungle, then Vungle conflicts with Chartboost),
   * it also neither resolves nor evaluates all the dependency graph (Vungle and AppBrain, while in conflict
   * with Chartboost, may also conflict with some other AdNetwork and so on). Instead it just takes sets of
   * conflicting AdNetworks for those AdNetworks which were explicitly configured in the properties file, merges them
   * together and filter out all but the highest scored conflicting AdNetworks.
   *
   * @param adNetworkList list that may contain two or more conflicting (mutually exclusive) AdNetworks
   * @return list with all but the highest scored conflicting AdNetworks filtered out
   */
  @Override
  public List<AdNetwork> eliminateAdNetworkConflicts(List<AdNetwork> adNetworkList) {
    if (config.getConflictingAdNetworks().isEmpty()) {
      return adNetworkList;
    }
    final Set<String> configuredRestrictions = getAdNetworksForWhichConflictsDefined(adNetworkList);
    if (configuredRestrictions.isEmpty()) {
      return adNetworkList;
    } else {
      return manageConflictingNetworks(adNetworkList, configuredRestrictions);
    }
  }

  private List<AdNetwork> manageConflictingNetworks(
      List<AdNetwork> adNetworkList,
      Set<String> configuredRestrictions
  ) {
    final Set<String> allConflictingAdNetworks = findAllConflictingAdNetworks(configuredRestrictions);
    if (adNetworkList.isEmpty()) {
      return adNetworkList;
    }
    return filterOutAllButOneHighestScoredNetwork(allConflictingAdNetworks, adNetworkList);
  }

  //Lists always come with AdNetworks sorted by the score in descending order
  //so we just keep the first found object and discard all others
  private List<AdNetwork> filterOutAllButOneHighestScoredNetwork(
      Set<String> allConflictingAdNetworks,
      List<AdNetwork> adNetworkList
  ) {
    LOG.debug("Conflicting networks: {}", allConflictingAdNetworks);
    final AdNetwork highestRatedNetwork = adNetworkList.stream()
        .filter(adNetwork -> allConflictingAdNetworks.contains(adNetwork.getName()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Set of conflicting networks must never be empty at this point")
        );
    LOG.debug("Highest scored network among conflicting: {}", highestRatedNetwork);
    allConflictingAdNetworks.remove(highestRatedNetwork.getName());
    return adNetworkList.stream()
        .filter(
            adNetwork -> !allConflictingAdNetworks.contains(adNetwork.getName()))
        .collect(Collectors.toList());
  }

  private Set<String> findAllConflictingAdNetworks(Set<String> configuredRestrictions) {
    final Set<String> resultSet = new HashSet<>(configuredRestrictions);
    configuredRestrictions.stream().map(adName -> config.getConflictingAdNetworks().get(adName)).forEach(
        resultSet::addAll
    );
    return resultSet;
  }

  private Set<String> getAdNetworksForWhichConflictsDefined(List<AdNetwork> adNetworkList) {
    final Set<String> configuredRestrictions = new HashSet<>(this.config.getConflictingAdNetworks().keySet());
    configuredRestrictions.retainAll(adNetworkList.stream().map(AdNetwork::getName).collect(Collectors.toSet()));
    return configuredRestrictions;
  }

  @Override
  public String toString() {
    return "ConstraintsDataHolderImpl{" +
        "config=" + config +
        '}';
  }
}
