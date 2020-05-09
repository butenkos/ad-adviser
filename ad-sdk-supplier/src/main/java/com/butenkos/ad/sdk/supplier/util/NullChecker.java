package com.butenkos.ad.sdk.supplier.util;

import java.util.Arrays;
import java.util.Objects;

public class NullChecker {
  public static void checkNotNull(Object... params) {
    Arrays.stream(params)
        .filter(Objects::isNull)
        .findFirst()
        .ifPresent(
            fail -> {
              throw new IllegalArgumentException("Parameters must not be null");
            });
  }
}
