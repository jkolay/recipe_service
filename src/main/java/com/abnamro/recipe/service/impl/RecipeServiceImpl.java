package com.abnamro.recipe.service.impl;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.service.IngredientService;
import com.abnamro.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * service implementation class for recipe service
 */
@Service
@Slf4j(topic = "RecipeServiceImpl")
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final CommonConfigMapper commonConfigMapper;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientService ingredientService, CommonConfigMapper commonConfigMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.commonConfigMapper = commonConfigMapper;

    }

    /**
     * creates a new recipe in the app.after creating a recipe publish the object in kafka to recipe topic
     * @param createRecipeRequest
     * @return
     */
    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        var existingRecipeDao = recipeRepository.findByNameEqualsIgnoreCase(createRecipeRequest.getName());
        if (existingRecipeDao != null) {
            log.error("Recipe is already available in the application");
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.RECIPE_ALREADY_EXISTS + existingRecipeDao.getId());
        }
        Set<IngredientDao> ingredientDaos = ingredientService.getIngredientsByIds(createRecipeRequest.getIngredientIds());
        var recipeDao = commonConfigMapper.mapCreateRecipeRequestToRecipe(createRecipeRequest);
        recipeDao.setRecipeIngredients(ingredientDaos);
        recipeDao.setCreatedAt(LocalDateTime.now());
        recipeDao.setUpdatedAt(LocalDateTime.now());
        var createdRecipeDao = recipeRepository.save(recipeDao);
        log.info("Recipe is created successfully");
        return commonConfigMapper.mapRecipeToRecipeResponse(createdRecipeDao);
    }

    /**
     * retrieves list of recipes based on page number and record size in a page
     * @param page
     * @param size
     * @return
     */
    public List<RecipeResponse> getRecipeList(int page, int size) {
        log.info("Recipe list is getting retrieved");
        var pageRequest = PageRequest.of(page, size);
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findAll(pageRequest).getContent());
    }

    /**
     * implementation to retrieve a recipe by the id
     * @param id
     * @return
     */
    public RecipeResponse getRecipeById(int id) {
        log.info("Recipe  is getting retrieved by recipe id");
        var recipeDao = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    /**
     * Implementation to modify a recipe and publish message to kafka in case of successful posting
     * @param updateRecipeRequest
     * @return
     */
    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        log.info("recipe is getting updated");
        var existingRecipeDao = recipeRepository.findById(updateRecipeRequest.getId()).orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));

        var ingredientDaos = Optional.ofNullable(updateRecipeRequest.getIngredientIds()).map(ingredientService::getIngredientsByIds).orElse(null);

        var recipeDao = commonConfigMapper.mapUpdateRecipeRequestToRecipe(updateRecipeRequest);
        recipeDao.setUpdatedAt(LocalDateTime.now());
        recipeDao.setCreatedAt(existingRecipeDao.getCreatedAt());
        if (Optional.ofNullable(ingredientDaos).isPresent()) {
            recipeDao.setRecipeIngredients(ingredientDaos);
        }

        recipeRepository.save(recipeDao);
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    /**
     * Implementation to delete a recipe from app
     * @param id
     */
    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            log.error("Recipe is not present in the database");
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
        }

        recipeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RecipeResponse> search(RecipeSearchRequest request) {
        var recipeDaos= recipeRepository.search(
                request.getIsVegetarian(),
                request.getServings(),
                request.getIngredientIn(),
                request.getIngredientEx(),
                request.getText());
        final var recipes = recipeDaos.stream()
                .map(recipe -> commonConfigMapper.mapRecipeToRecipeResponse(recipe)).collect(Collectors.toList());
        if (recipes.isEmpty()) {
            log.error(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
        }
        return recipes;
    }

}
