package nl.abnamro.recipe.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipe.dto.IngredientRequest;
import nl.abnamro.recipe.dto.IngredientResponse;
import nl.abnamro.recipe.exception.NotFoundException;
import nl.abnamro.recipe.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = {"Ingredient Controller"})
@RestController
@RequestMapping(value = "/api/v1/ingredients")
public class IngredientController {

    private final IngredientService service;

    @Autowired
    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @Operation(summary = "The endpoint create new ingredient")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ingredient successfully created."
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
    public ResponseEntity<IngredientResponse> creatIngredient(@Valid @RequestBody IngredientRequest request) {
        log.info("creatIngredient: {} >>>> started", request);
        IngredientResponse response = service.create(request);
        log.info("creatIngredient: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "The endpoint update ingredient if exist otherwise create")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ingredient updated or successfully created."
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
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable(name = "id") Integer id, @Valid @RequestBody IngredientRequest request) {
        log.info("updateIngredient with id{}:  >>>> started", id);
        IngredientResponse response = service.update(id, request);
        log.info("updateIngredient: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "The endpoint delete the ingredient by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(value = "{\"message\":\"Ingredient not found.\"}")}
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
    public ResponseEntity<Object> deleteIngredient(@PathVariable(name = "id") Integer id) {
        log.info("deleteIngredient with id{}:  >>>> started", id);
        service.delete(id);
        log.info("deleteIngredient:  >>>> finished");
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "The endpoint return the ingredient by id")
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
    public ResponseEntity<IngredientResponse> getIngredient(@PathVariable(name = "id") Integer id) {
        log.info("getIngredient with id{}:  >>>> started", id);
        IngredientResponse response = service.getById(id);
        log.info("getIngredient: {} >>>> finished", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "The endpoint return the list of ingredient by paging")
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
    public ResponseEntity<List<IngredientResponse>> getIngredients(@RequestParam(value = "page", required = false) @Valid Integer page,
                                                   @RequestParam(value = "limit", required = false) @Valid Integer limit) {
        log.info("getIngredients page{}, limit{}:  >>>> started", page, limit);
        List<IngredientResponse> response = service.list(page, limit);
        log.info("getIngredients :  >>>> finished");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
