package com.abnamro.recipe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application class for Recipe service
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Recipe API", version = "2.0", description = "Recipe Information"))
public class RecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeApplication.class, args);
	}

}
