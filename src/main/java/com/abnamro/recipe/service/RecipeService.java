package com.abnamro.recipe.service;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface  for Recipe
 */
public interface RecipeService {

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) ;

    public List<RecipeResponse> getRecipeList(int page, int size) ;

    public RecipeResponse getRecipeById(int id);

    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) ;

    public void deleteRecipe(int id);

    public List<RecipeResponse> getRecipeListByRecipeType(String recipeTpe);
    public List<RecipeResponse> getRecipesByServing(Integer numberOfServing);}
