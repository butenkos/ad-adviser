package com.butenkos.ad.sdk.adviser.external.management.sender;

import java.util.UUID;

public interface CommandSender {
  void sendUpdateCacheMessage(UUID batchJobId);

  void sendUpdateCacheMessage(String command);

  void sendUpdateConfigMessage();
}
