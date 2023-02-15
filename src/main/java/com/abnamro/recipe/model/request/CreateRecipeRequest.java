package com.abnamro.recipe.model.request;

import com.abnamro.recipe.config.MessageConfig;
import com.abnamro.recipe.config.ValidationConfig;
import com.abnamro.recipe.model.enums.RecipeType;
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
    @NotBlank(message = MessageConfig.RECIPE_NAME_NOT_NULL)
    @Size(max = ValidationConfig.MAX_LENGTH_NAME, message = MessageConfig.RECIPE_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConfig.PATTERN_NAME, message = MessageConfig.RECIPE_PATTERN_NOT_VALID)
    @Schema(description = "The name of the recipe", example = "Pasta")
    private String name;

    @Schema(description = "The type of the recipe", example = "VEGETARIAN")
    @EnumValidator(enumClass = RecipeType.class, message = MessageConfig.INVALID_RECIPE_TYPE)
    private String type;

    @NotNull(message = MessageConfig.INVALID_NUMBER_OF_SERVING)
    @Positive(message = MessageConfig.INVALID_NUMBER_OF_SERVING)
    @Schema(description = "The number of servings per recipe", example = "4")
    private int numberOfServings;

    @Schema(description = "The ids of the ingredients needed to make the recipe", example = "[1,2]")
    private List<Integer> ingredientIds;

    @NotBlank(message = MessageConfig.INSTRUCTION_IS_NOT_VALID)
    @Size(max = ValidationConfig.MAX_LENGTH_DEFAULT, message = MessageConfig.INSTRUCTION_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConfig.PATTERN_FREE_TEXT, message = MessageConfig.INSTRUCTION_PATTERN_NOT_VALID)
    @Schema(description = "The instructions to create the recipe", example = "Chop the tomato, stir and fry, boil and serve")
    private String instructions;

}
