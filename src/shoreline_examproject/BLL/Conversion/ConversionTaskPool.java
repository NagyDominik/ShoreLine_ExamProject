package shoreline_examproject.BLL.Conversion;

import shoreline_examproject.Utility.ObjectPool;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author sebok
 */
public class ConversionTaskPool extends ObjectPool<ConversionTask>
{

    public ConversionTaskPool()
    {
        super();
    }
 
    @Override
    protected ConversionTask create()
    {
        try {
            return new ConversionTask();
        }
        catch (Exception ex)
        {
            EventLogger.log(EventLogger.Level.ERROR, "An exception occured while attempting to create a ConversionTask. Message: \n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    protected boolean validate(ConversionTask o)
    {
        return o != null;
    }

    @Override
    public void expire(ConversionTask o)
    {
        o = null;
    }
    
}
