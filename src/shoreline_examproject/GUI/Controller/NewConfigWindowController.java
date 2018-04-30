package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import shoreline_examproject.BE.AttributeValueMap;
import shoreline_examproject.GUI.Model.Model;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class NewConfigWindowController implements Initializable {

    @FXML
    private JFXButton closeButton;

    private Model model;
    @FXML
    private ListView<AttributeValueMap> lstViewImportAttributes;
    @FXML
    private ListView<AttributeValueMap> lstViewExportAttributes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        setUpAttributeList();
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpAttributeList()
    {

    }

    
    
}
