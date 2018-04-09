
package recipesearch;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeSearch extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        ResourceBundle bundle = java.util.ResourceBundle.getBundle("recipesearch/resources/RecipeSearch");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("recipe_search.fxml"), bundle);
        loader.load();
        Parent root = loader.getRoot();

        Scene scene = new Scene(root, 800, 500);

        RecipeSearchController recipeSearchController = (RecipeSearchController) loader.getController();
        recipeSearchController.init();

        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
