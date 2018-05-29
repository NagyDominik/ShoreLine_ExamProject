package shoreline_examproject.BLL.Conversion;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.Utility.EventLogger;

/**
 * Handles conversion of files that are automatically detected in the designated
 * folders.
 *
 * @author sebok
 */
public class FolderConverter {

    private final ExecutorService executorService;
    private final ConcurrentLinkedQueue<ConversionTask> conversionTasks;
    private final BLLManager manager;
    private final ConversionTaskPool conversionTaskPool;
    private boolean isRunning;

    public FolderConverter(BLLManager manager) {
        this.executorService = Executors.newFixedThreadPool(1, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true); // Do not allow converter threads in the background
            t.setName("FolderConverterThread");
            return t;
        });

        this.conversionTasks = new ConcurrentLinkedQueue<>();
        this.conversionTaskPool = new ConversionTaskPool();
        this.manager = manager;
        this.isRunning = false;
    }

    public void addConversionTask(Path p, FolderInformation fi) {
        if (fi.getConfig() == null) {
            throw new NullPointerException("Config is null");
        }
        AttributesCollection ac = manager.loadFileData(p.toString());
        ac.setExportPath(fi.getExportPath().toString() + "\\" + p.getFileName() + ".json");
        ConversionTask newTask = conversionTaskPool.checkOut();
        newTask.setInput(ac);
        newTask.setConfig(fi.getConfig());
        conversionTasks.add(newTask);
        System.out.println("File added: " + p.toString());
        if (!isRunning) {
            changeState();
        }
    }

    public void changeState() {
        if (!isRunning) {
            Runnable r = (() -> {
                try {
                    convert();
                } catch (InterruptedException ex) {
                    Logger.getLogger(FolderConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            isRunning = true;
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        } else {
            isRunning = false;
        }
    }

    private void convert() throws InterruptedException {
        while (isRunning) {
            ConversionTask conversionTask = conversionTasks.poll();
            if (conversionTask == null) {
                continue;
            }
            
            CompletableFuture<AttributesCollection> f = CompletableFuture.supplyAsync(() -> {
                try {
                    return conversionTask.call();
                } catch (Exception ex) {
                    EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
                    Logger.getLogger(FolderConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }, executorService);
            
            AttributesCollection ac = f.join();
            if (ac == null) {
                if (conversionTask.getIsCanceled()) {
                    EventLogger.log(EventLogger.Level.ALERT, "Task " + conversionTask.getConfigName() + " has been canceled by " + "INSERT USERNAME HERE");
                    System.out.println("CANCELED");
                } else {
                    System.out.println("NULL");
                }
            }
            else {
                System.out.println("File converted: " + ac.getExportPath());
                manager.saveToJSON(ac);
            }
            
//            f.thenAccept((Object t) -> {
//                if (t == null) {
//                    if (conversionTask.getIsCanceled()) {
//                        EventLogger.log(EventLogger.Level.ALERT, "Task " + conversionTask.getConfigName() + " has been canceled by " + "INSERT USERNAME HERE");
//                        System.out.println("CANCELED");
//                    } else {
//                        System.out.println("NULL");
//                    }
//                } else {
//                    AttributesCollection ac = (AttributesCollection) t;
//                    manager.saveToJSON(ac);
//                }
//            });
 
        }
    }
}
