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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeRequest {
    @NotNull(message = RecipeValidationMessageConfig.ID_NOT_NULL)
    @Positive(message = RecipeValidationMessageConfig.ID_NOT_NULL)
    @Schema(description = "Id of the attribute", example = "1")
    private Integer id;

    @NotBlank(message = RecipeValidationMessageConfig.RECIPE_NAME_NOT_NULL)
    @Size(max = ValidationConstant.MAX_LENGTH_NAME, message = RecipeValidationMessageConfig.RECIPE_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConstant.PATTERN_NAME, message = RecipeValidationMessageConfig.RECIPE_PATTERN_NOT_VALID)
    @Schema(description = "The name of the ingredient", example = "Potato")
    private String name;

    @EnumValidator(enumClass = RecipeType.class, message = RecipeValidationMessageConfig.INVALID_RECIPE_TYPE)
    @Schema(description = "The type of the recipe", example = "VEGETARIAN")
    private String type;

    @NotNull(message = RecipeValidationMessageConfig.INVALID_NUMBER_OF_SERVING)
    @Positive(message = RecipeValidationMessageConfig.INVALID_NUMBER_OF_SERVING)
    @Schema(description = "The number of servings", example = "7")
    private int numberOfServings;

    @Schema(description = "The new ids of the ingredients needed for the update", example = "[3,4]")
    private List<Integer> ingredientIds;

    @NotBlank(message = RecipeValidationMessageConfig.INSTRUCTION_IS_NOT_VALID)
    @Size(max = ValidationConstant.MAX_LENGTH_DEFAULT, message = RecipeValidationMessageConfig.INSTRUCTION_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConstant.PATTERN_FREE_TEXT, message = RecipeValidationMessageConfig.INSTRUCTION_PATTERN_NOT_VALID)
    @Schema(description = "The instructions to update the recipe", example = "Cut,fry,eat")
    private String instructions;

}
