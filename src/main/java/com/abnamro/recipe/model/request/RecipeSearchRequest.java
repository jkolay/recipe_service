package com.abnamro.recipe.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class RecipeSearchRequest {
    private Boolean isVegetarian;
    private Integer servings;
    private String ingredientIn;
    private String ingredientEx;
    private String text;
}
