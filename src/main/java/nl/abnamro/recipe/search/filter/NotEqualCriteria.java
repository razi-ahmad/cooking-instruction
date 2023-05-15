package nl.abnamro.recipe.search.filter;

import javax.persistence.criteria.*;

public class NotEqualCriteria<T> implements CriteriaFilter<T> {
    private static final String EQUAL_OPERATOR = "!=";


    @Override
    public Predicate toPredicate(String key, String value, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return key.contains(".") ? builder.notEqual(root.join(key.split("\\.")[0], JoinType.INNER).get(key.split("\\.")[1]), value)
                : builder.equal(root.<String>get(key), value);
    }

    @Override
    public boolean evaluate(String operator) {
        return EQUAL_OPERATOR.equalsIgnoreCase(operator);
    }
}
