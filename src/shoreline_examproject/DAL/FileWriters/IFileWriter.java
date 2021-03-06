package shoreline_examproject.DAL.FileWriters;

import shoreline_examproject.BE.AttributesCollection;

/**
 * Writes data to a file.
 *
 * @author sebok
 */
public abstract class IFileWriter {

    public abstract void saveData(AttributesCollection data);
}
