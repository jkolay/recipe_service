package com.abnamro.recipe.service;

import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.SearchCriteriaRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;

import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.model.search.SearchCriteria;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.search.RecipeSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final CommonConfigMapper commonConfigMapper;
    private final Logger logger = LoggerFactory.getLogger(RecipeService.class);


    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         IngredientService ingredientService, CommonConfigMapper commonConfigMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.commonConfigMapper = commonConfigMapper;
    }

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        RecipeDao existingRecipeDao = recipeRepository.findByNameEqualsIgnoreCase(createRecipeRequest.getName());
        if(existingRecipeDao !=null){
            logger.error("Recipe is already available in the application");
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.RECIPE_ALREADY_EXISTS+ existingRecipeDao.getId());
        }
        Set<IngredientDao> ingredientDaos =ingredientService.getIngredientsByIds(createRecipeRequest.getIngredientIds());
        RecipeDao recipeDao = commonConfigMapper.mapCreateRecipeRequestToRecipe(createRecipeRequest);
        recipeDao.setRecipeIngredients(ingredientDaos);
        recipeDao.setCreatedAt(LocalDateTime.now());
        recipeDao.setUpdatedAt(LocalDateTime.now());
        RecipeDao createdRecipeDao = recipeRepository.save(recipeDao);
        logger.info("Recipe is created successfully");
        return commonConfigMapper.mapRecipeToRecipeResponse(createdRecipeDao);
    }

    public List<RecipeResponse> getRecipeList(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return commonConfigMapper.mapRecipesToRecipeResponses(recipeRepository.findAll(pageRequest).getContent());
    }

    public RecipeResponse getRecipeById(int id) {
        RecipeDao recipeDao = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    public RecipeResponse updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        RecipeDao existingRecipeDao = recipeRepository.findById(updateRecipeRequest.getId())
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND));

        Set<IngredientDao> ingredientDaos = Optional.ofNullable(updateRecipeRequest.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);

        RecipeDao recipeDao =commonConfigMapper.mapUpdateRecipeRequestToRecipe(updateRecipeRequest);
        recipeDao.setUpdatedAt(LocalDateTime.now());
        if (Optional.ofNullable(ingredientDaos).isPresent()) recipeDao.setRecipeIngredients(ingredientDaos);

        recipeRepository.save(recipeDao);
        return commonConfigMapper.mapRecipeToRecipeResponse(recipeDao);
    }

    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            logger.error("Recipe is not present in the database");
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.RECIPE_IS_NOT_FOUND);
        }

        recipeRepository.deleteById(id);
    }

    public List<RecipeResponse> findBySearchCriteria(RecipeSearchRequest recipeSearchRequest,int page,int size){
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        Specification<RecipeDao> recipeSpecification = createRecipeSpecificationForSearch(recipeSearchRequest);
        Page<RecipeDao> filteredRecipes = recipeRepository.findAll(recipeSpecification, pageRequest);
        return commonConfigMapper.mapRecipesToRecipeResponses(filteredRecipes.getContent());
    }

    private Specification<RecipeDao> createRecipeSpecificationForSearch(RecipeSearchRequest recipeSearchRequest) {
        List<SearchCriteriaRequest> criteriaRequestList = recipeSearchRequest.getSearchCriteriaRequests();
        if(criteriaRequestList.isEmpty()){
            logger.error("Filter is not provided the the request");
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
