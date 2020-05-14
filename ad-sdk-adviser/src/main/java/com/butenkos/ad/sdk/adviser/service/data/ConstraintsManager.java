package com.butenkos.ad.sdk.adviser.service.data;

import com.butenkos.ad.sdk.adviser.model.domain.AdNetwork;
import com.butenkos.ad.sdk.adviser.model.request.RequestData;

import java.util.List;
import java.util.Set;

/**
 * Component for managing configured constrains. It uses preloaded configuration data
 * to filter out.
 *
 * @see com.butenkos.ad.sdk.adviser.config.ConstraintsConfig
 * @see com.butenkos.ad.sdk.adviser.config.ConstraintsConfigImpl
 * @see ConstraintsManagerImpl
 */
public interface ConstraintsManager {
  Set<String> getAdNetworkNamesToAvoid(RequestData requestData);

  List<AdNetwork> eliminateAdNetworkConflicts(List<AdNetwork> adNetworkList);
}
