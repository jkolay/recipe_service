package com.abnamro.recipe.model.request;


import com.abnamro.recipe.config.MessageConfig;
import com.abnamro.recipe.model.enums.FilterKeyReqInput;
import com.abnamro.recipe.model.enums.SearchOperationReqInput;
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

    @EnumValidator(enumClass = FilterKeyReqInput.class, message = MessageConfig.FILTER_KEY_IS_INVALID)
    private String filterKey;

    private Object value;

    @Schema(description = "The operation type you wanted to search (cn - contains, " +
            "nc - doesn't contain, " +
            "eq - equals, " +
            "ne - not equals", example = "cn")
    @EnumValidator(enumClass = SearchOperationReqInput.class, message = MessageConfig.SEARCH_OPTION_IS_NOT_VALID)
    private String operation;

    @Schema(hidden = true)
    private String dataOption;


    public SearchCriteriaRequest(String filterKey, Object value, String operation) {
        this.filterKey=filterKey;
        this.value=value;
        this.operation=operation;
    }
}
