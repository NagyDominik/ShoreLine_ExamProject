package shoreline_examproject.GUI.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.DirectoryChooser;
import shoreline_examproject.BE.Config;
import shoreline_examproject.GUI.Model.Model;

/**
 * FXML Controller class
 *
 * @author sebok
 */
public class AssignFolderWindowController implements Initializable
{

    @FXML
    private TableColumn<FolderInformation, String> tblColumnFolder;
    @FXML
    private TableColumn<FolderInformation, Config> tblColumConfig;
    @FXML
    private TableColumn<FolderInformation, Integer> tblColumNumOfFiles;
    @FXML
    private TableView<FolderInformation> tblViewFiles;

    
    private Model model;
    private ObservableList<FolderInformation> selectedFolders;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = Model.getInstance();
        selectedFolders = FXCollections.observableArrayList();
        
        setUpTableView();
        
    }    

    private void setUpTableView()
    {
        tblViewFiles.setItems(selectedFolders);
        
        tblColumnFolder.setCellValueFactory((TableColumn.CellDataFeatures<FolderInformation, String> param) -> {
            return new ReadOnlyStringWrapper(param.getValue().getFolderName());
        });  
        
        
        tblColumConfig.setCellFactory(ComboBoxTableCell.forTableColumn(model.getConfList()));
        tblColumConfig.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FolderInformation, Config>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<FolderInformation, Config> event)
            {
                if (event.getNewValue() != null) {
                    event.getRowValue().setConfig(event.getNewValue());
                }
            }
        });
        
        tblColumNumOfFiles.setCellValueFactory((TableColumn.CellDataFeatures<FolderInformation, Integer> param) -> param.getValue().numberOfConvertibleFilesProperty().asObject());
        
    }
    
    @FXML
    private void btnSelectFolderClicked(ActionEvent event)
    {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select folder");
        File selectedDir = dc.showDialog(tblViewFiles.getScene().getWindow());
        selectedFolders.add(new FolderInformation(selectedDir));
    }
   
    
    /**
     * Store information about the selected folder, so it can be displayed in the table view.
     */
    class FolderInformation {
        
        private File selectedFolder; 
        private Config assignedConfig;
        private final IntegerProperty numberOfConvertibleFiles = new SimpleIntegerProperty();
       
        public FolderInformation(File selectedFolder)
        {
            this.selectedFolder = selectedFolder;
            countNumberOfConvertibleFiles();
        }

        private int getNumberOfConvertibleFiles()
        {
            return numberOfConvertibleFiles.get();
        }

        private void setNumberOfConvertibleFiles(int value)
        {
            numberOfConvertibleFiles.set(value);
        }

        private IntegerProperty numberOfConvertibleFilesProperty()
        {
            return numberOfConvertibleFiles;
        }
        
        public String getFolderName() {
            return this.selectedFolder.getName();
        }

        private void setConfig(Config newValue)
        {
            this.assignedConfig = newValue;
        }

        private void countNumberOfConvertibleFiles()
        {
            int n = 0;
            for (File listFile : selectedFolder.listFiles()) {
                if (listFile.getAbsolutePath().endsWith(".xlsx")) {
                    n++;
                }
            }
            
            this.numberOfConvertibleFiles.set(n);
        }
        
        
    }
}
