package com.abnamro.recipe.search;

import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.search.DataOptionReqInput;
import com.abnamro.recipe.model.search.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeSpecificationBuilder {
    private final List<SearchCriteria> params;

    public RecipeSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public final RecipeSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, value, operation));
        return this;
    }

    public final RecipeSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Optional<Specification<RecipeDao>> build() {
        if (params.size() == 0) {
            return null;
        }
        Specification<RecipeDao> result = new RecipeSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++) {
            SearchCriteria criteria = params.get(idx);

            Optional<DataOptionReqInput> dataOption = DataOptionReqInput.getDataOption(criteria.getDataOption());
            if (dataOption.isPresent()) {
                result = (dataOption.get() == DataOptionReqInput.ALL)
                        ? Specification.where(result).and(new RecipeSpecification(criteria))
                        : Specification.where(result).or(new RecipeSpecification(criteria));
            }
        }
        return Optional.of(result);
    }

}
