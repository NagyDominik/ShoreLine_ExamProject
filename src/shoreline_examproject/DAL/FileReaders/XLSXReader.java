package shoreline_examproject.DAL.FileReaders;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import shoreline_examproject.BE.AttributeValueMap;

/**
 * Read in data from an XLSX file.
 *
 * @author sebok
 */
public class XLSXReader extends FileReader {

    @Override
    public AttributeValueMap getData(File file) {
        
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            Iterator<Row> firstRow = rowIterator;   //Save a reference to the first row, to be able to retrieve the attributes;
            
            if (rowIterator.hasNext()) {
                rowIterator.next(); //Skip the first line (attributes)
            }
            
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(XLSXReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
        }
        return null;
    }
}
