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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.CellType;
import shoreline_examproject.BE.AttributesCollection;

/**
 * Read in data from an XLSX file.
 *
 * @author sebok
 */
public class XLSXReader extends FileReader {

    @Override
    public AttributesCollection getData(File file) {
        try {
            InputStream ExcelFileToRead = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

            XSSFWorkbook test = new XSSFWorkbook();

            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

            Iterator rows = sheet.rowIterator();

            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();

                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else {
                        //U Can Handel Boolean, Formula, Errors
                    }
                }
                System.out.println();
            }
            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();

                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else {
                        //U Can Handel Boolean, Formula, Errors
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
