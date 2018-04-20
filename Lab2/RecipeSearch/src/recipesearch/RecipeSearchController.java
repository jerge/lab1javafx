package recipesearch;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecipeSearchController {

    public RecipeSearchController(){
        nameToImageMap.put("Kött", getImage("RecipeSearch/resources/icon_main_meat.png"));
        nameToImageMap.put("Fisk", getImage("RecipeSearch/resources/icon_main_fish.png"));
        nameToImageMap.put("Kyckling", getImage("RecipeSearch/resources/icon_main_chicken.png"));
        nameToImageMap.put("Vegetarisk", getImage("RecipeSearch/resources/icon_main_veg.png"));

        nameToImageMap.put("Sverige", getImage("RecipeSearch/resources/icon_flag_sweden.png"));
        nameToImageMap.put("Grekland", getImage("RecipeSearch/resources/icon_flag_greece.png"));
        nameToImageMap.put("Indien", getImage("RecipeSearch/resources/icon_flag_india.png"));
        nameToImageMap.put("Asien", getImage("RecipeSearch/resources/icon_flag_asia.png"));
        nameToImageMap.put("Afrika", getImage("RecipeSearch/resources/icon_flag_africa.png"));
        nameToImageMap.put("Frankrike", getImage("RecipeSearch/resources/icon_flag_france.png"));
    }

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
    private ImageView closeImageView;

    @FXML
    private Label recipeDetailName;

    @FXML
    private ImageView recipeDetailImageView;

    private RecipeBackendController recipeBackendController;

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<>();
    private Map<String, Image> nameToImageMap = new HashMap<>();
    public void init() {
        recipeBackendController = new RecipeBackendController();
        recipeBackendController.getRecipes().forEach(recipe -> {
            RecipeListItem recipelistItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipelistItem);
        });

        initComboBox(
                mainIngredientController,
                "Visa alla",
                (observable, oldValue, newValue) -> {
                    recipeBackendController.setMainIngredient(newValue);
                    updateRecipeList();
                },
                "Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk"
        );

        populateMainIngredientComboBox();

        initComboBox(
                countryController,
                "",
                (observable, oldValue, newValue) -> {
                    recipeBackendController.setCuisine(newValue);
                    updateRecipeList();
                },
                "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike"
        );

        populateCountryComboBox();

        initRadioButtons(difficultyAnyController, difficultyEasyController, difficultyMediumController, difficultyHardController);

        initSpinner(
                maxPriceController,
                0,
                100,
                100,
                1);

        initSlider(maxTimeController, minutesLabel);

        initRecipeDetailPane(recipeDetailPane);

        updateRecipeList();

        Platform.runLater(() -> mainIngredientController.requestFocus());
    }

    private boolean detailOpen = false;

    @FXML
    public void onClickedStackPane(Event e){
        System.out.println("stack");
        if(detailOpen) {
            closeRecipeDetail();
        }
    }

    @FXML
    public void onCloseRecipeDetailButton() {
        closeRecipeDetail();
    }

    private void closeRecipeDetail(){
        if(!detailOpen){
            return;
        }
        detailOpen = false;
        searchPane.toFront();
        searchPane.getStyleClass().remove("dim");
    }

    public void openRecipeView(Recipe recipe) {
        if(detailOpen){
            closeRecipeDetail();
            return;
        }
        detailOpen = true;
        populateRecipeDetailView(recipe);
        searchPane.getStyleClass().add("dim");
        recipeDetailPane.toFront();
    }

    private void populateRecipeDetailView(Recipe recipe) {
        recipeDetailName.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
    }

    private void initRecipeDetailPane(AnchorPane pane) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_se.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void initComboBox(ComboBox<T> comboBox, T selected, ChangeListener<T> changeListener, T... objects) {
        comboBox.getItems().addAll(objects);
        comboBox.getSelectionModel().select(selected);
        comboBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void initRadioButtons(RadioButton... radioButtons) {
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtons[0].setSelected(true);

        for (RadioButton radioButton : radioButtons) {
            radioButton.setToggleGroup(toggleGroup);
        }

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                recipeBackendController.setDifficulty(selectedRadioButton.getText());
                updateRecipeList();
            }
        });
    }

    private void initSpinner(Spinner<Integer> spinner, int min, int max, int initialValue, int amountToStepBy) {
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
            if (!newValue) {
                Integer value = Integer.valueOf(spinner.getEditor().getText());
                recipeBackendController.setMaxPrice(value);
                updateRecipeList();
            }
        });
    }

    private void initSlider(Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                recipeBackendController.setMaxTime(newValue.intValue());
                label.setText("Max minutes: " + newValue.intValue());
                updateRecipeList();
            }
        });

        slider.valueProperty().set(150);
    }

    private void updateRecipeList() {
        recipeListFlowPane.getChildren().clear();
        recipeBackendController.getRecipes().forEach(recipe -> recipeListFlowPane.getChildren().add(recipeListItemMap.get(recipe.getName())));
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon;
                            String iconPath;
                            icon = nameToImageMap.get(item);
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientController.setButtonCell(cellFactory.call(null));
        mainIngredientController.setCellFactory(cellFactory);
    }

    private void populateCountryComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            icon = nameToImageMap.get(item);
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        countryController.setButtonCell(cellFactory.call(null));
        countryController.setCellFactory(cellFactory);
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }

    public Image getIconImage(String name){
        return nameToImageMap.get(name);
    }

}