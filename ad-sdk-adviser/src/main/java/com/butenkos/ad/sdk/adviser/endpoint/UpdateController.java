package com.butenkos.ad.sdk.adviser.endpoint;

import com.butenkos.ad.sdk.adviser.external.management.sender.CommandSender;
import com.butenkos.ad.sdk.adviser.service.data.AdNetworkDataCache;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Profile("default")
public class UpdateController {
  private static final Logger LOG = LoggerFactory.getLogger(UpdateController.class);
  private final AdNetworkDataCache dataCache;
  private final CommandSender commandSender;
  private final ContextRefresher refresher;

  @Autowired
  public UpdateController(AdNetworkDataCache dataCache, CommandSender commandSender, ContextRefresher refresher) {
    this.dataCache = dataCache;
    this.commandSender = commandSender;
    this.refresher = refresher;
  }

  @Operation(
      method = "POST",
      operationId = "updateConfig",
      description = "forces all the instances of the app to reload such configuration as constraints and fallback lists"
  )
  @PostMapping("/config/update")
  public ResponseEntity<String> updateConfig() {
    try {
      refresher.refresh();
      askOtherInstancesToUpdateConfig();
      return ResponseEntity.ok("configuration updated");
    } catch (FailedToNotifyOtherInstancesException e) {
      final String errorMsg = "Failed to notify other instances, configuration may be in inconsistent state";
      LOG.error(errorMsg, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg + ", " + e.getMessage());
    } catch (Exception e) {
      LOG.error("failed to update configuration", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @Operation(
      method = "POST",
      operationId = "updateConfig",
      description = "forces all the instances of the app to reload cache with the data of the most recent batch job"
  )
  @PostMapping("/cache/update")
  public ResponseEntity<String> updateCachedAdNetworkData() {
    try {
      dataCache.updateWithMostRecentData();
      askOtherInstancesToUpdateCacheWithMostRecentData();
      return ResponseEntity.ok("update successful, cache=" + dataCache);
    } catch (FailedToNotifyOtherInstancesException e) {
      final String errorMsg = "Failed to notify other instances, cache may be in inconsistent state";
      LOG.error(errorMsg, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg + ", " + e.getMessage());
    } catch (Exception e) {
      LOG.error("failed to update AdNetworkData with the most recent data", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @Operation(
      method = "POST",
      operationId = "updateConfig",
      description = "forces all the instances of the app to reload cache with the data of the particular batch job"
  )
  @PostMapping("/cache/update/{batchJobId}")
  public ResponseEntity<String> updateCachedAdNetworkDataByBatchJobId(@PathVariable UUID batchJobId) {
    try {
      dataCache.update(batchJobId.toString());
      askOtherInstancesToUpdateCache(batchJobId);
      return ResponseEntity.ok("update successful, cache=" + dataCache);
    } catch (FailedToNotifyOtherInstancesException e) {
      final String errorMsg = "Failed to notify other instances, cache may be in inconsistent state";
      LOG.error(errorMsg, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg + ", " + e.getMessage());
    } catch (Exception e) {
      LOG.error("failed to update AdNetworkData cache, batchJobId {}", batchJobId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  private void askOtherInstancesToUpdateConfig() {
    try {
      commandSender.sendUpdateConfigMessage();
    } catch (Exception e) {
      throw new FailedToNotifyOtherInstancesException(e);
    }
  }

  private void askOtherInstancesToUpdateCache(UUID batchJobId) {
    try {
      commandSender.sendUpdateCacheMessage(batchJobId);
    } catch (Exception e) {
      throw new FailedToNotifyOtherInstancesException(e);
    }
  }

  private void askOtherInstancesToUpdateCacheWithMostRecentData() {
    try {
      commandSender.sendUpdateCacheMessage("most_recent");
    } catch (Exception e) {
      throw new FailedToNotifyOtherInstancesException(e);
    }
  }

  private static class FailedToNotifyOtherInstancesException extends RuntimeException {
    public FailedToNotifyOtherInstancesException(Exception e) {
      super(e);
    }
  }
}
