package recipes.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeController {
    @Autowired
    RecipeService recipeService = new RecipeService();

    @PostMapping("/api/recipe/new")
    public String postRecipe(@Validated @RequestBody Recipe recipe){
        recipeService.saveRecipe(recipe);
        return String.format("{\n" +
                "\"id\":%d" +
                "\n}",recipe.getId());
    }

    @GetMapping(value = "/api/recipe/search", params = "category")
    public String getRecipeByCategory(@RequestParam(name="category") String category){
        System.out.println(category);
        return recipeService.sortByCategory(category);
    }

    @GetMapping(value = "/api/recipe/search", params = "name")
    public String getRecipeByName(@RequestParam(name="name") String name){
        return recipeService.sortByName(name);
    }

    @GetMapping("/api/recipe/{id}")
    public String getRecipe(@PathVariable Long id){
        return recipeService.getRecipe(id);
    }

    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@PathVariable Long id){
        recipeService.deleteRecipe(id);
    }

    @PutMapping("/api/recipe/{id}")
    public void updateRecipe(@PathVariable long id, @Validated @RequestBody Recipe recipe){ recipeService.updateRecipe(id,recipe);}
}
