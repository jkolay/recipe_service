package com.abnamro.recipe.repositories;

import com.abnamro.recipe.model.persistence.RecipeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository class for recipe
 */
@Repository
public interface RecipeRepository extends JpaRepository<RecipeDao, Integer>, JpaSpecificationExecutor<RecipeDao> {

    RecipeDao findByNameEqualsIgnoreCase(String name);

    @Query(value = "SELECT DISTINCT r.id, r.name, r.instructions, r.number_of_servings, " +
            "r.type,r.created_at,r.updated_at " +
            "FROM Recipes r " +
            "LEFT JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "LEFT JOIN ingredients i ON ri.ingredient_id = i.id " +

            "WHERE (:isVegetarian IS NULL OR :isVegetarian IS TRUE OR r.type<> 'VEGETARIAN') " +
            "AND (:isVegetarian IS NULL OR :isVegetarian IS FALSE OR r.type = 'VEGETARIAN') " +
            "AND (:servings IS NULL OR r.number_of_servings = :servings) " +
            "AND (:ingredientIn IS NULL OR lower(i.ingredient) = lower(:ingredientIn)) " +
            "AND (:ingredientEx IS NULL OR r.id NOT IN (SELECT DISTINCT ri2.recipe_id FROM recipe_ingredient ri2, ingredients i2 " +
            "WHERE ri2.ingredient_id = i2.id and lower(i2.ingredient) = lower(:ingredientEx))) " +
            "AND (:text IS  NULL ) OR lower(r.instructions)  LIKE CONCAT('%',lower(:text),'%') " +
            "ORDER BY r.id", nativeQuery = true)
    List<RecipeDao> search(@Param("isVegetarian") Boolean isVegetarian,
                        @Param("servings") Integer servings,
                        @Param("ingredientIn") String ingredientIn,
                        @Param("ingredientEx") String ingredientEx,
                        @Param("text") String text);


}