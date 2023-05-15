package nl.abnamro.recipe.service;

import nl.abnamro.recipe.dto.FilterRequest;
import nl.abnamro.recipe.model.RecipeModel;

import java.util.List;

public interface ISearchRecipeService {
    List<RecipeModel> searchRecipe(List<FilterRequest> request, String joinType, int page, int limit);
}
