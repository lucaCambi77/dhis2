package it.cambi.dhis2.swagger;

import it.cambi.dhis2.controller.v1.Dhis2Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(Dhis2Controller.class.getPackage().getName()))
        .paths(PathSelectors.any())
        .build()
        .securityContexts(Collections.singletonList(securityContext()))
        .securitySchemes(Collections.singletonList(basicAuthScheme()));
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(Collections.singletonList(basicAuthReference()))
        .operationSelector(
            operationContext -> operationContext.requestMappingPattern().contains("/dhis2/v1/api/"))
        .build();
  }

  private SecurityScheme basicAuthScheme() {
    return new BasicAuth("basicAuth");
  }

  private SecurityReference basicAuthReference() {
    return new SecurityReference("basicAuth", new AuthorizationScope[0]);
  }
}
