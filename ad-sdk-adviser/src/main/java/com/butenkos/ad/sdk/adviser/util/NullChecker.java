package com.butenkos.ad.sdk.adviser.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * i just don't like NPE
 */
public class NullChecker {
  public static void checkNotNull(Object... params) {
    if (null == params) {
      throw new IllegalArgumentException("Parameters must not be null");
    }
    if (Arrays.stream(params).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Parameters must not be null");
    }
  }
}
