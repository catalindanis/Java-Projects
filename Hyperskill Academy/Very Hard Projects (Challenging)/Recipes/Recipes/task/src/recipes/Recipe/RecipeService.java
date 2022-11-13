package recipes.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;
    EntityManager entityManager;
    public void saveRecipe(Recipe recipe){
        try {
            recipe.setRecipeOwner(SecurityContextHolder.getContext().getAuthentication().getName());
            recipeRepository.save(recipe);
        }
        catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    public String getRecipe(long id){
        Optional<Recipe> value = recipeRepository.findById(id);
        if(!value.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return value.get().toString();
    }
    public void deleteRecipe(long id){
        Optional<Recipe> value = recipeRepository.findById(id);
        if(!value.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if(value.get().getRecipeOwner().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            recipeRepository.deleteById(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    public void updateRecipe(long id, Recipe recipe){
        try {
            Recipe obj = recipeRepository.findById(id).get();

            if(!obj.getRecipeOwner().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);

            obj.setName(recipe.getName());
            obj.setCategory(recipe.getCategory());
            obj.setDate(LocalDateTime.now());
            obj.setDescription(recipe.getDescription());
            obj.setIngredients(recipe.getIngredients());
            obj.setDirections(recipe.getDirections());
            saveRecipe(obj);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }catch (NoSuchElementException noSuchElementException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public String sortByCategory(String category){
        List<Recipe> values = recipeRepository.findAll();
        values.sort((recipe1,recipe2) -> {
            return recipe2.getDate().compareTo(recipe1.getDate());
        });
        StringBuilder result = new StringBuilder();
        try {
            for (Recipe currentRecipe : values)
                if (currentRecipe.getCategory().trim().toLowerCase().equals(category.trim().toLowerCase())
                        && currentRecipe != null)
                    result.append(currentRecipe.toString() + ",\n");
            result.deleteCharAt(result.length() - 2);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException){}
        return String.format("[\n%s\n]",result.toString());
    }

    public String sortByName(String name){
        List<Recipe> values = recipeRepository.findAll();
        values.sort((recipe1,recipe2) -> {
            return recipe2.getDate().compareTo(recipe1.getDate());
        });
        StringBuilder result = new StringBuilder();
        try {
            for (Recipe currentRecipe : values)
                if (currentRecipe.getName().trim().toLowerCase().contains(name.trim().toLowerCase()))
                    result.append(currentRecipe.toString() + ",\n");
            result.deleteCharAt(result.length() - 2);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException){}
        return String.format("[\n%s\n]",result.toString());
    }
}
