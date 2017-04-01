package com.aurea;

import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(Predicates.not(PathSelectors.regex("/error")))
        .apis(RequestHandlerSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private int unusedForTesting;

  private ApiInfo apiInfo() {

    return new ApiInfo(
        "Dead Code DeadCodeDetection",
        "This API crawls given github repositories and detects dead code among them.",
        "v1.0",
        "",
        new Contact("Emin Sadiyev", "", ""),
        "",
        "");
  }
}
