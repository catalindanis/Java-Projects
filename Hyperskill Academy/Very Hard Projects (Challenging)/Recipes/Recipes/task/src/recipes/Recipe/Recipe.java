package recipes.Recipe;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import recipes.User.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Recipes")
@AllArgsConstructor
@Data
public class Recipe {
    public Recipe(){};
    @Id
    @Column(name="recipes_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @NotBlank
    @NotEmpty
    @NonNull
    @Column(name="recipes_NAME")
    private String name;
    @NotNull
    @NotBlank
    @NotEmpty
    @NonNull
    @Column(name="recipes_CATEGORY")
    private String category;
    @Column(name="recipes_DATE")
    private LocalDateTime date = LocalDateTime.now();
    @NotNull
    @NotBlank
    @NotEmpty
    @NonNull
    @Column(name="recipes_DESCRIPTION")
    private String description;

    @Size(min=1)
    @NotNull
    @NonNull
    @NotEmpty
    @OneToMany(
            cascade = CascadeType.ALL
    )
    private List<Ingredient> ingredients;

    @Size(min=1)
    @NotNull
    @NonNull
    @NotEmpty
    @OneToMany(
            cascade = CascadeType.ALL
    )
    private List<Direction> directions;

    private String recipeOwner;

    @Override
    public String toString(){
        return String.format("{\n" +
                "\"name\": \"%s\",\n" +
                "\"category\": \"%s\",\n" +
                "\"date\": \"%s\",\n" +
                "\"description\": \"%s\",\n" +
                "\"ingredients\": %s,\n" +
                "\"directions\": %s" +
                "\n}",this.getName(), this.getCategory(), this.getDate(), this.getDescription(),
                this.getIngredients(), this.getDirections());
    }

    @Override
    public int hashCode(){
        return Math.abs(name.hashCode()+description.hashCode()+ingredients.hashCode()+directions.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof Recipe))
            return false;
        Recipe that = (Recipe) obj;
        return this.name.equals(that.name) || this.description.equals(that.description) ||
                this.ingredients.equals(that.ingredients) || this.directions.equals(that.directions);
    }
}
@Entity
@Data
@AllArgsConstructor
class Ingredient{
    public Ingredient(){};
    @Id
    @Column(name="ingredient_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @NonNull
    @Column(name="ingredient_NAME")
    private String name;

    @ManyToOne
    private Recipe recipes;

    public Ingredient(String name){
        this.name = name;
        //this.id = hashCode();
    }

    @Override
    public String toString(){
        return String.format("" +
                "\"%s\"",this.name);
    }

    @Override
    public int hashCode(){
        return Math.abs(name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof Ingredient))
            return false;
        Ingredient that = (Ingredient) obj;
        return this.name.equals(that.name);
    }
}
@Entity
@Data
@AllArgsConstructor
class Direction{
    public Direction(){};
    @Id
    @Column(name="direction_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @NonNull
    @Column(name="direction_NAME")
    private String name;

    @ManyToOne
    private Recipe recipes;

    public Direction(String name){
        this.name = name;
        //this.id = hashCode();
    }

    @Override
    public String toString(){
        return String.format("" +
                "\"%s\"",this.name);
    }

    @Override
    public int hashCode(){
        return Math.abs(name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof Direction))
            return false;
        Direction that = (Direction) obj;
        return this.name.equals(that.name);
    }
}
