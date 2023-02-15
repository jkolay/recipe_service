package com.abnamro.recipe.repositories;

import com.abnamro.recipe.model.persistence.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByIdIn(List<Integer> ingredientIds);
    Ingredient findByIngredientEqualsIgnoreCase(String ingredient);

}
