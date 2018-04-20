package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.List;

public class RecipeBackendController {

    private RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private String cuisine = null;
    private String mainIngredient = null;
    private String difficulty = null;
    private int maxPrice = 0;
    private int maxTime = 0;

    public List<Recipe> getRecipes() {
        cuisine = cuisine == null ? "" : cuisine; //Make sure cuisine is not null
        return db.search(new SearchFilter(difficulty, maxTime, cuisine.equals("Visa alla") ? "" : cuisine, maxPrice, mainIngredient));
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
}
