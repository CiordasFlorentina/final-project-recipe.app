package com.example.recipe.app.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@Configuration
public class SpringFoxConfig {
    //https://springdoc.org/migrating-from-springfox.html SpringFox 3.0.0 Deprecated. Using actuator gives error for SpringFox
    @Bean
    public GroupedOpenApi publicApi()  {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("http:/localhost:8080/**")
                .build();
    }
//    @Bean
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.recipe.app"))
//                .build();
//    }
}