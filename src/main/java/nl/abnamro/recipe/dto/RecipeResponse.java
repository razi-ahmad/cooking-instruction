package nl.abnamro.recipe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import nl.abnamro.recipe.enums.Category;

import java.util.List;

@Data
@Builder
public class RecipeResponse {

    @ApiModelProperty(notes = "Id of the recipe", example = "10001")
    private int id;

    @ApiModelProperty(notes = "Name of the recipe", example = "Pizza")
    private String name;

    @ApiModelProperty(notes = "Instruction of the recipe", example = "Boil egg: Place in water, boil for 9-12 min. Remove, cool in cold water for 30s. Crack, peel, enjoy!")
    private String instruction;

    @ApiModelProperty(notes = "Category of the recipe", example = "VEGAN")
    private Category category;

    @JsonIgnoreProperties("ingredients")
    private List<IngredientResponse> ingredients;
}
