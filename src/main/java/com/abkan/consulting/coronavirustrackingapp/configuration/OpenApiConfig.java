package com.abkan.consulting.coronavirustrackingapp.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(new Info()
            .title("coronavirus-tracking-app")
            .description("Collects the data related to ongoing coronavirus pandemic.")
            .license(new License()
                .name("AbKan AB")
                .url("https://github.com/abhishekdiwakar/coronavirus-tracker-application")
            ));
  }

  @Bean
  public GroupedOpenApi userOpenApi() {
    String[] paths = { "/v1/coronavirusData/**" };
    String[] packagedToMatch = { "com.abkan.consulting.coronavirustrackingapp" };
    return GroupedOpenApi
        .builder()
        .setGroup("coronavirus-data")
        .pathsToMatch(paths)
        .packagesToScan(packagedToMatch)
        .build();
  }
}
