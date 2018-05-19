package shoreline_examproject.BLL;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.Utility.EventLogger;

/**
 * Handle folders, so that newly added files will be converted.
 * @author sebok
 */
public class FolderHandler {
    
    private List<FolderInformation> folders;
    private WatchService watcher;
    
    private List<WatchKey> keys;
    
    private Thread watcherThread;
    private final BooleanProperty isMonitoring = new SimpleBooleanProperty();
   
    private BLLManager manager;
    
    public FolderHandler(BLLManager manager) throws IOException {
        this.manager = manager;
        this.folders = new ArrayList<>();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.isMonitoring.setValue(false);
        
    }
    
    public void changeMonitoring() {
        if (isMonitoring.get()) {
            watcherThread.interrupt();
            isMonitoring.setValue(false);
        } 
        else {
            Runnable r = () -> {
                watch();
            };

            watcherThread = new Thread(r);
            watcherThread.setDaemon(true);
            watcherThread.start();
            isMonitoring.setValue(true);
        }
    }
    
    public void addFolderInformation(FolderInformation fi) throws IOException {
        if (folders == null) {
            EventLogger.log(EventLogger.Level.ALERT, "The folders List have not been initialized yet!");
            throw new NullPointerException("The folders List have not been initialized yet!");
        }
        
        folders.add(fi);
        getKeys();
    }
    
    private void watch()
    {
        try {
            while (true) {
                
                for (WatchKey key : keys) {
                    for (WatchEvent<?> pollEvent : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = pollEvent.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            WatchEvent<Path> ev = (WatchEvent<Path>) pollEvent;
                            Path filename = ev.context();
                           
                            Path f = (Path)key.watchable();
                            Path full = f.resolve(filename);
                            
                            if (filename.toString().endsWith(".xlsx")) {
                                for (FolderInformation folder : folders) {
                                    if (folder.contains(full)) {
                                        folder.increaseNumberOfConvertibleFiles();
                                        AttributesCollection ac = manager.loadFileData(full.toString());
                                        manager.createConversionTask(folder.getConfig(), ac);
                                        break;
                                    }
                                }
                            }

                            System.out.println("File created: " + filename);
                            //EventLogger.log(EventLogger.Level.INFORMATION, "File created: " + filename);
                            continue;
                        }
                        
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            WatchEvent<Path> ev = (WatchEvent<Path>) pollEvent;
                            Path filename = ev.context();
                           
                            Path f = (Path)key.watchable();
                            Path full = f.resolve(filename);

                            if (filename.toString().endsWith(".xlsx")) {
                                for (FolderInformation folder : folders) {
                                    if (folder.contains(full)) {
                                        folder.decreaseNumberOfConvertibleFiles();
                                        break;
                                    }
                                }
                            }
                            
                            System.out.println("File deleted: " + filename);
                            //EventLogger.log(EventLogger.Level.INFORMATION, "File deleted: " + filename);
                            continue;
                        }

                    }
                }
            }
            
        } catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
    }

    private void getKeys() throws IOException {        
        if (keys == null) {
            keys = new ArrayList<>();
        }
        else {
            keys.clear();
        }
        
        for (FolderInformation folder : folders) {
            keys.add(folder.getPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE));
        }
    }
    
    public boolean isIsMonitoring() {
        return isMonitoring.get();
    }

    public void setIsMonitoring(boolean value) {
        isMonitoring.set(value);
    }

    public BooleanProperty isMonitoringProperty() {
        return isMonitoring;
    }

    void removeFolder(FolderInformation selected) throws IOException {
        if (folders == null) {
            EventLogger.log(EventLogger.Level.ALERT, "The folders List have not been initialized yet!");
            throw new NullPointerException("The folders List have not been initialized yet!");
        }
        
        folders.remove(selected);
        getKeys();
    }
}
