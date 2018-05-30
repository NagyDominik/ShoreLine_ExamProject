package shoreline_examproject.BLL.Conversion;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
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
    private final BlockingQueue<ConversionTask> conversionTasks;
    private final List<ConversionTask> tasks;
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

        this.conversionTasks = new LinkedBlockingDeque<>();
        this.conversionTaskPool = new ConversionTaskPool();
        this.tasks = new ArrayList<>();
        this.manager = manager;
        this.isRunning = false;
    }

    public void addConversionTask(Path p, FolderInformation fi) throws InterruptedException {
        if (fi == null || fi.getConfig() == null) {
            return;
        }
        AttributesCollection ac = manager.loadFileData(p.toString());
        ac.setExportPath(fi.getExportPath().toString() + "\\" + p.getFileName() + ".json");
        ConversionTask newTask = conversionTaskPool.checkOut();
        newTask.setInput(ac);
        newTask.setConfig(fi.getConfig());
        System.out.println("Added: " + newTask.getInputData().getExportPath());
        convert(newTask);
    }

    private void convert(ConversionTask task) {
        CompletableFuture<AttributesCollection> f = CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception ex) {
                EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
                Logger.getLogger(FolderConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }, executorService);

        AttributesCollection ac = f.join();
        if (ac == null) {
            if (task.getIsCanceled()) {
                EventLogger.log(EventLogger.Level.ALERT, "Task " + task.getConfigName() + " has been canceled by " + "INSERT USERNAME HERE");
                System.out.println("CANCELED");
            } else {
                System.out.println("NULL");
            }
        } else {
            manager.saveToJSON(ac);
        }
    }
}
