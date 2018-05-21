/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;
import shoreline_examproject.Utility.EventLogger;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class OptionsWindowController implements Initializable {

    @FXML
    private JFXButton closeButton;
    @FXML
    private Label lblCurrentName;
    @FXML
    private JFXTextField txtFieldNewName;
    
    private String newName;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            lblCurrentName.setText(model.getCurrentUser());
            System.out.println(model.getCurrentUser());
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
            EventPopup.showAlertPopup(ex);       
        }
    }
    
    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnSaveClicked(ActionEvent event) {
        changeName();
    }

    private void changeName() {
        if (txtFieldNewName != null) {
            newName = txtFieldNewName.getText();
            EventLogger.setUsername(newName);
            lblCurrentName.setText(newName);
        }
            lblCurrentName.setText(EventLogger.getUsername());
    }
    
}
