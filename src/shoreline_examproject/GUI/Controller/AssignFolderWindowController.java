package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.charts.ChartLayoutAnimator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JFileChooser;
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
    private TableView<FolderInformation> tblViewFiles;
    @FXML
    private TableColumn<FolderInformation, String> tblColumnFolder;
    @FXML
    private TableColumn<FolderInformation, Config> tblColumConfig;
    @FXML
    private TableColumn<FolderInformation, Integer> tblColumNumOfFiles;
    @FXML
    private TableColumn<FolderInformation, String> tblColumnDestination;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton btnMonitorStatus;
    @FXML
    private Circle crclStatusIndicator;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            setUpTableView();
            setUpIndicator();
        } catch (Exception ex) {
            Logger.getLogger(AssignFolderWindowController.class.getName()).log(Level.SEVERE, null, ex);
            EventLogger.log(EventLogger.Level.ERROR, "An error occured: " + ex.getMessage());
        } 
    }    

    @FXML
    private void btnSelectFolderClicked(ActionEvent event) throws IOException { 
        try {
            DirectoryChooser dc = new DirectoryChooser();
            File f = dc.showDialog(this.btnMonitorStatus.getScene().getWindow());
            
            if (f == null) {
                return;
            }
            
            FolderInformation fi = new FolderInformation(f);
            tblViewFiles.getItems().add(fi);
            model.registerDirectory(fi);
        } catch (ModelException ex) {
            Logger.getLogger(AssignFolderWindowController.class.getName()).log(Level.SEVERE, null, ex);
            EventLogger.log(EventLogger.Level.ERROR, "An error has occured: " + ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnStartClicked(ActionEvent event) {
        try {
            if (tblViewFiles.getItems().isEmpty()) {
                return;
            }
            model.startWatch();
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void btnRemoveFolderClicked(ActionEvent event) {
        FolderInformation selected = tblViewFiles.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            return;
        }
        
        model.removeDirectory(selected);
        tblViewFiles.getItems().remove(selected);
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
        
        tblColumnDestination.setCellFactory((TableColumn<FolderInformation, String> param) -> {
           return new TableCell<FolderInformation, String>() {
               @Override
               protected void updateItem(final String item, final boolean empty) {
                   super.updateItem(item, empty);
                   if (empty) {
                       setGraphic(null);
                       return;
                   }
                   
                   Button b = new Button("Select destination");
                   b.setOnAction((ActionEvent event) -> {
                       System.out.println("Selecting destination");
                       DirectoryChooser dc = new  DirectoryChooser();
                       
                       File destination = dc.showDialog(tblViewFiles.getScene().getWindow());
                        if (destination == null) {
                           return;
                        }
                       
                       FolderInformation fi = param.getTableView().getItems().get(getIndex());
                       fi.setExportpath(destination);
                       System.out.println(destination.toPath().toString());
                       Label label = new Label(destination.toPath().toString());
                       setGraphic(label);
                       
                   });
                   setGraphic(b);
               }
           };
        });
    }

    private void setUpIndicator() {
        model.getIsWatching().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               if (newValue) {
                    crclStatusIndicator.setFill(new Color(0, 1, 0, 1));
                    btnMonitorStatus.setText("Stop monitoring");
                }
                else {
                    crclStatusIndicator.setFill(new Color(1, 0, 0, 1));
                    btnMonitorStatus.setText("Start monitoring");
                }        
        });
    }
}