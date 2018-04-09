
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.Presenter;


public class AddressBookController implements Initializable {
    
    @FXML private MenuBar menuBar;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private ListView contactsList;
    @FXML private TextField fnameTextField;
    @FXML private TextField lnameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postcodeTextField;
    @FXML private TextField cityTextField;

    private Presenter presenter;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter(
                contactsList,
                fnameTextField,
                lnameTextField,
                phoneTextField,
                emailTextField,
                addressTextField,
                postcodeTextField,
                cityTextField
        );
        presenter.init();

        contactsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                presenter.contactsListChanged();
            }

        });

        fnameTextField.focusedProperty().addListener(new TextFieldListener(fnameTextField));
        lnameTextField.focusedProperty().addListener(new TextFieldListener(lnameTextField));
        phoneTextField.focusedProperty().addListener(new TextFieldListener(phoneTextField));
        emailTextField.focusedProperty().addListener(new TextFieldListener(emailTextField));
        addressTextField.focusedProperty().addListener(new TextFieldListener(addressTextField));
        postcodeTextField.focusedProperty().addListener(new TextFieldListener(postcodeTextField));
        cityTextField.focusedProperty().addListener(new TextFieldListener(cityTextField));

    }

    private class TextFieldListener implements ChangeListener<Boolean>{

        private TextField textField;

        public TextFieldListener(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                presenter.textFieldFocusGained(textField);

            }
            else{
                presenter.textFieldFocusLost(textField);
            }
        }
    }

    @FXML
    protected void textFieldActionPerformed (ActionEvent event){
        presenter.textFieldActionPerformed(event);
    }

    @FXML
    protected void newButtonActionPerformed (ActionEvent event){
        presenter.newContact();
    }

    @FXML
    protected void deleteButtonActionPerformed (ActionEvent event){
        presenter.removeCurrentContact();
    }
    
    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }    
}
