package com.rypsk.weeklymenucreator.api.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Weekly Menu Creator API",
                description = "REST API for Weekly Menu Creator application",
                termsOfService = "www.weeklymenucreator.com/termsOfService",
                version = "1.0.0",
                contact = @Contact(
                        name = "rypsk",
                        url = "www.weeklymenucreator.com/contact",
                        email = "rypsk@hotmail.com"
                ),
                license = @License(
                        name = "This is free and unencumbered software released into the public domain.",
                        url = "www.weeklymenucreator.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "Dev Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod Server",
                        url = "https://weeklumenucreator.com:8080"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token for Weekly Menu Creator API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
