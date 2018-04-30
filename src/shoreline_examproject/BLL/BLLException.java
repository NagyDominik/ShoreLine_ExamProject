package shoreline_examproject.BLL;

/**
 *
 * @author sebok
 */
public class BLLException extends Exception
{

    public BLLException(Exception ex)
    {
        super(ex.getMessage());
    }

    public BLLException(String message)
    {
        super(message);
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
    
    
    
}
