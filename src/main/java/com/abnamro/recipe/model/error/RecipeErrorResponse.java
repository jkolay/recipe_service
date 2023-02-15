package com.abnamro.recipe.model.error;

import com.abnamro.recipe.model.error.RecipeErrorModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeErrorResponse {
    private List<RecipeErrorModel> errors;
}
