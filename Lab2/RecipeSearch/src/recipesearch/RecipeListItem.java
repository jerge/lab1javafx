package recipesearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {

    @FXML
    private ImageView recipeImageView, countryFlagImageView, mainIngredientImageView, difficultyImageView;

    @FXML
    private Label recipeNameLabel, minutesCookingLabel, costLabel;


    private RecipeSearchController parentController;
    private Recipe recipe;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        recipeImageView.setImage(recipe.getFXImage());
        countryFlagImageView.setImage(recipeSearchController.getIconImage(recipe.getCuisine()));
        mainIngredientImageView.setImage(recipeSearchController.getIconImage(recipe.getMainIngredient()));
        difficultyImageView.setImage(getDifficultyImage(recipe.getDifficulty()));
        recipeNameLabel.setText(recipe.getName());
        minutesCookingLabel.setText(recipe.getTime() + " minuter");
        costLabel.setText(recipe.getPrice() + " kr");

        super.setOnMouseClicked(event -> {
            event.consume();
            parentController.openRecipeView(recipe);
        });
    }

    private Image getDifficultyImage(String difficulty){
        switch(difficulty){
            case "Lätt":
                return getImage("RecipeSearch/resources/icon_difficulty_easy.png");
            case "Mellan":
                return getImage("RecipeSearch/resources/icon_difficulty_medium.png");
            case "Svår":
                return getImage("RecipeSearch/resources/icon_difficulty_hard.png");
        }
        return null;
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

}
