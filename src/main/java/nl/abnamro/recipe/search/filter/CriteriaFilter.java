package nl.abnamro.recipe.search.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface CriteriaFilter<T> {

    Predicate toPredicate(String key, String value, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder);

    default boolean evaluate(String operator) {
        return Boolean.FALSE;
    }
}