package com.abnamro.recipe.service;


import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.repositories.IngredientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final CommonConfigMapper commonConfigMapper;

    public IngredientService(IngredientRepository ingredientRepository, CommonConfigMapper commonConfigMapper) {
        this.ingredientRepository = ingredientRepository;
        this.commonConfigMapper = commonConfigMapper;
    }

    public IngredientResponse create(CreateIngredientRequest request) {
        if(ingredientRepository.findByIngredientEqualsIgnoreCase(request.getName())!=null){
            throw new IngredientDuplicationException(RecipeValidationMessageConfig.INGREDIENT_ALREADY_EXISTS);
        }
        IngredientDao ingredientDao = commonConfigMapper.mapCreateIngredientRequestToIngredient(request);
        ingredientDao.setCreatedAt(LocalDateTime.now());
        ingredientDao.setUpdatedAt(LocalDateTime.now());
        ingredientDao = ingredientRepository.save(ingredientDao);
        return commonConfigMapper.mapIngredientToIngredientResponse(ingredientDao);
    }


    public Set<IngredientDao> getIngredientsByIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::findById)
                .collect(Collectors.toSet());
    }

    private IngredientDao findById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(RecipeValidationMessageConfig.INGREDIENT_IS_NOT_AVAILABLE + id));
    }

    public List<IngredientResponse> list(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return ingredientRepository.findAll(pageRequest)
                .map(ingredient -> commonConfigMapper.mapIngredientToIngredientResponse(ingredient))
                .getContent();
    }

    public void delete(int id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RecipeNotFoundException();
        }
        ingredientRepository.deleteById(id);
    }

    public IngredientResponse getIngredient(Integer id) {
        return commonConfigMapper.mapIngredientToIngredientResponse(findById(id));
    }

    public List<IngredientResponse> createIngredients(List<CreateIngredientRequest> requests) {
        return requests.stream().map(ingredient->create(ingredient)).collect(Collectors.toList());
    }
}
