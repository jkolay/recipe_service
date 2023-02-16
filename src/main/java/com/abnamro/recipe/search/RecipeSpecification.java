package com.abnamro.recipe.search;

import com.abnamro.recipe.model.constant.DatabaseConstant;
import com.abnamro.recipe.model.persistence.IngredientDao;
import com.abnamro.recipe.model.persistence.RecipeDao;
import com.abnamro.recipe.model.search.SearchCriteria;
import com.abnamro.recipe.model.search.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;

public class RecipeSpecification implements Specification<RecipeDao> {
    private final SearchCriteria searchCriteria;

    public RecipeSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<RecipeDao> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strToSearch = searchCriteria.getValue().toString().toLowerCase();
        query.distinct(true);
        switch (Objects.requireNonNull(SearchOperation.getOperation(searchCriteria.getOperation()).get())) {
            case CONTAINS:
                if (searchCriteria.getFilterKey().equals(DatabaseConstant.INGREDIENT_KEY)) {
                    return cb.like(cb.lower(ingredientJoin(root).get(searchCriteria.getFilterKey()).as(String.class)),
                            "%" + strToSearch + "%");
                }
                return cb.like(root.get(searchCriteria.getFilterKey()).as(String.class), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                if (searchCriteria.getFilterKey().equals(DatabaseConstant.INGREDIENT_KEY)) {
                    return cb.notLike(cb.lower(ingredientJoin(root).get(searchCriteria.getFilterKey()).as(String.class)),
                            "%" + strToSearch + "%");
                }
                return cb.notLike(root.get(searchCriteria.getFilterKey()).as(String.class), "%" + strToSearch + "%");
            case EQUAL:
                if (searchCriteria.getFilterKey().equals(DatabaseConstant.INGREDIENT_KEY)) {
                    return cb.equal(cb.lower(ingredientJoin(root).get(searchCriteria.getFilterKey()).as(String.class)),
                            strToSearch);
                }
                return cb.equal(cb.lower(root.get(searchCriteria.getFilterKey()).as(String.class)), strToSearch);
            case NOT_EQUAL:
                if (searchCriteria.getFilterKey().equals(DatabaseConstant.INGREDIENT_KEY)) {
                    return cb.notEqual(cb.lower(ingredientJoin(root).get(searchCriteria.getFilterKey()).as(String.class)),
                            strToSearch);
                }
                return cb.notEqual(root.get(searchCriteria.getFilterKey()).as(String.class), strToSearch);

        }
        return null;
    }

    private Join<RecipeDao, IngredientDao> ingredientJoin(Root<RecipeDao> root) {
        return root.join(DatabaseConstant.JOINED_TABLE_NAME, JoinType.INNER);
    }
}
