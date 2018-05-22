package shoreline_examproject.BLL;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author sebok
 */
public class FolderHandler {
    private final WatchService watcher;
    private final HashMap<WatchKey, Path> keys; 
    private final BooleanProperty isRunning = new SimpleBooleanProperty();    
    private Thread watchThread;
    private final Object lock;
    private final BLLManager bLLManager;
    private final List<FolderInformation> folders; 
    
    public FolderHandler(BLLManager manager) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.lock = new Object();
        this.bLLManager = manager;
        this.folders = new ArrayList<>();
    }
    
    public void registerDirectory(FolderInformation fi) throws IOException {
        WatchKey key = fi.getPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
        keys.put(key, fi.getPath());
        folders.add(fi);
    }
    
    public void startMonitoring() throws InterruptedException {
        synchronized (lock) {
            if (!isRunning.get()) {
                if (watchThread == null) {
                    Runnable r = () -> {
                        runWatchLoop();
                    };
    
                    watchThread = new Thread(r);
                    watchThread.setDaemon(true);
                    
                    
                    isRunning.setValue(true);
                    System.out.println("Starting watch thread! " + Thread.currentThread().getName());
                    watchThread.start();
                }
                else {
                    isRunning.setValue(true);
                    System.out.println("Continuig watch thread!" + Thread.currentThread().getName());
                    lock.notify();
                }
            }
            else {
                isRunning.set(false);
                System.out.println("Pausing watch thread!");
                watchThread.interrupt();
                watchThread = null;
            }
        }
    }        
    
    /**
     * Process events in the queued folders.
     * Implemented using: https://howtodoinjava.com/java-8/java-8-watchservice-api-tutorial/
     */
    private void runWatchLoop() {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            }
            catch (InterruptedException ex) {
                return;
            }
            
            Path dir = keys.get(key);
            
            if (dir == null) {
                EventLogger.log(EventLogger.Level.ALERT, "WatchKey not recognized!");
                continue;
            }
            
            for (WatchEvent<?> pollEvent : key.pollEvents()) {
                WatchEvent.Kind kind = pollEvent.kind();
                
                Path name = ((WatchEvent<Path>)pollEvent).context();    
                Path child = dir.resolve(name);
                
                System.out.format("%s: %s\n", pollEvent.kind().name(), child);
                
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    if (child.toString().endsWith(".xlsx")) {
                        for (FolderInformation folder : folders) {
                            if (folder.contains(child)) {
                                bLLManager.addNewFileToFolderConverter(child, folder.getConfig());
                            }
                        }
                    }
                }
                
                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);
                    
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        }
    } 
    
    
    public boolean isRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean value) {
        isRunning.set(value);
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public void removeFolder(FolderInformation fi) {
        Path p = fi.getPath();
        keys.values().remove(p);
    }
}
