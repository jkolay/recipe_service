package com.abnamro.recipe.model.request;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.config.ValidationConstant;
import com.abnamro.recipe.model.constant.RecipeType;
import com.abnamro.recipe.validator.EnumValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeRequest {
    @NotBlank(message = RecipeValidationMessageConfig.RECIPE_NAME_NOT_NULL)
    @Size(max = ValidationConstant.MAX_LENGTH_NAME, message = RecipeValidationMessageConfig.RECIPE_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConstant.PATTERN_NAME, message = RecipeValidationMessageConfig.RECIPE_PATTERN_NOT_VALID)
    @Schema(description = "The name of the recipe", example = "Tomato Soup")
    private String name;

    @Schema(description = "The type of the recipe", example = "VEGETARIAN")
    @EnumValidator(enumClass = RecipeType.class, message = RecipeValidationMessageConfig.INVALID_RECIPE_TYPE)
    private String type;

    @NotNull(message = RecipeValidationMessageConfig.INVALID_NUMBER_OF_SERVING)
    @Positive(message = RecipeValidationMessageConfig.INVALID_NUMBER_OF_SERVING)
    @Schema(description = "The number of servings per recipe", example = "5")
    private int numberOfServings;

    @Schema(description = "The ids of the ingredients needed to make the recipe", example = "[1,2]")
    @NotNull(message = RecipeValidationMessageConfig.INGREDIENT_LIST_CANNOT_BE_EMPTY)
    private List<Integer> ingredientIds;

    @NotBlank(message = RecipeValidationMessageConfig.INSTRUCTION_IS_NOT_VALID)
    @Size(max = ValidationConstant.MAX_LENGTH_DEFAULT, message = RecipeValidationMessageConfig.INSTRUCTION_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConstant.PATTERN_FREE_TEXT, message = RecipeValidationMessageConfig.INSTRUCTION_PATTERN_NOT_VALID)
    @Schema(description = "The instructions to create the recipe", example = "Chop the tomato, stir and fry, boil and serve")
    private String instructions;

}
