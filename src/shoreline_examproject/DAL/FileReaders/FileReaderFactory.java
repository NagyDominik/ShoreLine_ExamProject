package shoreline_examproject.DAL.FileReaders;

/**
 * REsponsible for creating a appropriate instance of the FileReader class.
 * @author sebok
 */
public class FileReaderFactory
{
    public static FileReader CreateFileReader(String path)
    {
        if (path.endsWith(".xlsx")) {
            return new XLSXReader();
        }
        else if (path.endsWith("xml")) {
            return new XMLReader();
        }
        else {
            throw new IllegalArgumentException("File type not recognized!");
        }
    }
}
