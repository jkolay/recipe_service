package com.abnamro.recipe.builder;



import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.request.CreateIngredientRequest;

import java.time.LocalDateTime;
import java.util.List;

public class IngredientTestDataBuilder {
    public static CreateIngredientRequest createIngredientRequest() {
        return new CreateIngredientRequestBuilder()
                .withName("Coffee")
                .build();
    }

    public static IngredientDao createIngredient() {
        return new IngredientModelBuilder()
                .withName("Coffee")
                .build();
    }

    public static IngredientDao createIngredientWithNameParam(String name) {
        return new IngredientModelBuilder()
                .withName(name)
                .build();
    }


    public static List<IngredientDao> createIngredientList() {
        return createIngredientList(false);
    }

    public static List<IngredientDao> createIngredientList(boolean withId) {
        IngredientDao i1 = new IngredientModelBuilder()
                .withId(withId ? 2 : null)
                .withName("milk")
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(LocalDateTime.now())
                .build();

        IngredientDao i2 = new IngredientModelBuilder()
                .withId(withId ? 3 : null)
                .withName("sugar")
                .withCreatedAt(LocalDateTime.now())
                .withUpdatedAt(LocalDateTime.now())
                .build();

        return List.of(i1, i2);
    }
}
