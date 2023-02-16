package com.abnamro.recipe.model.request;


import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.model.search.DataOptionReqInput;
import com.abnamro.recipe.validator.EnumValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeSearchRequest {
    @JsonProperty("criteria")
    @Schema(description = "Search criteria you want to search recipe with")
    @Valid
    private List<SearchCriteriaRequest> searchCriteriaRequests;

    @Schema(description = "If you want all or just one criteria is enough for filter to work", example = "all")
    @EnumValidator(enumClass = DataOptionReqInput.class, message = RecipeValidationMessageConfig.DATA_OPTION_NOT_VALID)
    private String dataOption;

}
