package nl.abnamro.recipe.unit.service;

import nl.abnamro.recipe.dto.IngredientRequest;
import nl.abnamro.recipe.dto.IngredientResponse;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.repository.IngredientRepository;
import nl.abnamro.recipe.service.IngredientService;
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
class IngredientServiceTest {

    @InjectMocks
    private IngredientService underTest;

    @Mock
    private IngredientRepository repository;


    @Test
    void test_create_ingredient_successful() {
        IngredientRequest request = getIngredientRequest("Egg");
        IngredientModel model = getIngredientModel(request.getName());

        Mockito.when(repository.save(ArgumentMatchers.any(IngredientModel.class))).thenReturn(model);

        IngredientResponse result = underTest.create(request);
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(request.getName());
    }

    @Test
    void test_update_ingredient_successful() {
        IngredientRequest request = getIngredientRequest("Potato");
        IngredientModel model = getIngredientModel("Egg");

        Mockito.when(repository.save(ArgumentMatchers.any(IngredientModel.class))).thenReturn(model);
        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(model));

        assert model != null;
        IngredientResponse result = underTest.update(model.getId(), request);
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(request.getName());
    }

    @Test
    void test_update_create_when_not_found_ingredient_successful() {
        IngredientRequest request = getIngredientRequest("Potato");
        IngredientModel model = getIngredientModel(request.getName());

        Mockito.when(repository.save(ArgumentMatchers.any(IngredientModel.class))).thenReturn(model);
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        IngredientResponse result = underTest.update(model.getId(), request);
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(request.getName());
    }

    @Test
    void test_delete_ingredient() {
        Mockito.doNothing().when(repository).deleteById(1);
        Mockito.when(repository.existsById(1)).thenReturn(Boolean.TRUE);

        underTest.delete(1);

        Mockito.verify(repository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void test_delete_ingredient_when_not_found() {
        Mockito.when(repository.existsById(1)).thenReturn(Boolean.FALSE);

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> underTest.delete(1));

        Mockito.verify(repository, Mockito.times(0)).deleteById(1);
    }

    @Test
    void test_get_by_id_ingredient() {
        IngredientModel model = getIngredientModel("Egg");

        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(model));

        assert model != null;
        IngredientResponse result = underTest.getById(model.getId());
        Assertions.assertThat(result.getId()).isSameAs(model.getId());
        Assertions.assertThat(result.getName()).isSameAs(model.getName());
    }

    @Test
    void test_get_by_id_when_not_found_ingredient() {
        IngredientModel model = getIngredientModel("Egg");

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> underTest.getById(model.getId()));
    }

    @Test
    void test_list_ingredient() {
        IngredientModel model = getIngredientModel("Egg");
        Mockito.when(repository.findAll(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(List.of(model)));

        List<IngredientResponse> result = underTest.list(0, 1);
        Assertions.assertThat(result.size()).isSameAs(1);
    }

    @Test
    void test_get_ingredients_by_ids(){
        IngredientModel model = getIngredientModel("Egg");

        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(model));

        List<IngredientModel> result=underTest.getIngredientsByIds(List.of(1));
        Assertions.assertThat(result.size()).isSameAs(1);
    }


    private static IngredientModel getIngredientModel(String name) {
        return IngredientModel
                .builder()
                .id(1)
                .name(name)
                .build();
    }

    private static IngredientRequest getIngredientRequest(String name) {
        return IngredientRequest
                .builder()
                .name(name)
                .build();
    }
}