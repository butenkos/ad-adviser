package com.butenkos.ad.sdk.adviser.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI appApi() {
    return new OpenAPI().info(
        new Info()
            .title("Ad Network SDK adviser")
            .description("test assignment")
            .version("SNAPSHOT")
            .termsOfService(null)
            .license(null)
            .contact(new Contact().name("Sergey Butenko").email("s.butenko@rambler.ru"))
    );
  }
}
