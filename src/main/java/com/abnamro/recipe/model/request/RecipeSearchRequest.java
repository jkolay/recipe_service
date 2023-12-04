package com.abnamro.recipe.model.request;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.config.ValidationConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class RecipeSearchRequest {
    @Schema(description = "The recipe should be found on based on whether the dish is vegetarian or not.Value should be either true or false")
    private Boolean isVegetarian;
    @Positive(message = RecipeValidationMessageConfig.INVALID_NUMBER_OF_SERVING)
    @Schema(description = "The recipe should be found on based on servings", example = "5")
    private Integer servings;
    @Schema(description = "The recipe should be found on based on ingredient which must be included in the recipe", example = "tea")
    private String ingredientIn;
    @Schema(description = "The recipe should be found on based on ingredient which must be excluded in the recipe", example = "sugar")
    private String ingredientEx;
    @Size(max = ValidationConstant.MAX_LENGTH_DEFAULT, message = RecipeValidationMessageConfig.INSTRUCTION_SIZE_NOT_VALID)
    @Schema(description = "The text by which recipe can be searched. This field would search on the based on recipe instruction", example = "oven")
    private String text;
}
