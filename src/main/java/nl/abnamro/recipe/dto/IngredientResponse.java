package nl.abnamro.recipe.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponse {

    @ApiModelProperty(notes = "Id of the ingredient", example = "10001")
    private int id;

    @ApiModelProperty(notes = "Name of the ingredient", example = "Potato")
    private String name;
}
