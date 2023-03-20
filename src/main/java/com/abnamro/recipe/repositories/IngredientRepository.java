package com.abnamro.recipe.repositories;

import com.abnamro.recipe.model.persistence.IngredientDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository class for ingredients
 */
@Repository
public interface IngredientRepository extends JpaRepository<IngredientDao, Integer> {
    IngredientDao findByIngredientEqualsIgnoreCase(String ingredient);

}
