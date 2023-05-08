package nl.abnamro.recipe.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.abnamro.recipe.enums.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    @NotBlank(message = "Name can not be empty")
    @Size(max = 255, message = "Maximum length can be 255 character")
    @ApiModelProperty(notes = "Name of the recipe", example = "Pizza")
    private String name;

    @NotBlank(message = "Instruction can not be empty")
    @Size(max = 255, message = "Maximum length can be 255 character")
    @ApiModelProperty(notes = "Instruction of the recipe", example = "Boil egg: Place in water, boil for 9-12 min. Remove, cool in cold water for 30s. Crack, peel, enjoy!")
    private String instruction;

    @ApiModelProperty(notes = "Category of the recipe", example = "VEGAN")
    private Category category;

    @ApiModelProperty(notes = "The ids of the ingredients", example = "[1001,1002]")
    private List<Integer> ingredientIds;


}
