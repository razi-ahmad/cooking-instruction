package nl.abnamro.recipe.service;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipe.dto.IngredientRequest;
import nl.abnamro.recipe.dto.IngredientResponse;
import nl.abnamro.recipe.eception.NotFoundException;
import nl.abnamro.recipe.model.IngredientModel;
import nl.abnamro.recipe.repository.IngredientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IngredientService implements IIngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public IngredientResponse create(IngredientRequest request) {
        IngredientModel model = map(request);
        return map(repository.save(model));
    }

    public IngredientResponse update(Integer id, IngredientRequest request) {
        IngredientModel model;
        try {
            model = fetchById(id);
        } catch (NotFoundException exception) {
            log.warn("Ingredient not found by id : {}", id);
            return create(request);
        }
        model.setName(request.getName());
        return map(repository.save(model));
    }

    public void delete(int id) {
        if (!repository.existsById(id)) {
            log.warn("ingredient not found by id:{}", id);
            throw new NotFoundException("Ingredient not found");
        }
        repository.deleteById(id);
    }


    public IngredientResponse getById(int id) {
        return map(fetchById(id));
    }


    private IngredientModel fetchById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));
    }

    public List<IngredientModel> getIngredientsByIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::fetchById)
                .collect(Collectors.toList());
    }

    public List<IngredientResponse> list(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return map(repository.findAll(pageRequest).getContent());
    }

    public List<IngredientResponse> map(List<IngredientModel> ingredients) {
        return ingredients
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private IngredientResponse map(IngredientModel ingredient) {
        return IngredientResponse
                .builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .build();
    }

    private IngredientModel map(IngredientRequest request) {
        return IngredientModel.
                builder()
                .name(request.getName())
                .build();
    }
}
