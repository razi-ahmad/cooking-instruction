package nl.abnamro.recipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "instruction")
public class IngredientModel extends BaseModel {

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @JsonIgnoreProperties("recipes")
    @ManyToMany(mappedBy = "ingredients", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<RecipeModel> recipes;
}
