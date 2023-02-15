package com.abnamro.recipe.service;

import com.abnamro.recipe.config.MessageConfig;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.Ingredient;
import com.abnamro.recipe.model.persistence.Recipe;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public Integer createRecipe(CreateRecipeRequest createRecipeRequest) {
        Set<Ingredient> ingredients = ingredientService.getIngredientsByIds(createRecipeRequest.getIngredientIds());

        Recipe recipe = commonConfigMapper.mapCreateRecipeRequestToRecipe(createRecipeRequest);
        recipe.setRecipeIngredients(ingredients);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        Recipe createdRecipe = recipeRepository.save(recipe);

        return createdRecipe.getId();
    }

    public List<RecipeResponse> getRecipeList(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return recipeRepository.findAll(pageRequest)
                .map(recipe -> commonConfigMapper.mapRecipeToRecipeResponse(recipe)).getContent();
    }

    public RecipeResponse getRecipeById(int id) {
        Recipe recipe= recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(MessageConfig.RECIPE_IS_NOT_FOUND));
        return commonConfigMapper.mapRecipeToRecipeResponse(recipe);
    }

    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        Recipe recipe = recipeRepository.findById(updateRecipeRequest.getId())
                .orElseThrow(() -> new RecipeNotFoundException(MessageConfig.RECIPE_IS_NOT_FOUND));

        Set<Ingredient> ingredients =ingredientService.getIngredientsByIds(updateRecipeRequest.getIngredientIds());

        recipe.setName(updateRecipeRequest.getName());
        recipe.setType(updateRecipeRequest.getType());
        recipe.setNumberOfServings(updateRecipeRequest.getNumberOfServings());
        recipe.setInstructions(updateRecipeRequest.getInstructions());
        recipe.setRecipeIngredients(ingredients);
        recipe.setUpdatedAt(LocalDateTime.now());

        recipeRepository.save(recipe);
        return commonConfigMapper.mapRecipeToRecipeResponse(recipe);
    }

    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(MessageConfig.RECIPE_IS_NOT_FOUND);
        }

        recipeRepository.deleteById(id);
    }

    /*public List<RecipeResponse> findBySearchCriteria(RecipeSearchRequest recipeSearchRequest, int page, int size) {
        List<SearchCriteria> searchCriterionRequests = new ArrayList<>();
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder(searchCriterionRequests);
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name")
                .ascending());

        Specification<Recipe> recipeSpecification = createRecipeSpecification(recipeSearchRequest, builder);
        Page<Recipe> filteredRecipes = recipeRepository.findAll(recipeSpecification, pageRequest);

        return filteredRecipes.toList().stream()
                .map(RecipeResponse::new)
                .collect(Collectors.toList());
    }

    private Specification<Recipe> createRecipeSpecification(RecipeSearchRequest recipeSearchRequest,
                                                            RecipeSpecificationBuilder builder) {
        List<SearchCriteriaRequest> searchCriteriaRequests = recipeSearchRequest.getSearchCriteriaRequests();

        if (Optional.ofNullable(searchCriteriaRequests).isPresent()) {
            List<SearchCriteria> searchCriteriaList = searchCriteriaRequests.stream()
                    .map(SearchCriteria::new)
                    .collect(Collectors.toList());

            if (!searchCriteriaList.isEmpty()) searchCriteriaList.forEach(criteria -> {
                criteria.setDataOption(recipeSearchRequest.getDataOption());
                builder.with(criteria);
            });
        }

        return builder
                .build()
                .orElseThrow(() -> new RecipeNotFoundException(messageProvider.getMessage("criteria.notFound")));
    }*/
}
