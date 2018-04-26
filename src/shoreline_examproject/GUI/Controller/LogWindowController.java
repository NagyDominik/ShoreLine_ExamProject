/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Dominik
 */
public class LogWindowController implements Initializable {

    @FXML
    private TableView<?> logTV;
    @FXML
    private TableColumn<?, String> dateCol;
    @FXML
    private TableColumn<?, String> typeCol;
    @FXML
    private TableColumn<?, String> descCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
