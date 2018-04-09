
package recipesearch;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class RecipeSearchController {

    @FXML
    private AnchorPane searchPane;

    @FXML
    private FlowPane recipeListFlowPane;

    private RecipeBackendController recipeBackendController;

    public void init() {
        recipeBackendController = new RecipeBackendController();
        updateRecipeList();
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        recipeBackendController.getRecipes().forEach(recipe -> recipeListFlowPane.getChildren().add(new RecipeListItem(recipe, this)));
    }
}