package com.butenkos.ad.sdk.adviser.helper;

@SuppressWarnings("unused")
public class DbEntry {
  String batchJobId;
  String adNetwork;
  String country;
  String adType;
  int score;

  public String getBatchJobId() {
    return batchJobId;
  }

  public void setBatchJobId(String batchJobId) {
    this.batchJobId = batchJobId;
  }

  public String getAdNetwork() {
    return adNetwork;
  }

  public void setAdNetwork(String adNetwork) {
    this.adNetwork = adNetwork;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAdType() {
    return adType;
  }

  public void setAdType(String adType) {
    this.adType = adType;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
