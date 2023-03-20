package com.abnamro.recipe.unit.controllers;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import com.abnamro.recipe.controllers.IngredientController;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.service.IngredientService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IngredientControllerTest {
    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    public void test_createIngredient_successfully() {
        when(ingredientService.create(any(CreateIngredientRequest.class))).thenReturn(new IngredientResponse());
    CreateIngredientRequest request = new CreateIngredientRequest();
        assertNotNull(ingredientController.createIngredient(request));
    }

    @Test
    public void test_getIngredient_successfully() {
        IngredientResponse ingredient = new IngredientResponse();
        ingredient.setId(5);
        when(ingredientService.getIngredient(anyInt())).thenReturn(ingredient);
        assertNotNull(ingredientController.getIngredient(5));
    }

    @Test
    public void test_listIngredients_successfully() {
        List<IngredientResponse> storedIngredientList = new ArrayList<>();
        when(ingredientService.list(anyInt(), anyInt())).thenReturn(storedIngredientList);
        assertNotNull(ingredientController.getIngredientList(anyInt(), anyInt()));
    }

    @Test
    public void test_deleteIngredient_successfully() {
        doNothing().when(ingredientService).delete(anyInt());
        ingredientController.deleteIngredient(5);
    }

}