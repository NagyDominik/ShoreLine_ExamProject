package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXComboBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
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
    private void newConfigClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/NewConfigWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("New Config");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void startClicked(ActionEvent event) {
        DALManager dal = new DALManager();  //Just to test the XLSX reading
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

    @FXML
    private void moreDetailClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/DetailWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("New Config");
        stage.setResizable(false);
        stage.show();
    }

}
