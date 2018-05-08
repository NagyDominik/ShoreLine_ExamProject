package shoreline_examproject.BLL.Conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.Utility.EventLogger;

/**
 * Used to manage ConversionTask objects.
 *
 * @author sebok
 */
public class Converter {

    private List<ConversionTask> tasks = new ArrayList();
    private ConversionTaskPool pool = new ConversionTaskPool();
    private List<Future> futures = new ArrayList();

    /**
     * Retrieve a ConversionTask object from the object pool, and store it so it
     * can be started later.
     *
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
    public void convertAll() {
        try {
            List<ConversionTask> currentTasks;
            int procCount = Runtime.getRuntime().availableProcessors();

            ExecutorService execService = Executors.newFixedThreadPool(procCount, (Runnable r) -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true); // Close the applications if only converter threads are running.
                return t;
            });

            for (ConversionTask task : tasks) {
                Future f = execService.<Callable<AttributesCollection>>submit(task);
                futures.add(f);
            }
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
    }

    public void stopTask(ConversionTask task) {
        try {
            int id = findTask(task);
            if (id == -1) {
                throw new Exception("Could not find task!");
            }
            futures.get(id).cancel(true);
            tasks.remove(id);
            System.out.println("Task Stopped");
        }
        catch (Exception e) {
            EventLogger.log(EventLogger.Level.ERROR, e.getMessage());
        }
    }

    public void pauseTask(ConversionTask task) {
        try {
            int id = findTask(task);
            if (id == -1) {
                throw new Exception("Could not find task!");
            }
            
        }
        catch (Exception e) {
        }
    }

    private int findTask(ConversionTask task) {
        int id = -1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == task) {
                id = i;
                break;
            }
        }
        return id;
    }

}
