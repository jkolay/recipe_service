package com.abnamro.recipe.mapper;

import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.model.response.RecipeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * This is a mapper interface which generates the model to dao class mapping implementation and dao to response class implementation at runtime
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommonConfigMapper {
    @Mapping(target = "ingredient", source = "name")
    IngredientDao mapCreateIngredientRequestToIngredient(CreateIngredientRequest createIngredientRequest);

    IngredientResponse mapIngredientToIngredientResponse(IngredientDao ingredientDao);

    RecipeDao mapCreateRecipeRequestToRecipe(CreateRecipeRequest recipeRequest);

    RecipeResponse mapRecipeToRecipeResponse(RecipeDao recipeDao);

    List<RecipeResponse> mapRecipesToRecipeResponses(List<RecipeDao> recipeList);
    RecipeDao mapUpdateRecipeRequestToRecipe(UpdateRecipeRequest updateRecipeRequest);

}
