package com.abnamro.recipe.model.request;

import com.abnamro.recipe.config.MessageConfig;
import com.abnamro.recipe.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientRequest {
    @NotBlank(message = MessageConfig.INGREDIENT_NAME_NOT_NULL)
    @Size(max = ValidationConfig.MAX_LENGTH_NAME, message = MessageConfig.INGREDIENT_SIZE_NOT_VALID)
    @Pattern(regexp = ValidationConfig.PATTERN_NAME, message = MessageConfig.INGREDIENT_PATTERN_NOT_VALID)
    @Schema(description = "The name of the ingredient", example = "Potato")
    private String name;


}
