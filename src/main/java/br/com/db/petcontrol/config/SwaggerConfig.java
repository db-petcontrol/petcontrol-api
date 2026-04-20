package br.com.db.petcontrol.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "PetControl API",
            version = "1.0",
            summary = "API para gerenciamento de animais."))
public class SwaggerConfig {}
