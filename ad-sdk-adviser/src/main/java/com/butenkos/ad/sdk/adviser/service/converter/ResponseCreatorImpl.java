package com.butenkos.ad.sdk.adviser.service.converter;

import com.butenkos.ad.sdk.adviser.model.response.AdNetworkAdviseResponse;
import com.butenkos.ad.sdk.adviser.usecase.AdNetworkSearchingAndFilteringResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResponseCreatorImpl implements ResponseCreator {
  @Override
  public AdNetworkAdviseResponse createResponse(
      List<AdNetworkSearchingAndFilteringResult> results,
      Set<String> adNetworkNamesToAvoid
  ) {
    final AdNetworkAdviseResponse response = createResponseEntityFilledWithAdNetworksData(results);
    final String warningMessage = createWarningMessage(results, adNetworkNamesToAvoid);
    if (!warningMessage.isEmpty()) {
      response.setWarning(warningMessage);
    }
    return response;
  }

  private AdNetworkAdviseResponse createResponseEntityFilledWithAdNetworksData(List<AdNetworkSearchingAndFilteringResult> results) {
    final AdNetworkAdviseResponse response = new AdNetworkAdviseResponse();
    results.forEach(result -> {
          switch (result.getAdType()) {
            case BANNER:
              response.setBannerAdNetworks(result.getAdNetworkNames());
              break;
            case INTERSTITIAL:
              response.setInterstitialAdNetworks(result.getAdNetworkNames());
              break;
            case VIDEO:
              response.setVideoAdNetworks(result.getAdNetworkNames());
              break;
            default:
              break;
          }
        }
    );
    return response;
  }

  //conflicting networks are not getting into this message
  private String createWarningMessage(
      List<AdNetworkSearchingAndFilteringResult> results,
      Set<String> adNetworkNamesToAvoid) {

    final String fallback = results.stream()
        .filter(AdNetworkSearchingAndFilteringResult::isFallbackUsed)
        .map(result -> result.getAdType().toString())
        .collect(Collectors.joining(", "));
    final String networksToAvoid = String.join(", ", adNetworkNamesToAvoid);
    final String fallbackWarning = fallback.isEmpty() ?
        "" :
        "fallback list was used for the following ad types: " + fallback;
    final String networksToAvoidWarning = networksToAvoid.isEmpty() ? "" : "Not allowed networks: " + networksToAvoid;
    return Stream.of(fallbackWarning, networksToAvoidWarning)
        .filter(msg -> !msg.isEmpty())
        .collect(Collectors.joining(" | "));
  }


}
