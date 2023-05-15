package nl.abnamro.recipe.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipe.dto.FilterRequest;
import nl.abnamro.recipe.dto.RecipeRequest;
import nl.abnamro.recipe.dto.RecipeResponse;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = {"Recipe Controller"})
@RestController
@RequestMapping(value = "/api/v1/recipes")
public class RecipeController {

    private final RecipeService service;

    @Autowired
    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @Operation(summary = "The endpoint create new recipe")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recipe successfully created."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"Name can not be empty.\"}")}
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"500 Internal Server Error\"}")}
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RecipeResponse> creatRecipe(@Valid @RequestBody RecipeRequest request) {
        log.info("creatRecipe: {} >>>> started", request);
        RecipeResponse response = service.create(request);
        log.info("creatRecipe: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "The endpoint update recipe if exist otherwise create")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recipe updated or successfully created."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"Name can not be empty.\"}")}
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"500 Internal Server Error\"}")}
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable(name = "id") Integer id, @Valid @RequestBody RecipeRequest request) {
        log.info("updateRecipe with id{}:  >>>> started", id);
        try {
            service.getById(id);
        } catch (NotFoundException ex) {
            log.warn("Recipe not found by id: {}", id);
            return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
        }

        RecipeResponse response = service.update(id, request);
        log.info("updateRecipe: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "The endpoint delete the recipe by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"Recipe not found.\"}")}
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"500 Internal Server Error\"}")}
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable(name = "id") Integer id) {
        log.info("deleteRecipe with id{}:  >>>> started", id);
        service.delete(id);
        log.info("deleteRecipe:  >>>> finished");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "The endpoint return the recipe by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(value = "{\"message\":\"500 Internal Server Error\"}")
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable(name = "id") Integer id) {
        log.info("getRecipe with id{}:  >>>> started", id);
        RecipeResponse response = service.getById(id);
        log.info("getRecipe: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "The endpoint return the list of recipe by paging")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"500 Internal Server Error\"}")}
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getRecipes(@RequestParam(value = "page", required = false) @Valid Integer page,
                                                           @RequestParam(value = "limit", required = false) @Valid Integer limit) {
        log.info("getRecipes page{}, limit{}:  >>>> started", page, limit);
        List<RecipeResponse> response = service.list(page, limit);
        log.info("getRecipes :  >>>> finished");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RecipeResponse>> getFilteredRecipes(@RequestBody List<FilterRequest> request, @RequestParam(value = "page", required = true) @Valid Integer page,
                                                                   @RequestParam(value = "limit", required = true) @Valid Integer limit) {
        log.info("getFilteredRecipes page{}, limit{}:  >>>> started", page, limit);
        List<RecipeResponse> response = service.listFiltered(request, null, page, limit);
        log.info("getFilteredRecipes :  >>>> finished");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
