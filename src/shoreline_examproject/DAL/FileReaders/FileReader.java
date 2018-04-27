package shoreline_examproject.DAL.FileReaders;

import java.io.File;
import java.util.List;
import shoreline_examproject.BE.AttributeValueMap;

/**
 *
 * @author sebok
 */
public abstract class FileReader {

    public abstract AttributeValueMap getData(File file);
}
