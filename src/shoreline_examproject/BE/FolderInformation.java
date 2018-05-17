package shoreline_examproject.BE;

import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Store information about the selected folder, so it can be displayed in the
 * table view.
 */
public class FolderInformation {

    private File selectedFolder;
    private Config assignedConfig;
    private final IntegerProperty numberOfConvertibleFiles = new SimpleIntegerProperty();

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
}
