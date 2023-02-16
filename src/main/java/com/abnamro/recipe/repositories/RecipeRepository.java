package com.abnamro.recipe.repositories;

import com.abnamro.recipe.model.persistence.RecipeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeDao, Integer>, JpaSpecificationExecutor<RecipeDao> {

    RecipeDao findByNameEqualsIgnoreCase(String name);
}