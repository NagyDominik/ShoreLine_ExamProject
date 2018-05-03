package shoreline_examproject.BLL.Conversion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;

/**
 * Used to manage ConversionTask objects.
 * @author sebok
 */
public class Converter
{
    private ObservableList<ConversionTask> tasks = FXCollections.observableArrayList();
    private ConversionTaskPool pool = new ConversionTaskPool();
    
    public AttributesCollection convert(AttributesCollection input, Config config)
    {
        ConversionTask task = pool.checkOut();
        
        task.setInput(input);
        task.setConfig(config);
        
        pool.checkIn(task);
    }
}
