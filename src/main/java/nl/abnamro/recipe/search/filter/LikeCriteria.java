package nl.abnamro.recipe.search.filter;

import javax.persistence.criteria.*;

public class LikeCriteria<T> implements CriteriaFilter<T> {
    private static final String LIKE_OPERATOR = "like";
    private static final String PERCENTAGE_OPERATOR = "%";


    @Override
    public Predicate toPredicate(String key, String value, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return key.contains(".") ? builder.like(root.join(key.split("\\.")[0], JoinType.INNER).get(key.split("\\.")[1]), PERCENTAGE_OPERATOR.concat(value).concat(PERCENTAGE_OPERATOR))
                : builder.like(root.get(key), PERCENTAGE_OPERATOR.concat(value).concat(PERCENTAGE_OPERATOR));
    }

    @Override
    public boolean evaluate(String operator) {
        return LIKE_OPERATOR.equalsIgnoreCase(operator);
    }
}
