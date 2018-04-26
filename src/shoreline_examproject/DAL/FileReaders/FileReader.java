package shoreline_examproject.DAL.FileReaders;

import java.util.List;

/**
 *
 * @author sebok
 */
public abstract class FileReader
{
    public abstract List<?> loadFileContents();
}
