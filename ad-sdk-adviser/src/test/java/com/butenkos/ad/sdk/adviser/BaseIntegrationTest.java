package com.butenkos.ad.sdk.adviser;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * to avoid context re-creation for every test suite
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
}
