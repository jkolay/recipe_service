
package com.abnamro.recipe.controllers;

import com.abnamro.recipe.builder.IngredientTestDataBuilder;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.repositories.IngredientRepository;
import com.abnamro.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientDaoControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void before() {

        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }


    @Test
    public void test_createIngredient_successfully() throws Exception {
        CreateIngredientRequest request = IngredientTestDataBuilder.createIngredientRequest();
        MvcResult result = performPost("/api/v1/ingredient", request)
                .andExpect(status().isCreated())
                .andReturn();
        Integer id = readByJsonPath(result, "$.id");
        Optional<IngredientDao> ingredient = ingredientRepository.findById(id);
        assertTrue(ingredient.isPresent());
        assertEquals(ingredient.get().getIngredient(), request.getName());
    }

    @Test
    public void test_listIngredient_successfully() throws Exception {
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredient();
        IngredientDao savedIngredientDao = ingredientRepository.save(ingredientDao);
        performGet("/api/v1/ingredient/" + savedIngredientDao.getId())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(savedIngredientDao.getId()))
                .andExpect(jsonPath("$.ingredient").value(ingredientDao.getIngredient()));
    }

    @Test
    public void test_listIngredients_successfully() throws Exception {
        List<IngredientDao> ingredientDaoList = IngredientTestDataBuilder.createIngredientList();
        ingredientRepository.saveAll(ingredientDaoList);
        MvcResult result = performGet("/api/v1/ingredient/page/0/size/10")
                .andExpect(status().isFound())
                .andReturn();
        List<IngredientResponse> responses = getListFromMvcResult(result, IngredientResponse.class);
        assertEquals(ingredientDaoList.size(), responses.size());
    }

    @Test
    public void test_deleteIngredients_successfully() throws Exception {
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredient();
        IngredientDao savedIngredientDao = ingredientRepository.save(ingredientDao);
        performDelete("/api/v1/ingredient?id=" + savedIngredientDao.getId()).andExpect(status().isOk());
        Optional<IngredientDao> deletedIngredient = ingredientRepository.findById(savedIngredientDao.getId());
        assertTrue(deletedIngredient.isEmpty());
    }

    @Test
    public void test_findIngredientById_successfully() throws Exception {
        IngredientDao ingredientDao = IngredientTestDataBuilder.createIngredient();
        IngredientDao savedIngredientDao = ingredientRepository.save(ingredientDao);
        MvcResult result = performGet("/api/v1/ingredient/" + savedIngredientDao.getId())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(savedIngredientDao.getId()))
                .andExpect(jsonPath("$.ingredient").value(savedIngredientDao.getIngredient()))
                .andReturn();
        IngredientResponse ingredientResponse = getFromMvcResult(result, IngredientResponse.class);
        assertEquals(savedIngredientDao.getIngredient(), ingredientResponse.getIngredient());
    }
}

