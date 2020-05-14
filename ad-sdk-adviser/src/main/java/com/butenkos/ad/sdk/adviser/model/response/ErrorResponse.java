package com.butenkos.ad.sdk.adviser.model.response;

import java.time.LocalDateTime;

/**
 * POJO representation of JSON error response
 */
@SuppressWarnings("unused")
public class ErrorResponse {
  private String timestamp;
  private String status;
  private String error;
  private String message;
  private String path;

  public ErrorResponse(String status, String error, String message, String path) {
    this.timestamp = LocalDateTime.now().toString();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}