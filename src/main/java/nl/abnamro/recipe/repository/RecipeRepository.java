package nl.abnamro.recipe.repository;

import nl.abnamro.recipe.model.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel, Integer>, JpaSpecificationExecutor<RecipeModel> {

}