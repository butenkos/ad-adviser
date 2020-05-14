package com.butenkos.ad.sdk.info.updater.endpoint;

import com.butenkos.ad.sdk.info.updater.messaging.Sender;
import com.butenkos.ad.sdk.info.updater.messaging.Sender.FailedToSendUpdateCacheMessage;
import com.butenkos.ad.sdk.info.updater.model.Country;
import com.butenkos.ad.sdk.info.updater.service.TestDataCreator;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatchJobController {
  private static final Logger LOG = LoggerFactory.getLogger(BatchJobController.class);
  private final TestDataCreator dataCreator;
  private final Sender sender;

  @Autowired
  public BatchJobController(TestDataCreator dataCreator, Sender sender) {
    this.dataCreator = dataCreator;
    this.sender = sender;
  }

  /*
   * Simulating batch job processing and evaluating ad networks
   * In real life here should be a long-running async task.
   */
  @Operation(
      method = "POST",
      operationId = "performBatchJob",
      description = "generates random test data and notifies all the instances of ad-sdk-adviser to reload the cache"
  )
  @PostMapping("/batchjob/perform")
  public List<String> performBatchJob() {
    List<String> batchJobIds = null;
    try {
      LOG.info("start batch job...");
      batchJobIds = dataCreator.generateTestDataInDb(1, Country.values());
      LOG.info("batchJob completed successfully, id={}", batchJobIds);
      sender.sendUpdateCacheMessage("update_cache");
      return batchJobIds;
    } catch (FailedToSendUpdateCacheMessage e) {
      LOG.error(
          "batchJob completed successfully, but failed to send 'update cache' message. batchJobId={}",
          batchJobIds
      );
      return batchJobIds;
    } catch (Exception e) {
      LOG.error("failed to execute batch job", e);
      throw new FailedToExecuteBatchJobException(e);
    }
  }

  private static class FailedToExecuteBatchJobException extends RuntimeException {
    FailedToExecuteBatchJobException(Exception e) {
      super("failed to execute batch job", e);
    }
  }
}
