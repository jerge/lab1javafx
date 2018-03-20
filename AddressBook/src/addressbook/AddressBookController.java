
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
