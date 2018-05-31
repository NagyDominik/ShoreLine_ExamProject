package shoreline_examproject.BLL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public void registerDirectory(FolderInformation fi) throws IOException, InterruptedException {
        WatchKey key = fi.getPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

        keys.put(key, fi.getPath());
        folders.add(fi);
    }

    public void startMonitoring() throws InterruptedException {
        synchronized (lock) {
            if (!isRunning.get()) {
                if (watchThread == null) {
                    Runnable r = () -> {
                        try {
                            runWatchLoop();
                        }
                        catch (Exception ex) {
                            Logger.getLogger(FolderHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    };

                    watchThread = new Thread(r);
                    watchThread.setDaemon(true);

                    isRunning.setValue(true);
                    System.out.println("Starting watch thread! " + Thread.currentThread().getName());
                    EventLogger.log(EventLogger.Level.INFORMATION, "Started folder monitoring");
                    watchThread.start();
                } else {
                    isRunning.setValue(true);
                    System.out.println("Continuig watch thread!" + Thread.currentThread().getName());
                    lock.notify();
                }
            } else {
                isRunning.set(false);
                System.out.println("Pausing watch thread!");
                EventLogger.log(EventLogger.Level.INFORMATION, "Paused folder monitoring");
                watchThread.interrupt();
                watchThread = null;
            }
        }
    }

    /**
     * Process events in the queued folders. Implemented using:
     * https://howtodoinjava.com/java-8/java-8-watchservice-api-tutorial/
     */
    private void runWatchLoop() throws InterruptedException, IOException {
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

                Path name = ((WatchEvent<Path>) pollEvent).context();
                Path child = dir.resolve(name);

                File f = child.toFile();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    String path = child.toString();
                    if (path.endsWith(".xlsx") || path.endsWith(".csv") || path.endsWith(".xls")) {
                        for (FolderInformation folder : folders) {
                            if (folder.contains(child)) {
                                waitForLock(f);

                                folder.setNumberOfConvertibleFiles(1);
                                bLLManager.addNewFileToFolderConverter(child, folder);
                                break;
                            }
                        }
                    }
                }

                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    String path = child.toString();
                    if (path.endsWith(".xlsx") || path.endsWith(".csv") || path.endsWith(".xls")) {
                        for (FolderInformation folder : folders) {
                            if (folder.contains(child)) {
                                folder.decreaseNumberOfConvertibleFiles();
                                break;
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

    private void waitForLock(File f) throws IOException, InterruptedException {
        boolean locked = true;
        while (locked) {    //Prevents a race condition when creating a new file. https://stackoverflow.com/questions/3369383/java-watching-a-directory-to-move-large-files
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(f, "r");
                raf.seek(f.length());
                locked = false;
            }
            catch (IOException ex) {
                locked = f.exists();
                if (locked) {
                    //System.out.println("File locked: " + f.getAbsolutePath() + ".");
                    Thread.sleep(500);
                } else {
                    //System.out.println("File was deleted while copying: " + f.getAbsolutePath() + ".");
                }
            } finally {
                if (raf != null) {
                    raf.close();
                }
            }
        }
    }

    private void convertAlreadyExistingFiles(FolderInformation fi) throws InterruptedException {
        File folder = new File(fi.getPath().toUri());

        for (File listFile : folder.listFiles()) {
            String path = listFile.getAbsolutePath();
            if (path.endsWith(".xlsx") || path.endsWith(".csv") || path.endsWith(".xls")) {
                bLLManager.addNewFileToFolderConverter(Paths.get(path), fi);
            }
        }
    }

    void updateFolderInformation(FolderInformation fi) throws BLLException {
        for (FolderInformation folder : folders) {
            if (folder.equals(fi)) {
                try {
                    if (fi.isHadDefaultFiles()) {
                        convertAlreadyExistingFiles(fi);
                        fi.setHadDefaultFiles(false);
                    }
                    break;
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(FolderHandler.class.getName()).log(Level.SEVERE, null, ex);
                    EventLogger.log(EventLogger.Level.ERROR, "Could not update existing folder information: " + ex.getMessage());
                    throw new BLLException(ex);
                }
            }
        }
    }
}
