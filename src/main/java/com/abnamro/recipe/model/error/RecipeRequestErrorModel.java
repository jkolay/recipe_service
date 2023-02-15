package com.abnamro.recipe.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestErrorModel {

    private Map<String,String> errorDescription;
    private String code;
    private ErrorSeverityLevelCodeType severityLevel;

}
