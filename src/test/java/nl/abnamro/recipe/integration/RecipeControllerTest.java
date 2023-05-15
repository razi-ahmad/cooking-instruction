package nl.abnamro.recipe.integration;

import nl.abnamro.recipe.dto.FilterRequest;
import nl.abnamro.recipe.dto.RecipeRequest;
import nl.abnamro.recipe.dto.RecipeResponse;
import nl.abnamro.recipe.enums.Category;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.model.RecipeModel;
import nl.abnamro.recipe.repository.IngredientRepository;
import nl.abnamro.recipe.repository.RecipeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerTest extends BaseControllerTest {
    private static final String BASE_PATH = "/api/v1/recipes";

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Before
    public void before() {
        repository.deleteAll();
    }

    @Test
    public void test_create_recipe_successfully() throws Exception {
        RecipeRequest request = createRecipeRequest();
        MvcResult result = performPost(BASE_PATH, request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.id");
        assertTrue(repository.findById(id).isPresent());
    }

    @Test
    public void test_update_recipe_successfully() throws Exception {
        RecipeModel model = createRecipe();
        RecipeModel entity = repository.save(model);

        RecipeRequest request = RecipeRequest.builder().name("pasta").instruction("update instruction").build();

        MvcResult result = performPut(BASE_PATH + "/" + entity.getId(), request)
                .andExpect(status().isOk()).andReturn();

        assertEquals(request.getName(), readByJsonPath(result, "$.name"));
        assertEquals(request.getInstruction(), readByJsonPath(result, "$.instruction"));
    }

    @Test
    public void test_update_recipe_when_id_is_null() throws Exception {
        RecipeRequest request = createRecipeRequest();

        performPut(BASE_PATH + "/5", request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    public void test_deleteRecipe_successfully() throws Exception {
        RecipeModel model = createRecipe();
        RecipeModel entity = repository.save(model);

        performDelete(BASE_PATH + "/" + entity.getId())
                .andExpect(status().isOk());

        Optional<RecipeModel> deletedRecipe = repository.findById(entity.getId());

        assertTrue(deletedRecipe.isEmpty());
    }

    @Test
    public void test_deleteRecipe_notFound() throws Exception {
        performDelete(BASE_PATH + "/1")
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_recipe_successfully() throws Exception {
        RecipeModel model = createRecipe();
        RecipeModel entity = repository.save(model);

        performGet(BASE_PATH + "/" + entity.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(entity.getName()));
    }

    @Test
    public void test_get_recipe_notFound() throws Exception {
        performGet(BASE_PATH + "/1")
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_list_recipes_successfully() throws Exception {
        RecipeModel model = createRecipe();
        List<RecipeModel> models = List.of(model);

        repository.saveAll(models);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("limit", "10");
        MvcResult result = performGet(BASE_PATH, params)
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponse> recipes = getListFromMvcResult(result, RecipeResponse.class);
        assertEquals(models.size(), recipes.size());
        assertEquals(models.get(0).getName(), recipes.get(0).getName());
    }

    @Test
    public void test_search_recipe_by_criteria_successfully() throws Exception {
        IngredientModel ingredientModel = createIngredient("salt");
        IngredientModel ingredientEntity = ingredientRepository.save(ingredientModel);

        RecipeRequest recipeRequest = new RecipeRequest("pasta", "past instruction",
                Category.VEGAN, List.of(ingredientEntity.getId()));

        MvcResult recipe = performPost(BASE_PATH, recipeRequest)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(recipe, "$.id");

        List<FilterRequest> request = new ArrayList<>();
        FilterRequest searchCriteria = new FilterRequest("ingredients.name", "salt", "like");
        request.add(searchCriteria);


        MvcResult result = performGet(BASE_PATH + "/filter?page=0&limit=10", request)
                .andExpect(status().isOk())
                .andReturn();

        Optional<RecipeModel> optionalRecipe = repository.findById(id);


        List<RecipeResponse> listRecipeList = getListFromMvcResult(result, RecipeResponse.class);
        Assert.assertTrue(optionalRecipe.isPresent());
        Assert.assertEquals(listRecipeList.get(0).getName(), optionalRecipe.get().getName());
        Assert.assertEquals(listRecipeList.get(0).getInstruction(), optionalRecipe.get().getInstruction());
    }

    //@Test
    public void test_SearchRecipeByCriteria_fails() throws Exception {
        performPost(BASE_PATH + "/filter", null)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").exists())
                .andReturn();
    }

    static List<IngredientModel> createIngredientList(boolean withId) {
        IngredientModel model1 = IngredientModel
                .builder()
                .id(withId ? 10 : null)
                .name("tomato")
                .build();

        IngredientModel model2 = IngredientModel.builder()
                .id(withId ? 11 : null)
                .name("cabbage")
                .build();

        return List.of(model1, model2);
    }

    static IngredientModel createIngredient(String name) {
        return IngredientModel.builder()
                .name(name)
                .build();
    }

    static RecipeModel createRecipe(Integer id) {
        return RecipeModel
                .builder()
                .id(id)
                .name("pasta")
                .category(Category.VEGAN)
                .instruction("someInstruction").build();

    }

    static RecipeModel createRecipe() {
        return RecipeModel
                .builder()
                .name("pizza")
                .instruction("pizza instruction")
                .category(Category.VEGETARIAN).build();

    }

    static RecipeRequest createRecipeRequest() {
        return RecipeRequest
                .builder()
                .name("pizza")
                .instruction("pizza instruction")
                .category(Category.VEGAN)
                .build();
    }
}
