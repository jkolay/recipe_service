package com.abnamro.recipe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Recipe response model
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeResponse {
    private Integer id;
    private String name;
    private Set<IngredientResponse> recipeIngredients;
    private String instructions;
    private String type;
    private int numberOfServings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
