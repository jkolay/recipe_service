package com.abnamro.recipe.model.response;

import com.abnamro.recipe.model.persistence.Recipe;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IngredientResponse {
    private Integer id;
    private String ingredient;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
