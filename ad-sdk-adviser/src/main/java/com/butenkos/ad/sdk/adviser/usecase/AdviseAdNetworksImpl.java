package com.butenkos.ad.sdk.adviser.usecase;

import com.butenkos.ad.sdk.adviser.config.FallbackAdNetworkDataConfiguration;
import com.butenkos.ad.sdk.adviser.model.domain.AdNetwork;
import com.butenkos.ad.sdk.adviser.model.domain.AdType;
import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;
import com.butenkos.ad.sdk.adviser.model.response.AdNetworkAdviseResponse;
import com.butenkos.ad.sdk.adviser.service.converter.RequestConverter;
import com.butenkos.ad.sdk.adviser.service.converter.ResponseCreator;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import com.butenkos.ad.sdk.adviser.service.data.ConstraintsManager;
import com.butenkos.ad.sdk.adviser.service.validation.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("default")
public class AdviseAdNetworksImpl implements AdviseAdNetworks {
  private static final Logger LOG = LoggerFactory.getLogger(AdviseAdNetworksImpl.class);
  private final RequestValidator requestValidator;
  private final RequestConverter requestConverter;
  private final AdNetworkDataCache dataCache;
  private final ConstraintsManager constraintsManager;
  private final FallbackAdNetworkDataConfiguration fallbackAdNetworkDataConfiguration;
  private final ResponseCreator responseCreator;

  @Autowired
  public AdviseAdNetworksImpl(
      RequestValidator requestValidator,
      RequestConverter requestConverter,
      AdNetworkDataCache dataCache,
      ConstraintsManager constraintsManager,
      FallbackAdNetworkDataConfiguration fallbackAdNetworkDataConfiguration,
      ResponseCreator responseCreator
  ) {
    this.requestValidator = requestValidator;
    this.requestConverter = requestConverter;
    this.dataCache = dataCache;
    this.constraintsManager = constraintsManager;
    this.fallbackAdNetworkDataConfiguration = fallbackAdNetworkDataConfiguration;
    this.responseCreator = responseCreator;
  }

  @Override
  public AdNetworkAdviseResponse giveAdvice(AdNetworkAdviseRequest request) {
    LOG.debug("+giveAdvice(), request {}", request);
    requestValidator.validateRequest(request);
    final RequestData requestData = requestConverter.convertRequest(request);
    //getting sorted ad network data from cache by country and ad type
    final List<AdNetwork> bannerNetworks = dataCache.get(requestData.country, AdType.BANNER);
    final List<AdNetwork> interstitialAdNetworks = dataCache.get(requestData.country, AdType.INTERSTITIAL);
    final List<AdNetwork> videoNetworks = dataCache.get(requestData.country, AdType.VIDEO);
    //getting configured constrains based on request content
    final Set<String> adNetworkNamesToAvoid = constraintsManager.getAdNetworkNamesToAvoid(requestData);
    /*
     * filtering result by configured constraints. If the list of ad provider names for some type is empty,
     * it is replaced to a fallback list, configured for every geo region
     */
    final AdNetworkSearchingAndFilteringResult bannerResult = getAdNetworkSearchingAndFilteringResult(
        requestData,
        bannerNetworks,
        adNetworkNamesToAvoid,
        AdType.BANNER
    );

    final AdNetworkSearchingAndFilteringResult interstitialResult = getAdNetworkSearchingAndFilteringResult(
        requestData,
        interstitialAdNetworks,
        adNetworkNamesToAvoid,
        AdType.INTERSTITIAL
    );

    final AdNetworkSearchingAndFilteringResult videoResult = getAdNetworkSearchingAndFilteringResult(
        requestData,
        videoNetworks,
        adNetworkNamesToAvoid,
        AdType.VIDEO
    );

    final AdNetworkAdviseResponse response = responseCreator.createResponse(
        Arrays.asList(bannerResult, interstitialResult, videoResult),
        adNetworkNamesToAvoid
    );
    LOG.debug("-giveAdvice(), response {}", response);
    return response;
  }

  private AdNetworkSearchingAndFilteringResult getAdNetworkSearchingAndFilteringResult(
      RequestData requestData,
      List<AdNetwork> bannerNetworks,
      Set<String> adNetworkNamesToAvoid,
      AdType banner
  ) {
    final List<String> filteredBannerData = filterAdNetworksByConfiguredRestrictionsAndGetTheirNames(
        bannerNetworks,
        adNetworkNamesToAvoid
    );
    return getFilteredOrFallbackList(requestData, filteredBannerData, banner);
  }

  private AdNetworkSearchingAndFilteringResult getFilteredOrFallbackList(
      RequestData requestData,
      List<String> filteredNetworksList,
      AdType adType
  ) {
    return filteredNetworksList.isEmpty() ?
        new AdNetworkSearchingAndFilteringResult(
            fallbackAdNetworkDataConfiguration.getAdNetworkNames(requestData.country.getRegion(), adType),
            adType,
            true
        ) :
        new AdNetworkSearchingAndFilteringResult(filteredNetworksList, adType);
  }

  private List<String> filterAdNetworksByConfiguredRestrictionsAndGetTheirNames(
      List<AdNetwork> adNetworkNames,
      Set<String> adNetworkNamesToAvoid
  ) {
    return manageConstrains(adNetworkNames, adNetworkNamesToAvoid).stream()
        .map(AdNetwork::getName)
        .collect(Collectors.toList());
  }

  private List<AdNetwork> manageConstrains(List<AdNetwork> adNetworkNames, Set<String> adNetworkNamesToAvoid) {
    return constraintsManager.eliminateAdNetworkConflicts(
        adNetworkNames.stream()
            .filter(adNetwork -> !adNetworkNamesToAvoid.contains(adNetwork.getName()))
            .collect(Collectors.toList())
    );
  }

}
