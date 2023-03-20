package com.abnamro.recipe.unit.services;


import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.CommonConfigMapper;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.repositories.IngredientRepository;
import com.abnamro.recipe.service.impl.IngredientServiceImpl;
import org.junit.jupiter.api.Assertions;import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CommonConfigMapper commonConfigMapper;

    @Mock
    KafkaTemplate<String, IngredientDao> kafkaTemplate;

    @Mock
    KafkaTemplate<String, String> kafkaTemplateStr;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @Test
    public void test_createIngredient_successfully() {
        //Given
        CreateIngredientRequest request = new CreateIngredientRequest("sugar");
        //When
        IngredientDao ingredient = Mockito.mock(IngredientDao.class);
        when(commonConfigMapper.mapCreateIngredientRequestToIngredient(Mockito.any(CreateIngredientRequest.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(IngredientDao.class))).thenReturn(ingredient);
        when(commonConfigMapper.mapIngredientToIngredientResponse(Mockito.any(IngredientDao.class))).thenReturn(new IngredientResponse());
        IngredientResponse response= ingredientService.create(request);

        //Then
        Assertions.assertNotNull(response);
    }


    @Test
    public void test_deleteIngredient_successfully() {
        when(ingredientRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(ingredientRepository).deleteById(anyInt());
        ingredientService.delete(5);
    }

   @Test
    public void test_deleteIngredient_notFound() {
        when(ingredientRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(RecipeNotFoundException.class,()->ingredientService.delete(1));
    }

    @Test
    public void test_getIngredientsByIds(){
        IngredientDao ingredientDao=Mockito.mock(IngredientDao.class);
        List<Integer> ingredientIds = new ArrayList<>();
        ingredientIds.add(1);
        when(ingredientRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(ingredientDao));
        assertNotNull(ingredientService.getIngredientsByIds(ingredientIds));
    }


}