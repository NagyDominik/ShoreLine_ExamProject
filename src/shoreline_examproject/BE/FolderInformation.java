package shoreline_examproject.BE;

import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import shoreline_examproject.Utility.EventLogger;

/**
 * Store information about the selected folder, so it can be displayed in the
 * table view.
 */
public class FolderInformation {

    private File selectedFolder;
    private Config assignedConfig;
    private IntegerProperty numberOfConvertibleFiles = new SimpleIntegerProperty();

    public FolderInformation(File selectedFolder) {
        this.selectedFolder = selectedFolder;
        countNumberOfConvertibleFiles();
    }

    public IntegerProperty numberOfConvertibleFilesProperty() {
        return numberOfConvertibleFiles;
    }

    public String getFolderName() {
        return this.selectedFolder.getName();
    }

    public void setConfig(Config newValue) {
        this.assignedConfig = newValue;
    }

    private void countNumberOfConvertibleFiles() {
        int n = 0;
        for (File listFile : selectedFolder.listFiles()) {
            if (listFile.getAbsolutePath().endsWith(".xlsx")) {
                n++;
            }
        }

        this.numberOfConvertibleFiles.set(n);
    }

    public Path getPath() {
        return this.selectedFolder.toPath();
    }
    
    public Config getConfig() {
        return this.assignedConfig;
    }
    
    public void increaseNumberOfConvertibleFiles() {
        this.numberOfConvertibleFiles.set(numberOfConvertibleFiles.get()+1);
    }

    public boolean contains(Path filename) {
        //return new File(selectedFolder.getAbsolutePath(), filename.getParent().toString()).exists();
        return this.selectedFolder.getAbsolutePath().equals(filename.getParent().toString());
    }

    public void decreaseNumberOfConvertibleFiles() {
        if (this.numberOfConvertibleFiles.get() < 1) {
            EventLogger.log(EventLogger.Level.ALERT, "Cannot delete from empty folder!");
            return;
        }
        
        this.numberOfConvertibleFiles.set(numberOfConvertibleFiles.get() - 1);
    }
}
