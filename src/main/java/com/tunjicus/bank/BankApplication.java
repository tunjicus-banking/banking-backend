package com.tunjicus.bank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "Banking Application",
                        version = "0.0",
                        description = "A toy banking application with simulated time"),
        security = {
            @SecurityRequirement(name = "JWT Auth"),
        })
@SecurityScheme(
        name = "JWT Auth",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization",
        bearerFormat = "Bearer <token>",
        description = "Login as a valid user and copy the token. Put Bearer {token}, (no braces), in box.",
        scheme = "Bearer",
        type = SecuritySchemeType.APIKEY)
@SpringBootApplication
@EnableScheduling
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
