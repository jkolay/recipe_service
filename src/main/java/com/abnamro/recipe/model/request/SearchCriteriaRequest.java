package com.abnamro.recipe.model.request;


import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.model.search.FilterKeyReqInput;
import com.abnamro.recipe.model.search.SearchOperationReqInput;
import com.abnamro.recipe.validator.EnumValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Valid
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaRequest {

    @Schema(description = "The filter key you want to search (name, numberOfServings, type, instructions, ingredient)",example = "name")
    @EnumValidator(enumClass = FilterKeyReqInput.class, message = RecipeValidationMessageConfig.FILTER_KEY_IS_INVALID)
    private String filterKey;

    private Object value;

    @Schema(description = "The operation type you wanted to search (cn - contains, " +
            "nc - doesn't contain, " +
            "eq - equals, " +
            "ne - not equals", example = "cn")
    @EnumValidator(enumClass = SearchOperationReqInput.class, message = RecipeValidationMessageConfig.SEARCH_OPTION_IS_NOT_VALID)
    private String operation;
}
