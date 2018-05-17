package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.binding.SelectBinding;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author sebok
 */
public class AssignFolderWindowController implements Initializable {

    @FXML
    private TableColumn<FolderInformation, String> tblColumnFolder;
    @FXML
    private TableColumn<FolderInformation, Config> tblColumConfig;
    @FXML
    private TableColumn<FolderInformation, Integer> tblColumNumOfFiles;
    @FXML
    private TableView<FolderInformation> tblViewFiles;
    @FXML
    private JFXButton closeButton;
    
    private Model model;

    private List<Config> confList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            confList = new ArrayList<Config>(model.getConfList());
            
            setUpTableView();
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }

    }

    private void setUpTableView() {
        tblViewFiles.setItems(model.getMonitoredFolders());

        tblColumnFolder.setCellValueFactory((TableColumn.CellDataFeatures<FolderInformation, String> param) -> {
            return new ReadOnlyStringWrapper(param.getValue().getFolderName());
        });
        
        tblColumConfig.setCellFactory(ComboBoxTableCell.forTableColumn(model.getConfList()));
        
        tblColumConfig.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper(param.getValue().getConfig());
        });
        
        tblColumConfig.setOnEditCommit((TableColumn.CellEditEvent<FolderInformation, Config> event) -> {
            if (event.getNewValue() != null) {
                event.getRowValue().setConfig(event.getNewValue());
            }
        });

        
        tblColumNumOfFiles.setCellValueFactory((param) -> {
            return param.getValue().numberOfConvertibleFilesProperty().asObject();
        });
        
    }

    @FXML
    private void btnSelectFolderClicked(ActionEvent event) {
        try {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Select folder");
            File selectedDir = dc.showDialog(tblViewFiles.getScene().getWindow());
            model.addFolderToList((new FolderInformation(selectedDir)));
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
}
