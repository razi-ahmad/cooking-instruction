package nl.abnamro.recipe.service;

import nl.abnamro.recipe.dto.RecipeRequest;
import nl.abnamro.recipe.dto.RecipeResponse;

import java.util.List;

public interface IRecipeService {

    RecipeResponse create(RecipeRequest request);

    RecipeResponse update(Integer id, RecipeRequest request);

    void delete(Integer id);

    RecipeResponse getById(Integer id);

    List<RecipeResponse> list(int page, int size);

}
