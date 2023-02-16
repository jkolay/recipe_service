package com.abnamro.recipe.service;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.Ingredient;
import com.abnamro.recipe.model.persistence.Recipe;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.SearchCriteriaRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.model.search.SearchCriteria;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.search.RecipeSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    private final CommonConfigMapper commonConfigMapper;


    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         IngredientService ingredientService, CommonConfigMapper commonConfigMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.commonConfigMapper = commonConfigMapper;
    }

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Recipe existingRecipe= recipeRepository.findByNameEqualsIgnoreCase(createRecipeRequest.getName());
        if(existingRecipe!=null){
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.RECIPE_ALREADY_EXISTS+ existingRecipe.getId());
        }
        Set<Ingredient> ingredients=ingredientService.getIngredientsByIds(createRecipeRequest.getIngredientIds());
        Recipe recipe = commonConfigMapper.mapCreateRecipeRequestToRecipe(createRecipeRequest);
        recipe.setRecipeIngredients(ingredients);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        Recipe createdRecipe = recipeRepository.save(recipe);

        return commonConfigMapper.mapRecipeToRecipeResponse(createdRecipe);
    }

    public List<RecipeResponse> getRecipeList(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findAll(pageRequest).getContent());
    }

    public RecipeResponse getRecipeById(int id) {
        Recipe recipe= recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));
        return commonConfigMapper.mapRecipeToRecipeResponse(recipe);
    }

    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        Recipe existingRecipe = recipeRepository.findById(updateRecipeRequest.getId())
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));

        Set<Ingredient> ingredients =ingredientService.getIngredientsByIds(updateRecipeRequest.getIngredientIds());

        Recipe recipe=commonConfigMapper.mapUpdateRecipeRequestToRecipe(updateRecipeRequest);
        recipe.setRecipeIngredients(ingredients);
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setCreatedAt(existingRecipe.getCreatedAt());

        recipeRepository.save(recipe);
        return commonConfigMapper.mapRecipeToRecipeResponse(recipe);
    }

    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
        }

        recipeRepository.deleteById(id);
    }

    public List<RecipeResponse> findBySearchCriteria(RecipeSearchRequest recipeSearchRequest,int page,int size){
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        Specification<Recipe> recipeSpecification = createRecipeSpecificationForSearch(recipeSearchRequest);
        Page<Recipe> filteredRecipes = recipeRepository.findAll(recipeSpecification, pageRequest);
        return commonConfigMapper.mapRecipesToRecipeResponses(filteredRecipes.getContent());
    }

    private Specification<Recipe> createRecipeSpecificationForSearch(RecipeSearchRequest recipeSearchRequest) {
        List<SearchCriteriaRequest> criteriaRequestList = recipeSearchRequest.getSearchCriteriaRequests();
        if(criteriaRequestList.isEmpty()){
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.FILTER_IS_NOT_PROVIDED);
        }
        List<SearchCriteria> criteriaList=commonConfigMapper.mapSearchCriteriaRequestsToSearchCriterias(criteriaRequestList);
        RecipeSpecificationBuilder recipeSpecificationBuilder = new RecipeSpecificationBuilder();
        if(criteriaList != null){
            criteriaList.forEach(criteria->
            {
                criteria.setDataOption(recipeSearchRequest.getDataOption());
                recipeSpecificationBuilder.with(criteria);
            });
        }
        return recipeSpecificationBuilder.build().orElseThrow(()-> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));
    }
}
