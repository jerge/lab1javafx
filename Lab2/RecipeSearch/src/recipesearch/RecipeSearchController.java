
package recipesearch;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeSearchController {

    @FXML
    private ComboBox<String> mainIngredientController, countryController;

    @FXML
    private RadioButton difficultyAnyController, difficultyEasyController, difficultyMediumController, difficultyHardController;

    @FXML
    private Spinner<Integer> maxPriceController;

    @FXML
    private Slider maxTimeController;

    @FXML
    private StackPane searchPane;

    @FXML
    private AnchorPane recipeDetailPane;

    @FXML
    private FlowPane recipeListFlowPane;

    @FXML
    private Label minutesLabel;

    /*Detail*/

    @FXML
    private Label recipeDetailName;

    @FXML
    private ImageView recipeDetailImageView;

    private RecipeBackendController recipeBackendController;

    public void init() {
        recipeBackendController = new RecipeBackendController();

        initComboBox(
                mainIngredientController,
                "Visa alla",
                (observable, oldValue, newValue) -> {
                    recipeBackendController.setMainIngredient(newValue);
                    updateRecipeList();
                },
                "Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk"
        );

        initComboBox(
                countryController,
                "",
                (observable, oldValue, newValue) -> {
                    recipeBackendController.setCuisine(newValue);
                    updateRecipeList();
                },
                "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike"
        );

        initRadioButtons(difficultyAnyController, difficultyEasyController, difficultyMediumController, difficultyHardController);

        initSpinner(
                maxPriceController,
                0,
                100,
                100,
                1);

        initSlider(maxTimeController, minutesLabel);

        initRecipeDetailPane(recipeDetailPane, "recipe_se.fxml");

        updateRecipeList();
    }

    @FXML
    public void onCloseRecipeDetailButton(){
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

    private void populateRecipeDetailView(Recipe recipe){
        recipeDetailName.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
    }

    private void initRecipeDetailPane(AnchorPane pane, String recipeDetailFXMLResource){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(recipeDetailFXMLResource));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void initComboBox(ComboBox<T> comboBox, T selected, ChangeListener<T> changeListener, T... objects){
        comboBox.getItems().addAll(objects);
        comboBox.getSelectionModel().select(selected);
        comboBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void initRadioButtons(RadioButton... radioButtons){
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtons[0].setSelected(true);

        for(RadioButton radioButton : radioButtons){
            radioButton.setToggleGroup(toggleGroup);
        }

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(toggleGroup.getSelectedToggle() != null){
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                recipeBackendController.setDifficulty(selectedRadioButton.getText());
                updateRecipeList();
            }
        });
    }

    private void initSpinner(Spinner<Integer> spinner, int min, int max, int initialValue, int amountToStepBy){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                min,
                max,
                initialValue,
                amountToStepBy
        );

        spinner.setValueFactory(valueFactory);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            recipeBackendController.setMaxPrice(newValue);
            updateRecipeList();
        });

        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                Integer value = Integer.valueOf(spinner.getEditor().getText());
                recipeBackendController.setMaxPrice(value);
                updateRecipeList();
            }
        });
    }

    private void initSlider(Slider slider, Label label){
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals(oldValue)) {
                recipeBackendController.setMaxTime(newValue.intValue());
                label.setText("Max minutes: " + newValue.intValue());
                updateRecipeList();
            }
        });

        slider.valueProperty().set(150);
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        recipeBackendController.getRecipes().forEach(recipe -> recipeListFlowPane.getChildren().add(new RecipeListItem(recipe, this)));
    }
}