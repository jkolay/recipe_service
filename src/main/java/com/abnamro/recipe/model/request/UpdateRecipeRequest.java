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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeRequest {
    @NotNull(message = MessageConfig.ID_NOT_NULL)
    @Positive(message = MessageConfig.ID_NOT_NULL)
    @Schema(description = "Id of the attribute", example = "1")
    private Integer id;

    @NotBlank(message = MessageConfig.RECIPE_NAME_NOT_NULL)
    @Size(max = ValidationConfig.MAX_LENGTH_NAME, message = MessageConfig.RECIPE_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConfig.PATTERN_NAME, message = MessageConfig.RECIPE_PATTERN_NOT_VALID)
    @Schema(description = "The name of the ingredient", example = "Potato")
    private String name;

    @EnumValidator(enumClass = RecipeType.class, message = MessageConfig.INVALID_RECIPE_TYPE)
    @Schema(description = "The type of the recipe", example = "VEGETARIAN")
    private String type;

    @NotNull(message = MessageConfig.INVALID_NUMBER_OF_SERVING)
    @Positive(message = MessageConfig.INVALID_NUMBER_OF_SERVING)
    @Schema(description = "The number of servings", example = "7")
    private int numberOfServings;

    @Schema(description = "The new ids of the ingredients needed for the update", example = "[3,4]")
    private List<Integer> ingredientIds;

    @NotBlank(message = MessageConfig.INSTRUCTION_IS_NOT_VALID)
    @Size(max = ValidationConfig.MAX_LENGTH_DEFAULT, message = MessageConfig.INSTRUCTION_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConfig.PATTERN_FREE_TEXT, message = MessageConfig.INSTRUCTION_PATTERN_NOT_VALID)
    @Schema(description = "The instructions to update the recipe", example = "Cut,fry,eat")
    private String instructions;

}
