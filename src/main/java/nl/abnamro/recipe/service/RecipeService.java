package nl.abnamro.recipe.service;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipe.dto.FilterRequest;
import nl.abnamro.recipe.dto.RecipeRequest;
import nl.abnamro.recipe.dto.RecipeResponse;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.model.RecipeModel;
import nl.abnamro.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeService implements IRecipeService {

    private final RecipeRepository repository;
    private final IngredientService ingredientService;
    private final ISearchRecipeService searchService;


    @Autowired
    RecipeService(RecipeRepository repository, IngredientService ingredientService, ISearchRecipeService searchService) {
        this.repository = repository;
        this.ingredientService = ingredientService;
        this.searchService = searchService;
    }

    @Override
    public RecipeResponse create(RecipeRequest request) {
        return map(repository.save(map(request)));
    }

    @Override
    public RecipeResponse update(Integer id, RecipeRequest request) {
        RecipeModel model = fetchById(id);
        model.setName(request.getName());
        model.setInstruction(request.getInstruction());
        model.setCategory(request.getCategory());
        model.setIngredients(getIngredientModels(request));
        return map(repository.save(model));
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            log.warn("recipe not found by id: {}", id);
            throw new NotFoundException("Recipe not found");
        }
        repository.deleteById(id);

    }

    @Override
    public RecipeResponse getById(Integer id) {
        return map(fetchById(id));
    }

    public List<RecipeResponse> listFiltered(List<FilterRequest> request, String joinType, int page, int limit) {
        return map(searchService.searchRecipe(request, joinType, page, limit));
    }

    private RecipeModel fetchById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
    }

    @Override
    public List<RecipeResponse> list(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return map(repository.findAll(pageRequest).getContent());
    }

    private List<RecipeResponse> map(List<RecipeModel> recipes) {
        return recipes
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private RecipeResponse map(RecipeModel recipe) {
        return RecipeResponse
                .builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .category(recipe.getCategory())
                .instruction(recipe.getInstruction())
                .ingredients(ingredientService.map(recipe.getIngredients()))
                .build();
    }

    private RecipeModel map(RecipeRequest request) {
        List<IngredientModel> ingredients = getIngredientModels(request);
        return RecipeModel
                .builder()
                .name(request.getName())
                .instruction(request.getInstruction())
                .category(request.getCategory())
                .ingredients(ingredients)
                .build();
    }

    private List<IngredientModel> getIngredientModels(RecipeRequest request) {
        return Optional
                .ofNullable(request.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);
    }
}
