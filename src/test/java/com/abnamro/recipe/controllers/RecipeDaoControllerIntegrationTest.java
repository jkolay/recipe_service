
package com.abnamro.recipe.controllers;


import com.abnamro.recipe.builder.IngredientTestDataBuilder;
import com.abnamro.recipe.builder.RecipeTestDataBuilder;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.SearchCriteriaRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.repositories.IngredientRepository;
import com.abnamro.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeDaoControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    public void before() {
        recipeRepository.deleteAll();
    }

    @Test
    public void test_createRecipe_successfully() throws Exception {
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredientWithNameParam("tomato");
        IngredientDao savedIngredientDao = ingredientRepository.save(ingredientDao);

        CreateRecipeRequest request = new CreateRecipeRequest("soup",
                "OTHER", 5, List.of(savedIngredientDao.getId()), "someInstruction");

        MvcResult result = performPost("/api/v1/recipe", request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.id");
        Optional<RecipeDao> optionalRecipe = recipeRepository.findById(id);
        assertTrue(optionalRecipe.isPresent());
        assertEquals(optionalRecipe.get().getName(), request.getName());
    }

    @Test
    public void test_getRecipe_successfully() throws Exception {
        RecipeDao RecipeDao = RecipeTestDataBuilder.createRecipe();
        RecipeDao savedRecipeDao = recipeRepository.save(RecipeDao);

        performGet("/api/v1/recipe/" + savedRecipeDao.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipeDao.getId()))
                .andExpect(jsonPath("$.name").value(savedRecipeDao.getName()))
                .andExpect(jsonPath("$.instructions").value(savedRecipeDao.getInstructions()))
                .andExpect(jsonPath("$.numberOfServings").value(savedRecipeDao.getNumberOfServings()));
    }


    @Test
    public void test_listRecipe_successfully() throws Exception {
        RecipeDao recipeDao1 = new RecipeDao();
        recipeDao1.setId(5);
        recipeDao1.setName("name1");
        recipeDao1.setInstructions("Ins1");
        recipeDao1.setType("OTHER");

        RecipeDao recipeDao2 = new RecipeDao();
        recipeDao2.setId(6);
        recipeDao2.setName("name2");
        recipeDao2.setInstructions("Ins2");
        recipeDao2.setType("OTHER");

        List<RecipeDao> storedRecipeListDao = new ArrayList<>();
        storedRecipeListDao.add(recipeDao1);
        storedRecipeListDao.add(recipeDao2);

        recipeRepository.saveAll(storedRecipeListDao);

        MvcResult result = performGet("/api/v1/recipe/page/0/size/10")
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponse> RecipeList = getListFromMvcResult(result, RecipeResponse.class);
        assertEquals(storedRecipeListDao.get(0).getName(), RecipeList.get(0).getName());
        assertEquals(storedRecipeListDao.get(1).getName(), RecipeList.get(1).getName());
    }

    @Test
    public void test_updateRecipe_successfully() throws Exception {
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredientWithNameParam("sugar");
        RecipeDao recipeDao = RecipeTestDataBuilder.createRecipe();
        recipeDao.setRecipeIngredients(Set.of(ingredientDao));
        RecipeDao savedRecipeDao = recipeRepository.save(recipeDao);
        savedRecipeDao.setName("fish curry");
        savedRecipeDao.setInstructions("someInstruction");
        performPut("/api/v1/recipe", savedRecipeDao);
        Optional<RecipeDao> updatedRecipe = recipeRepository.findById(savedRecipeDao.getId());
        assertTrue(updatedRecipe.isPresent());
        assertEquals(savedRecipeDao.getNumberOfServings(), updatedRecipe.get().getNumberOfServings());
        assertEquals(savedRecipeDao.getInstructions(), updatedRecipe.get().getInstructions());
    }

    @Test
    public void test_deleteRecipe_successfully() throws Exception {
        RecipeDao testRecipeDao = RecipeTestDataBuilder.createRecipe();
        RecipeDao savedRecipeDao = recipeRepository.save(testRecipeDao);

        performDelete("/api/v1/recipe", Pair.of("id", String.valueOf(savedRecipeDao.getId())))
                .andExpect(status().isOk());
        Optional<RecipeDao> deletedRecipe = recipeRepository.findById(savedRecipeDao.getId());
        assertTrue(deletedRecipe.isEmpty());
    }



    @Test
    public void test_SearchRecipeByCriteria_successfully() throws Exception {
        //create ingredient for recipe
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredientWithNameParam("milk");
        IngredientDao savedIngredientDao = ingredientRepository.save(ingredientDao);

        //create the recipe
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("pudding",
                "OTHER", 5, List.of(savedIngredientDao.getId()), "someInstruction");

        MvcResult createdRecipe = performPost("/api/v1/recipe", createRecipeRequest)
                .andExpect(status().isCreated())
                .andReturn();

        //prepare the search criteria and by newly created id
        Integer id = readByJsonPath(createdRecipe, "$.id");

        RecipeSearchRequest request = new RecipeSearchRequest();
        List<SearchCriteriaRequest> searchCriteriaList = new ArrayList<>();
        SearchCriteriaRequest searchCriteria = new SearchCriteriaRequest("name",
                "pudding",
                "cn");

        searchCriteriaList.add(searchCriteria);

        request.setDataOption("ALL");
        request.setSearchCriteriaRequests(searchCriteriaList);

        //call search endpoint by previously created criteria
        MvcResult result = performPost("/api/v1/recipe/search", request)
                .andExpect(status().isOk())
                .andReturn();

        Optional<RecipeDao> optionalRecipe = recipeRepository.findById(id);


        List<RecipeResponse> listRecipeList = getListFromMvcResult(result, RecipeResponse.class);
        assertEquals(listRecipeList.size(), listRecipeList.size());
        Assertions.assertTrue(optionalRecipe.isPresent());
        Assertions.assertEquals(listRecipeList.get(0).getName(), optionalRecipe.get().getName());
        Assertions.assertEquals(listRecipeList.get(0).getInstructions(), optionalRecipe.get().getInstructions());
        Assertions.assertEquals(listRecipeList.get(0).getNumberOfServings(), optionalRecipe.get().getNumberOfServings());
    }


}

