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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shoreline_examproject.GUI.Model.Model;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCurrentName.setText(EventLogger.getUsername());
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
    }
    
}
