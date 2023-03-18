package com.abnamro.recipe.service;


import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;

import java.util.List;
import java.util.Set;

public interface IngredientService {
    public IngredientResponse create(CreateIngredientRequest request) ;

    public Set<IngredientDao> getIngredientsByIds(List<Integer> ingredientIds);

    public List<IngredientResponse> list(int page, int size) ;

    public void delete(int id) ;

    public IngredientResponse getIngredient(Integer id);

    public List<IngredientResponse> createIngredients(List<CreateIngredientRequest> requests);
}
