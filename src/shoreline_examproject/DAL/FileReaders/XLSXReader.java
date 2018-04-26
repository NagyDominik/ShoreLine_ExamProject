package shoreline_examproject.DAL.FileReaders;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import shoreline_examproject.BE.AttributesCollection;

/**
 * Read in data from an XLSX file.
 * @author sebok
 */
public class XLSXReader extends FileReader
{

    @Override
    public AttributesCollection getData(File file)
    {
        try {
            FileInputStream excelFile = new FileInputStream(file);
            Workbook book = new HSSFWorkbook(excelFile);
            Sheet dataSheet = book.getSheetAt(0);
            Iterator<Row> iterator = dataSheet.iterator();
            
            while(iterator.hasNext()) {
                Row current = iterator.next();
                Iterator<Cell> cellIterator = current.iterator();
                
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        System.out.println(currentCell.getStringCellValue() + " ");
                    }
                    else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.print(currentCell.getNumericCellValue() + "--");
                    }

                }
                System.out.println();
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XLSXReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XLSXReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    
}
