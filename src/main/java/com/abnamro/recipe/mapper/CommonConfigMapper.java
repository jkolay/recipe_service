package com.abnamro.recipe.mapper;

import com.abnamro.recipe.model.persistence.Ingredient;
import com.abnamro.recipe.model.persistence.Recipe;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.SearchCriteriaRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.model.search.SearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommonConfigMapper {
    @Mapping(target = "ingredient", source = "name")
    Ingredient mapCreateIngredientRequestToIngredient(CreateIngredientRequest createIngredientRequest);

    IngredientResponse mapIngredientToIngredientResponse(Ingredient ingredient);

    Recipe mapCreateRecipeRequestToRecipe(CreateRecipeRequest recipeRequest);

    RecipeResponse mapRecipeToRecipeResponse(Recipe recipe);

    List<RecipeResponse> mapRecipesToRecipeResponses(List<Recipe> recipe);


    Recipe mapUpdateRecipeRequestToRecipe(UpdateRecipeRequest updateRecipeRequest);

    List<SearchCriteria> mapSearchCriteriaRequestsToSearchCriterias(List<SearchCriteriaRequest> criteriaRequest);
}
