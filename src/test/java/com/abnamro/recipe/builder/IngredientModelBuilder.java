package com.abnamro.recipe.builder;



import com.abnamro.recipe.model.persistence.IngredientDao;

import java.time.LocalDateTime;

public class IngredientModelBuilder {
    private Integer id;
    private String ingredientName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IngredientDao build() {
        IngredientDao ingredientDao = new IngredientDao();
        ingredientDao.setId(id);
        ingredientDao.setIngredient(ingredientName);
        ingredientDao.setCreatedAt(createdAt);
        ingredientDao.setUpdatedAt(updatedAt);

        return ingredientDao;
    }
    public IngredientModelBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public IngredientModelBuilder withName(String name) {
        this.ingredientName = name;
        return this;
    }
    public IngredientModelBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public IngredientModelBuilder withUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
