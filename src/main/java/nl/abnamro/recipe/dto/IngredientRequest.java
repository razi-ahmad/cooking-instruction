package nl.abnamro.recipe.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
    @NotBlank(message = "Name can not be empty")
    @Size(max = 255, message = "Maximum length of the characters")
    @ApiModelProperty(notes = "Name of the ingredient", example = "Egg")
    private String name;
}
