/**
 *
 */
package nl.abnamro.recipe.search;

import nl.abnamro.recipe.model.RecipeModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecificationBuilder<T> {

    public static final String OR = "OR";

    private final List<SearchCriteria> params;
    private String joinType;

    public SpecificationBuilder() {
        params = new ArrayList<>();
    }

    public SpecificationBuilder<T> with(String key, Object value, String operation) {
        params.add(new SearchCriteria(key, value, operation, null));
        return this;
    }

    public SpecificationBuilder<T> with(SearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public SpecificationBuilder<T> joinType(String joinType) {
        if (OR.equalsIgnoreCase(joinType)) {
            this.joinType = OR;
        }
        return this;
    }

    public Specification<T> build(Specification<T> result) {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new SearchSpecification<>(param));
        }
        int index = 0;
        if (result == null) {
            result = specs.get(0);
            index = 1;
        }
        for (int i = index; i < specs.size(); i++) {
            if (OR.equalsIgnoreCase(joinType)) {
                result = Specification.where(result).or(specs.get(i));
            } else {
                result = Specification.where(result).and(specs.get(i));
            }
        }
        return result;
    }

    public Optional<Specification<RecipeModel>> build() {
        if (params.size() == 0) return Optional.empty();

        Specification<RecipeModel> result = new SearchSpecification<>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            SearchCriteria criteria = params.get(i);
            result = (OR.equalsIgnoreCase(joinType))
                    ? Specification.where(result).and(new SearchSpecification<>(criteria))
                    : Specification.where(result).or(new SearchSpecification<>(criteria));
        }
        return Optional.of(result);
    }
}