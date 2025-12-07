package com.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "OAuth Application Build by Md Irfan",
                description = "Generic OAuth app that can be used with any application",
                contact = @Contact(
                        name = "Md Irfan",
                        url = "localhost:8083",
                        email = "mirfan916152@gmail.com"
                ),
                version = "1.0.0",
                summary = "This app is very important if you don't know OAuth app from scratch"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", // Authorization: bearer dkdfdkl
        bearerFormat = "JWT"

)
public class SwaggerConfig {


}
