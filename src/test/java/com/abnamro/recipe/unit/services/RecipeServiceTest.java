package com.abnamro.recipe.unit.services;

import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.service.impl.IngredientServiceImpl;
import com.abnamro.recipe.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class
RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientServiceImpl ingredientService;

    @Mock
    private CommonConfigMapper commonConfigMapper;



    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    public void test_createRecipe_successfully() {
        CreateRecipeRequest request = new CreateRecipeRequest("Tea", "OTHER", 4, null, "instructions");
        RecipeDao recipeDao = mock(RecipeDao.class);
        RecipeResponse response = mock(RecipeResponse.class);
        when(commonConfigMapper.mapCreateRecipeRequestToRecipe(Mockito.any(CreateRecipeRequest.class))).thenReturn(recipeDao);
        when(recipeRepository.save(Mockito.any(RecipeDao.class))).thenReturn(recipeDao);
        when(commonConfigMapper.mapRecipeToRecipeResponse(recipeDao)).thenReturn(response);
        assertNotNull(recipeService.createRecipe(request));
    }

    @Test
    public void test_updateRecipe_successfully() {
        RecipeDao recipeDao = mock(RecipeDao.class);
        RecipeResponse response = mock(RecipeResponse.class);
        UpdateRecipeRequest request = new UpdateRecipeRequest(1, "Soup", "vegetarian", 4, null, "instructions");
        when(recipeRepository.save(Mockito.any(RecipeDao.class))).thenReturn(recipeDao);
        when(commonConfigMapper.mapUpdateRecipeRequestToRecipe(Mockito.any(UpdateRecipeRequest.class))).thenReturn(recipeDao);
        when(commonConfigMapper.mapRecipeToRecipeResponse(Mockito.any(RecipeDao.class))).thenReturn(response);
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.of(recipeDao));
        assertNotNull(recipeService.updateRecipe(request));
    }

    @Test
    public void test_updateRecipe_notFound() {
        UpdateRecipeRequest request = new UpdateRecipeRequest(1, "pasta", "OTHER", 4, null, "instructions");
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(request));
    }

    @Test
    public void test_deleteRecipe_successfully() {
        when(recipeRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(anyInt());
        recipeService.deleteRecipe(1);
    }



}