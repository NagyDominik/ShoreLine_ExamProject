package shoreline_examproject.DAL.FileReaders;

import java.io.File;
import shoreline_examproject.BE.AttributesCollection;

/**
 *
 * @author sebok
 */
public abstract class IFileReader {

    public abstract AttributesCollection getData(File file);
}
