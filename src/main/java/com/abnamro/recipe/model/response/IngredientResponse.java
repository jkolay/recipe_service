package com.abnamro.recipe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ingredient response model
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IngredientResponse {
    private Integer id;
    private String ingredient;
}
