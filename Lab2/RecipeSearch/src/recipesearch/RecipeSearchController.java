
package recipesearch;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

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
    private AnchorPane searchPane;

    @FXML
    private FlowPane recipeListFlowPane;

    @FXML
    private Label minutesLabel;

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

        updateRecipeList();
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