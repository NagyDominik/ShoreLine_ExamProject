package shoreline_examproject.BLL.Conversion;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.BLLManager;

/**
 * Handles conversion of files that are automatically detected in the designated folders.
 * @author sebok
 */
public class FolderConverter {
    private final ExecutorService executorService;
    private final List<ConversionTask> conversionTasks;
    private final BLLManager manager;
    private final ConversionTaskPool conversionTaskPool;
    
    public FolderConverter(BLLManager manager) {
        this.executorService = Executors.newFixedThreadPool(1, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true); // Do not allow converter threads in the background
            return t;
        });
        
        this.conversionTasks = new ArrayList<>();
        this.conversionTaskPool = new ConversionTaskPool();
        this.manager = manager;
    }
    
    public void addConversionTask(Path p, Config c) {
        AttributesCollection ac = manager.loadFileData(p.toString());
        ConversionTask newTask = conversionTaskPool.checkOut();
        newTask.setInput(ac);
        newTask.setConfig(c);
        conversionTasks.add(newTask);
        System.out.println("Conversion task added!");
    } 
}
