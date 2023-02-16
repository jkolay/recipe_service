package com.abnamro.recipe.builder;


import com.abnamro.recipe.model.persistence.RecipeDao;

public class RecipeTestDataBuilder {
    public static RecipeDao createRecipe() {
        return createRecipe(null);
    }

    public static RecipeDao createRecipe(Integer id) {
        RecipeDao recipeDao = new RecipeDao();
        recipeDao.setId(id);
        recipeDao.setName("Coffee");
        recipeDao.setNumberOfServings(5);
        recipeDao.setType("OTHER");
        recipeDao.setInstructions("someInstruction");

        return recipeDao;
    }
}
