package nl.abnamro.recipe.service;

import nl.abnamro.recipe.dto.IngredientRequest;
import nl.abnamro.recipe.dto.IngredientResponse;
import nl.abnamro.recipe.model.IngredientModel;

import java.util.List;

public interface IIngredientService {
    IngredientResponse create(IngredientRequest request);

    IngredientResponse update(Integer id, IngredientRequest request);

    void delete(int id);

    IngredientResponse getById(int id);

    List<IngredientModel> getIngredientsByIds(List<Integer> ingredientIds);

    List<IngredientResponse> list(int page, int size);

    List<IngredientResponse> map(List<IngredientModel> ingredients);

}
