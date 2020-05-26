package com.butenkos.ad.sdk.adviser.endpoint;

import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("default")
public class MonitoringController {
  private final AdNetworkDataCache dataCache;

  @Autowired
  public MonitoringController(AdNetworkDataCache dataCache) {
    this.dataCache = dataCache;
  }

  @Operation(
      method = "GET",
      operationId = "getCacheState",
      description = "returns statistics of ad networks by country and typ stored in the cache at the moment"
  )
  @GetMapping("/cache/stats")
  public String getCacheState() {
    return dataCache.getCacheStatistics();
  }

  @Operation(method = "GET", operationId = "getCacheContents", description = "prints out cache contents")
  @GetMapping("/cache")
  public String getCacheContents() {
    return dataCache.printContents();
  }
}
