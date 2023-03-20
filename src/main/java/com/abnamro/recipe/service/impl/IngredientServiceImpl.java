package com.abnamro.recipe.service.impl;


import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.repositories.IngredientRepository;
import com.abnamro.recipe.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ingredient service implementation class
 */
@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final CommonConfigMapper commonConfigMapper;

    @Qualifier("kafkaTemplate")
    private final KafkaTemplate<String, IngredientDao> kafkaTemplate;

    @Qualifier("kafkaTemplateStr")
    private final KafkaTemplate<String, String> kafkaTemplateStr;

    private static final String TOPIC="recipe";

    private final Logger logger = LoggerFactory.getLogger(IngredientService.class);

    public IngredientServiceImpl(IngredientRepository ingredientRepository, CommonConfigMapper commonConfigMapper, KafkaTemplate<String,IngredientDao> kafkaTemplate, KafkaTemplate<String,String> kafkaTemplateStr) {
        this.ingredientRepository = ingredientRepository;
        this.commonConfigMapper = commonConfigMapper;this.kafkaTemplate = kafkaTemplate;
    this.kafkaTemplateStr = kafkaTemplateStr;}

    /**
     * implementation method to create a new ingredient
     * @param request
     * @return
     */
    public IngredientResponse create(CreateIngredientRequest request) {
        if(ingredientRepository.findByIngredientEqualsIgnoreCase(request.getName())!=null){
            logger.error("Ingredient is already present in the application");
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.INGREDIENT_ALREADY_EXISTS);
        }
        IngredientDao ingredientDao = commonConfigMapper.mapCreateIngredientRequestToIngredient(request);
        ingredientDao.setCreatedAt(LocalDateTime.now());
        ingredientDao.setUpdatedAt(LocalDateTime.now());
        ingredientDao = ingredientRepository.save(ingredientDao);
        logger.info("Created ingredient successfully ");
        kafkaTemplateStr.send(TOPIC,"Ingredient created");
        kafkaTemplate.send(TOPIC,ingredientDao);
        return commonConfigMapper.mapIngredientToIngredientResponse(ingredientDao);
    }

    /**
     * implementation to retrieve ingredient by ids
     * @param ingredientIds
     * @return
     */
    public Set<IngredientDao> getIngredientsByIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::findById)
                .collect(Collectors.toSet());
    }

    /**
     * implementation to retrieve ingredient by id
     * @param id
     * @return
     */
    private IngredientDao findById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.INGREDIENT_IS_NOT_AVAILABLE + id));
    }

    /**
     * implementation to retrieve list of ingredients
     * @param page
     * @param size
     * @return
     */
    public List<IngredientResponse> list(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return ingredientRepository.findAll(pageRequest)
                .map(ingredient -> commonConfigMapper.mapIngredientToIngredientResponse(ingredient))
                .getContent();
    }

    /**
     * implementation to delete ingredient by id
     * @param id
     */
    public void delete(int id) {
        if (!ingredientRepository.existsById(id)) {
            logger.error("Ingredient is not found ");
            throw new RecipeNotFoundException(RecipeValidationMessageConfig.INGREDIENT_IS_NOT_AVAILABLE);
        }
        ingredientRepository.deleteById(id);
        kafkaTemplateStr.send(TOPIC,"Ingredient deleted with id "+id);
    }

    /**
     * implementation to retrieve ingredient by id
     * @param id
     * @return
     */
    public IngredientResponse getIngredient(Integer id) {
        return commonConfigMapper.mapIngredientToIngredientResponse(findById(id));
    }

    /**
     * implementation to create list of ingredients
     * @param requests
     * @return
     */
    public List<IngredientResponse> createIngredients(List<CreateIngredientRequest> requests) {
        logger.info("Ingredients are getting added to app");
        return requests.stream().map(ingredient->create(ingredient)).collect(Collectors.toList());
    }
}
