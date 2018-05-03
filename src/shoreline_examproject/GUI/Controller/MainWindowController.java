package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.GUI.Model.ModelException;
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
    private JFXComboBox<Config> configComboBox;
    @FXML
    private Label filePathLbl;
    @FXML
    private TableView<?> previewTV;
    @FXML
    private TableView<ConversionTask> taskTV;
    @FXML
    private Label userNameLbl;
    @FXML
    private TableColumn<ConversionTask, String> taskTable;
    @FXML
    private TableColumn<ConversionTask, Double> progressTable;

    private Model model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameLbl.setText(System.getProperty("user.name"));
        userNameLbl.setAlignment(Pos.CENTER_RIGHT);
        model = Model.getInstance();
        model.setCurrentUser(userNameLbl.getText());
        taskTV.getItems().addAll(model.getTasks());
        setUpTaskTableView();
    }

    @FXML
    private void importFileClicked(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            String path = fc.showOpenDialog(this.userNameLbl.getScene().getWindow()).getPath();
            filePathLbl.setText(path);
            model.loadFileData(path);
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
        try {
            model.startConversion(configComboBox.getValue());
        }
        catch (ModelException ex) {
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void stopClicked(ActionEvent event) {
    }

    @FXML
    private void pauseClicked(ActionEvent event) {
    }

    @FXML
    private void optionsClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/OptionsWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Options");
        stage.setResizable(false);
        stage.show();
        
    }

    @FXML
    private void moreDetailClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/DetailWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Details");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void logClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/LogWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Log");
        stage.setResizable(false);
        stage.show();
    }

    private void setUpTaskTableView()
    {
        taskTable.setCellValueFactory((TableColumn.CellDataFeatures<ConversionTask, String> param) -> {
            ConversionTask ct = param.getValue();
            return new ReadOnlyStringWrapper(ct.getConfigName());
        });
        
       progressTable.setCellValueFactory((TableColumn.CellDataFeatures<ConversionTask, Double> param) -> {
           ConversionTask ct = param.getValue();
           
           return ct.progressProperty().asObject();
        });
       
       progressTable.setCellFactory(ProgressBarTableCell.<ConversionTask> forTableColumn());
    }
}
