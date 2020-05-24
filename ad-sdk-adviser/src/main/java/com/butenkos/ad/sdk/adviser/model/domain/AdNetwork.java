package com.butenkos.ad.sdk.adviser.model.domain;

import java.util.Objects;

/**
 * Model representing rated Ad Network SDK provider.
 */
public class AdNetwork {
  private final String name;
  private final byte score;

  /**
   *
   * @param name - name of the Ad Network SDK provider
   * @param score - pre-calculated value representing the performance rate (the bigger the better, min=0, max=99)
   */
  public AdNetwork(String name, byte score) {
    this.name = name;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public byte getScore() {
    return score;
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
