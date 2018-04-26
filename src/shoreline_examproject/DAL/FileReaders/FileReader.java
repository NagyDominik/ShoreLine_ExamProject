package shoreline_examproject.DAL.FileReaders;

import BE.AttributesCollection;
import java.io.File;

/**
 *
 * @author sebok
 */
public abstract class FileReader
{
    public abstract AttributesCollection getData(File file);
}
