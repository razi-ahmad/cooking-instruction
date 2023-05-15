package nl.abnamro.recipe.search;

import nl.abnamro.recipe.search.filter.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SearchSpecification<T> implements Specification<T> {


    private final SearchCriteria criteria;
    private final List<CriteriaFilter<T>> filters;


    public SearchSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
        filters = List.of(
                new EqualCriteria<>(),
                new NotEqualCriteria<>(),
                new LikeCriteria<>(),
                new NotLikeCriteria<>()
        );
    }


    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return filters
                .stream()
                .filter(f -> f.evaluate(criteria.getOperation()))
                .findFirst()
                .map(f -> f.toPredicate(criteria.getKey(), criteria.getValue().toString(), root, query, builder))
                .orElse(null);
    }
}