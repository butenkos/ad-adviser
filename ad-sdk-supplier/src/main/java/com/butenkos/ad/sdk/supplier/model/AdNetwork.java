package com.butenkos.ad.sdk.supplier.model;

import java.util.Objects;

public class AdNetwork {
  private final String name;
  private final int score;

  public AdNetwork(String name, int score) {
    this.name = name;
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AdNetwork adNetwork = (AdNetwork) o;
    return score == adNetwork.score && Objects.equals(name, adNetwork.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, score);
  }

  @Override
  public String toString() {
    return "AdNetwork{" + "name='" + name + '\'' + ", score=" + score + '}';
  }
}
