package com.abnamro.recipe.unit.controllers;

import com.abnamro.recipe.controllers.RecipeController;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {
    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    public void test_createRecipe_successfully() {
        CreateRecipeRequest request = new CreateRecipeRequest("noodles", "OTHER", 4, null, "instructions");
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(new RecipeResponse());
        assertNotNull(recipeController.createRecipe(request));
    }

    @Test
    public void test_listRecipe_successfully() {
        when(recipeService.getRecipeById(anyInt())).thenReturn(new RecipeResponse());
        assertNotNull(recipeController.getRecipe(5));

    }

    @Test
    public void test_listRecipes_successfully() {
        RecipeResponse recipe = new RecipeResponse();
        recipe.setName("name1");
        recipe.setType("OTHER");
        recipe.setInstructions("ins");
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        recipeResponses.add(recipe);
        when(recipeService.getRecipeList(anyInt(), anyInt())).thenReturn(recipeResponses);
        List<RecipeResponse> recipeList = recipeController.getRecipeList(anyInt(), anyInt());
        assertNotNull(recipeList);
    }

    @Test
    public void test_updateRecipe_successfully() {
        RecipeResponse recipe = new RecipeResponse();
        recipe.setName("name1");
        recipe.setType("OTHER");
        recipe.setInstructions("ins");
        when(recipeService.updateRecipe(any())).thenReturn(recipe);
        UpdateRecipeRequest request = new UpdateRecipeRequest(1, "noodles", "OTHER", 4, null, "instructions");
        recipeController.updateRecipe(request);
    }

    @Test
    public void test_deleteRecipe_successfully() {
        doNothing().when(recipeService).deleteRecipe(anyInt());
        recipeController.deleteRecipe(5);
    }
}