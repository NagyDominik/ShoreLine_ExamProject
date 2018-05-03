package shoreline_examproject.BLL.Conversion;

import java.util.ArrayList;
import java.util.List;
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
    
    public ConversionTask createConversionTask(Config c, AttributesCollection ac) {
        ConversionTask task = pool.checkOut();
        task.setConfig(c);
        task.setInput(ac);
        tasks.add(task);
        EventLogger.log(EventLogger.Level.INFORMATION, "New conversion task created.");
        return task;
    }
}
