package com.abnamro.recipe.service.impl;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.constant.RecipeType;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.service.IngredientService;
import com.abnamro.recipe.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * service implementation class for recipe service
 */
@Service
public class RecipeServiceImpl implements RecipeService {
    private static final String TOPIC = "recipe";
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final CommonConfigMapper commonConfigMapper;
    @Qualifier("kafkaTemplate")
    private final KafkaTemplate<String, RecipeDao> kafkaTemplate;
    @Qualifier("kafkaTemplateStr")
    private final KafkaTemplate<String, String> kafkaTemplateStr;
    private final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientService ingredientService, CommonConfigMapper commonConfigMapper, KafkaTemplate<String, RecipeDao> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateStr) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.commonConfigMapper = commonConfigMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateStr = kafkaTemplateStr;
    }

    /**
     * creates a new recipe in the app.after creating a recipe publish the object in kafka to recipe topic
     * @param createRecipeRequest
     * @return
     */
    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        RecipeDao existingRecipeDao = recipeRepository.findByNameEqualsIgnoreCase(createRecipeRequest.getName());
        if (existingRecipeDao != null) {
            logger.error("Recipe is already available in the application");
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.RECIPE_ALREADY_EXISTS + existingRecipeDao.getId());
        }
        Set<IngredientDao> ingredientDaos = ingredientService.getIngredientsByIds(createRecipeRequest.getIngredientIds());
        RecipeDao recipeDao = commonConfigMapper.mapCreateRecipeRequestToRecipe(createRecipeRequest);
        recipeDao.setRecipeIngredients(ingredientDaos);
        recipeDao.setCreatedAt(LocalDateTime.now());
        recipeDao.setUpdatedAt(LocalDateTime.now());
        RecipeDao createdRecipeDao = recipeRepository.save(recipeDao);
        logger.info("Recipe is created successfully");
        kafkaTemplateStr.send(TOPIC, "Recipe created");
        kafkaTemplate.send(TOPIC, createdRecipeDao);
        return commonConfigMapper.mapRecipeToRecipeResponse(createdRecipeDao);
    }

    /**
     * retrieves list of recipes based on page number and record size in a page
     * @param page
     * @param size
     * @return
     */
    public List<RecipeResponse> getRecipeList(int page, int size) {
        logger.info("Recipe list is getting retrieved");
        Pageable pageRequest = PageRequest.of(page, size);
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findAll(pageRequest).getContent());
    }

    /**
     * implementation to retrieve a recipe by the id
     * @param id
     * @return
     */
    public RecipeResponse getRecipeById(int id) {
        logger.info("Recipe  is getting retrieved by recipe id");
        RecipeDao recipeDao = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    /**
     * Implementation to modify a recipe and publish message to kafka in case of successful posting
     * @param updateRecipeRequest
     * @return
     */
    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        logger.info("recipe is getting updated");
        RecipeDao existingRecipeDao = recipeRepository.findById(updateRecipeRequest.getId()).orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));

        Set<IngredientDao> ingredientDaos = Optional.ofNullable(updateRecipeRequest.getIngredientIds()).map(ingredientService::getIngredientsByIds).orElse(null);

        RecipeDao recipeDao = commonConfigMapper.mapUpdateRecipeRequestToRecipe(updateRecipeRequest);
        recipeDao.setUpdatedAt(LocalDateTime.now());
        recipeDao.setCreatedAt(existingRecipeDao.getCreatedAt());
        if (Optional.ofNullable(ingredientDaos).isPresent()) {
            recipeDao.setRecipeIngredients(ingredientDaos);
        }

        recipeRepository.save(recipeDao);
        kafkaTemplateStr.send(TOPIC, "Recipe updated");
        kafkaTemplate.send(TOPIC, recipeDao);
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    /**
     * Implementation to delete a recipe from app
     * @param id
     */
    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            logger.error("Recipe is not present in the database");
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
        }

        recipeRepository.deleteById(id);
        kafkaTemplateStr.send(TOPIC, "Recipe deleted with recipe id " + id);
    }

    /**
     * Implementation to retrieves recipes by recipe type
     * @param recipeTpe
     * @return
     */
    @Override
    public List<RecipeResponse> getRecipeListByRecipeType(String recipeTpe) {
        if (!recipeTpe.equalsIgnoreCase(RecipeType.VEGETARIAN.name()) && !recipeTpe.equalsIgnoreCase(RecipeType.OTHER.name())) {
            logger.error("Recipe type is not present in the database");
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_TYPE_IS_NOT_FOUND);

        }
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findByTypeEqualsIgnoreCase(recipeTpe));
    }

    /**
     * Implementation to retrieve recipe by number of serving
     * @param numberOfServing
     * @return
     */
    @Override
    public List<RecipeResponse> getRecipesByServing(Integer numberOfServing) {
        if(numberOfServing<=0){
            throw new RecipeNotFoundException("Number of serving must be greater than 0");
        }
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findByNumberOfServingsGreaterThan(numberOfServing - 1));
    }
}
