package nl.abnamro.recipe.integration;

import nl.abnamro.recipe.dto.IngredientRequest;
import nl.abnamro.recipe.dto.IngredientResponse;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.repository.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientControllerTest extends BaseControllerTest {
    private static final String BASE_PATH = "/api/v1/ingredients";
    @Autowired
    private IngredientRepository repository;


    @Before
    public void before() {
        repository.deleteAll();
    }


    @Test
    public void test_create_ingredient_successfully() throws Exception {
        IngredientRequest request = IngredientRequest.builder().name("salt").build();

        MvcResult result = performPost(BASE_PATH, request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.id");

        Optional<IngredientModel> ingredient = repository.findById(id);

        assertTrue(ingredient.isPresent());
        assertEquals(ingredient.get().getName(), request.getName());
    }

    @Test
    public void test_update_ingredient_successfully() throws Exception {
        IngredientModel model = IngredientModel.builder().name("salt").build();
        IngredientModel entity = repository.save(model);

        IngredientRequest request = IngredientRequest.builder().name("salt").build();

        MvcResult result = performPut(BASE_PATH + "/" + entity.getId(), request)
                .andExpect(status().isOk()).andReturn();

        assertEquals(request.getName(), readByJsonPath(result, "$.name"));
    }

    @Test
    public void test_delete_ingredient_successfully() throws Exception {
        IngredientModel model = IngredientModel.builder().name("salt").build();
        IngredientModel entity = repository.save(model);

        performDelete(BASE_PATH + "/" + entity.getId())
                .andExpect(status().isNoContent());

        Optional<IngredientModel> result = repository.findById(entity.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    public void test_delete_ingredient_notFound() throws Exception {

        performDelete(BASE_PATH + "/1")
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_ingredient_successfully() throws Exception {
        IngredientModel model = IngredientModel.builder().name("salt").build();
        IngredientModel entity = repository.save(model);

        performGet(BASE_PATH + "/" + entity.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(model.getName()));
    }

    @Test
    public void test_get_ingredient_notFound() throws Exception {
        performGet(BASE_PATH + "/1")
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_ingredient_by_id_successfully() throws Exception {
        IngredientModel model = IngredientModel.builder().name("salt").build();
        IngredientModel entity = repository.save(model);

        MvcResult result = performGet(BASE_PATH + "/" + entity.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andReturn();

        IngredientResponse ingredientResponse = getFromMvcResult(result, IngredientResponse.class);
        assertEquals(entity.getName(), ingredientResponse.getName());
    }

    @Test
    public void test_list_ingredients_successfully() throws Exception {
        List<IngredientModel> ingredientList = List.of(IngredientModel.builder().name("salt").build());
        repository.saveAll(ingredientList);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("limit", "10");
        MvcResult result = performGet(BASE_PATH, params)
                .andExpect(status().isOk())
                .andReturn();

        List<IngredientResponse> responses = getListFromMvcResult(result, IngredientResponse.class);
        assertEquals(ingredientList.size(), responses.size());
    }
}
