package com.abnamro.recipe.service;


import com.abnamro.recipe.config.MessageConfig;
import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.Ingredient;
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

    public Ingredient create(CreateIngredientRequest request) {
        if(ingredientRepository.findByIngredientEqualsIgnoreCase(request.getName())!=null){
            throw new IngredientDuplicationException(MessageConfig.INGREDIENT_ALREADY_EXISTS);
        }
        Ingredient ingredient = commonConfigMapper.mapCreateIngredientRequestToIngredient(request);
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setUpdatedAt(LocalDateTime.now());
        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }


    public Set<Ingredient> getIngredientsByIds(List<Integer> ingredientIds) {
        return ingredientRepository.findByIdIn(ingredientIds).stream()
               .collect(Collectors.toSet());
    }

    public Ingredient findById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException());
    }

    public List<Ingredient> list(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return ingredientRepository.findAll(pageRequest)
                .getContent();
    }

    public void delete(int id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RecipeNotFoundException();
        }
        ingredientRepository.deleteById(id);
    }
}
