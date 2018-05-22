package shoreline_examproject.BLL.Conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BLL.IBLLManager;
import shoreline_examproject.Utility.EventLogger;

/**
 * Used to manage ConversionTask objects.
 *
 * @author sebok
 */
public class Converter {

    private List<ConversionTask> tasks = new ArrayList();
    private ConversionTaskPool pool = new ConversionTaskPool();
    private ExecutorService execService;
    private IBLLManager manager;

    public Converter(IBLLManager manager) {
        this.manager = manager;
        int procCount = Runtime.getRuntime().availableProcessors();

        execService = Executors.newFixedThreadPool(procCount, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true); // Do not allow converter threads in the background
            return t;
        });
    }

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
            for (ConversionTask task : tasks) {
                CompletableFuture f = CompletableFuture.supplyAsync(() -> {
                    try {
                        return task.call();
                    }
                    catch (Exception ex) {
                        Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }, execService); //execService.<Callable<AttributesCollection>>submit(task);
//                CompletableFuture<AttributesCollection> future = 
                    f.thenAccept((Object t) -> {
                    if (t == null) {
                        if (task.getIsCanceled()) {
                            EventLogger.log(EventLogger.Level.ALERT, "Task " + task.getConfigName() + " has been canceled by " + "INSERT USERNAME HERE");
                            System.out.println("CANCELED");
                        } else {
                            System.out.println("NULL");
                        }
                    } else {
                        AttributesCollection ac = (AttributesCollection) t;
                        manager.saveToJSON(ac);
                    }
                });

//                futures.add(f);
            }
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
    }

    public void stopTask(ConversionTask task) {
        try {
            int id = findTask(task);
            if (id == -1) {
                throw new Exception("Could not find task!");
            }
            tasks.get(id).stop();
            tasks.remove(id);
            System.out.println("Task Stopped");
        }
        catch (Exception e) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Information: " + e.getMessage());
        }
    }

    public void pauseTask(ConversionTask task) {
        try {
            int id = findTask(task);
            ConversionTask t = tasks.get(id);
            if (id == -1) {
                throw new Exception("Could not find task!");
            }
            if (t.isPaused()) {
                t.resume();
            } else {
                t.pause();
            }
        }
        catch (Exception e) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Information: " + e.getMessage());
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
