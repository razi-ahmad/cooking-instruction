package nl.abnamro.recipe.service;

import nl.abnamro.recipe.dto.FilterRequest;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.model.RecipeModel;
import nl.abnamro.recipe.repository.RecipeRepository;
import nl.abnamro.recipe.search.SearchCriteria;
import nl.abnamro.recipe.search.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchRecipeService implements ISearchRecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public SearchRecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeModel> searchRecipe(List<FilterRequest> request, String joinType, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        SpecificationBuilder<RecipeModel> builder = new SpecificationBuilder<>();

        List<SearchCriteria> criteria = request
                .stream()
                .map(f -> new SearchCriteria(f.getKey(), f.getValue(), f.getOperation(), RecipeModel.class))
                .collect(Collectors.toList());
        criteria.forEach(builder::with);

        Specification<RecipeModel> specification = builder
                .build()
                .orElseThrow(() -> new NotFoundException("criteria.notFound"));
        return recipeRepository.findAll(specification, pageable).toList();

    }
}
