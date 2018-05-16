package shoreline_examproject.DAL.FileReaders;

/**
 * Responsible for creating a appropriate instance of the FileReader class.
 *
 * @author sebok
 */
public class FileReaderFactory {

    public static FileReader CreateFileReader(String path) {
        String extension = path.substring(path.lastIndexOf("."));
        switch (extension) {
            //case ".xlsx": return new OptimizedExcelReader();
            case ".xlsx": return new ExcelReader();
            case ".xls": return new ExcelReader();
            case ".xml": return new XMLReader();
            default: throw new IllegalArgumentException("File type not recognised");
        }
    }
}
