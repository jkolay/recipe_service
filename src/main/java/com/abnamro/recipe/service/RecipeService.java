package com.abnamro.recipe.service;

import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;

import java.util.List;

/**
 * Service interface  for Recipe
 */
public interface RecipeService {

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest);

    public List<RecipeResponse> getRecipeList(int page, int size);

    public RecipeResponse getRecipeById(int id);

    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest);

    public void deleteRecipe(int id);

    public List<RecipeResponse> search(RecipeSearchRequest request);


}
