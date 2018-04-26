package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXComboBox;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label taskNameLbl;
    @FXML
    private Label progressLbl;
    @FXML
    private Label configLbl;
    @FXML
    private JFXComboBox<?> configComboBox;
    @FXML
    private Label filePathLbl;
    @FXML
    private TableView<?> previewTV;
    @FXML
    private TableView<?> taskTV;
    @FXML
    private Label userNameLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameLbl.setText(System.getProperty("user.name"));
        userNameLbl.setAlignment(Pos.CENTER_RIGHT);
    }    

    @FXML
    private void importFileClicked(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(this.userNameLbl.getScene().getWindow());
            filePathLbl.setText(file.getPath());
        }
        catch (Exception ex) {
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void newConfigClicked(ActionEvent event) {
    }

    @FXML
    private void startClicked(ActionEvent event) {
        DALManager dal = new DALManager();
        dal.loadFileData("asd");
    }

    @FXML
    private void stopClicked(ActionEvent event) {
    }

    @FXML
    private void pauseClicked(ActionEvent event) {
    }

    @FXML
    private void optionsClicked(ActionEvent event) {
    }
    
}
