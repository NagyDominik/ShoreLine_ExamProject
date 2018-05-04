package shoreline_examproject.BLL.Conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.Utility.EventLogger;

/**
 * Used to manage ConversionTask objects.
 * @author sebok
 */
public class Converter
{
    private List<ConversionTask> tasks = new ArrayList();
    private ConversionTaskPool pool = new ConversionTaskPool();
    
    /**
     * Retrieve a ConversionTask object from the object pool, and store it so it can be started later.
     * @param c The configuration that will be used to convert data.
     * @param ac The input data.
     * @return The created ConversionTask object.
     */
    public ConversionTask createConversionTask(Config c, AttributesCollection ac) {
        ConversionTask task = pool.checkOut();
        task.setConfig(c);
        task.setInput(ac);
        tasks.add(task);
        EventLogger.log(EventLogger.Level.INFORMATION, "New conversion task created.");
        return task;
    }

    /**
     * Start converting data using the stored ConversionTask objects.
     */
    public void convertAll()
    {
        try {
            List<ConversionTask> currentTasks;
            int procCount = Runtime.getRuntime().availableProcessors();
            
            ExecutorService execService = Executors.newCachedThreadPool();
            for (int i = 0; i < procCount; i++) {
                execService.submit(tasks.get(i));
            }
          
        } catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
    }
}
