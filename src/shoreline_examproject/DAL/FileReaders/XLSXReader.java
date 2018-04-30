package shoreline_examproject.DAL.FileReaders;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import shoreline_examproject.BE.AttributeValueMap;
import shoreline_examproject.BE.AttributesCollection;

/**
 * Read in data from an XLSX file.
 *
 * @author sebok
 */
public class XLSXReader extends FileReader {

    @Override
    public AttributesCollection getData(File file) {
        
        AttributesCollection loadedAttributes = new AttributesCollection();
        
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            Row firstRow = null;
            
            if (rowIterator.hasNext()) {
                firstRow = rowIterator.next(); //Store the first row, so we can access the attribute names.
            }
            
            while (rowIterator.hasNext())   //Iterate through the rows
            {
                Row row = rowIterator.next();
                
                Iterator<Cell> cellIterator = row.cellIterator();
                AttributeValueMap current = new AttributeValueMap();

                while(cellIterator.hasNext())
                {
                    Cell c = cellIterator.next();
                    Cell attributeCell = firstRow.getCell(c.getColumnIndex());
                    
                    //System.out.print(attributeCell.getStringCellValue() + "\t");
                    switch (c.getCellTypeEnum()) {
                        case STRING:
                            //System.out.print(c.getStringCellValue() + "\t");
                            current.addKeyValuePair(attributeCell.getStringCellValue(), c.getStringCellValue());
                            break;
                        case NUMERIC:
                            //System.out.print(c.getNumericCellValue() + "\t");
                            current.addKeyValuePair(attributeCell.getStringCellValue(), Double.toString(c.getNumericCellValue()));
                            break;
                        default:
                            current.addKeyValuePair(attributeCell.getStringCellValue(), "");

                    }    
                }
                //System.out.println("");
                loadedAttributes.addAttributeMap(current);

            }
            return loadedAttributes;
        } catch (IOException ex) {
            Logger.getLogger(XLSXReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
        }
        return null;
    }
}
