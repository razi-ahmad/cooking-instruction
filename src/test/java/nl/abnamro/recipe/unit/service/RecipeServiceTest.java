package nl.abnamro.recipe.unit.service;

import nl.abnamro.recipe.dto.RecipeRequest;
import nl.abnamro.recipe.dto.RecipeResponse;
import nl.abnamro.recipe.enums.Category;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.model.RecipeModel;
import nl.abnamro.recipe.repository.RecipeRepository;
import nl.abnamro.recipe.service.IngredientService;
import nl.abnamro.recipe.service.RecipeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @InjectMocks
    private RecipeService underTest;

    @Mock
    private RecipeRepository repository;

    @Mock
    private IngredientService ingredientService;

    @Test
    void test_create_recipe_successful() {

        RecipeRequest request = getRecipeRequest(List.of(1));
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));

        Mockito.when(repository.save(ArgumentMatchers.any(RecipeModel.class))).thenReturn(model);
        Mockito.when(ingredientService.getIngredientsByIds(request.getIngredientIds())).thenReturn(model.getIngredients());

        RecipeResponse result = underTest.create(request);
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(request.getName());
    }


    @Test
    void test_update_recipe_successful() {
        RecipeRequest request = getRecipeRequest(List.of(1));
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));

        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(model));
        Mockito.when(repository.save(ArgumentMatchers.any(RecipeModel.class))).thenReturn(model);
        Mockito.when(ingredientService.getIngredientsByIds(request.getIngredientIds())).thenReturn(model.getIngredients());

        assert model != null;
        RecipeResponse result = underTest.update(model.getId(), request);
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(request.getName());
    }


    @Test
    void test_update_when_not_found_recipe() {
        RecipeRequest request = getRecipeRequest(List.of(1));
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> underTest.update(model.getId(), request));

    }

    @Test
    void test_delete_recipe() {
        Mockito.doNothing().when(repository).deleteById(1);
        Mockito.when(repository.existsById(1)).thenReturn(Boolean.TRUE);

        underTest.delete(1);

        Mockito.verify(repository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void test_delete_recipe_when_not_found() {
        Mockito.when(repository.existsById(1)).thenReturn(Boolean.FALSE);

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> underTest.delete(1));

        Mockito.verify(repository, Mockito.times(0)).deleteById(1);
    }

    @Test
    void test_get_by_id_recipe() {
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));

        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(model));

        assert model != null;
        RecipeResponse result = underTest.getById(model.getId());
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(model.getName());
    }

    @Test
    void test_get_by_id_when_not_found_recipe() {
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> underTest.getById(model.getId()));
    }

    @Test
    void test_list_recipes() {
        RecipeModel model = getRecipeModel(List.of(IngredientModel.builder().id(1).build()));
        Mockito.when(repository.findAll(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(List.of(model)));

        List<RecipeResponse> result = underTest.list(0, 1);
        Assertions.assertThat(result.size()).isSameAs(1);
    }

    private static RecipeModel getRecipeModel(List<IngredientModel> ingredients) {
        return RecipeModel
                .builder()
                .id(1)
                .name("Boiling egg")
                .category(Category.VEGAN)
                .instruction("Take water and put egg in water and start the flame")
                .ingredients(ingredients)
                .build();
    }

    private static RecipeRequest getRecipeRequest(List<Integer> ids) {
        return RecipeRequest
                .builder()
                .name("Boiling egg")
                .ingredientIds(ids)
                .category(Category.VEGAN)
                .instruction("Take water and put egg in water and start the flame")
                .build();
    }
}