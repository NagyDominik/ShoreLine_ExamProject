package shoreline_examproject.BLL.Conversion;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.Utility.EventLogger;

/**
 * Handles conversion of files that are automatically detected in the designated folders.
 * @author sebok
 */
public class FolderConverter {
    private final ExecutorService executorService;
    private final List<ConversionTask> conversionTasks;
    private final BLLManager manager;
    private final ConversionTaskPool conversionTaskPool;
    private boolean isRunning;
    
    public FolderConverter(BLLManager manager) {
        this.executorService = Executors.newFixedThreadPool(1, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true); // Do not allow converter threads in the background
            return t;
        });
        
        this.conversionTasks = new ArrayList<>();
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
        System.out.println("Conversion task added!");
        if (!isRunning) {
            changeState();
        }
    } 
    
    public void changeState() {
        if (!isRunning) {
            Runnable r = (() -> convert());
            
            isRunning = true;
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        }
        else {
            isRunning = false;
        }
    }
    
    private void convert() {
        while (isRunning) {
            if (!conversionTasks.isEmpty()) {
                //for (ConversionTask conversionTask : conversionTasks) {
                   for (Iterator<ConversionTask> iterator = conversionTasks.iterator(); iterator.hasNext();) {
                        ConversionTask conversionTask = iterator.next();
                        CompletableFuture<AttributesCollection> f = CompletableFuture.supplyAsync(() -> {
                        try {
                            return conversionTask.call();
                        }
                        catch (Exception ex) {
                            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
                        }
                        return null;
                    }, executorService);

                    f.thenAccept((Object t) -> {
                        if (t == null) {
                        if (conversionTask.getIsCanceled()) {
                            EventLogger.log(EventLogger.Level.ALERT, "Task " + conversionTask.getConfigName() + " has been canceled by " + "INSERT USERNAME HERE");
                            System.out.println("CANCELED");
                        } else {
                            System.out.println("NULL");
                        }
                    } else {
                        AttributesCollection ac = (AttributesCollection) t;
                        manager.saveToJSON(ac);
                    }
                    });
                    iterator.remove();
                }
            }
        }
    }
}
