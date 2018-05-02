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
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.DataRow;
import shoreline_examproject.BE.AttributeMap;

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
                AttributeMap current = null;
                
                DataRow dataRow = new DataRow();
                
                while(cellIterator.hasNext())
                {
                    Cell c = cellIterator.next();
                    Cell attributeCell = firstRow.getCell(c.getColumnIndex());
                    
                    //System.out.print(attributeCell.getStringCellValue() + "\t");
                    switch (c.getCellTypeEnum()) {
                        case STRING:
                            //System.out.print(c.getStringCellValue() + "\t");
                            //current.addKeyValuePair(attributeCell.getStringCellValue(), c.getStringCellValue());
                            current = new AttributeMap(attributeCell.getStringCellValue(), false);
                            current.setValue(c.getStringCellValue());
                            dataRow.addData(current);
                            break;
                        case NUMERIC:
                            //System.out.print(c.getNumericCellValue() + "\t");
                            //current.addKeyValuePair(attributeCell.getStringCellValue(), Double.toString(c.getNumericCellValue()));
                             current = new AttributeMap(attributeCell.getStringCellValue(), false);
                             current.setValue(Double.toString(c.getNumericCellValue()));
                             dataRow.addData(current);
                            break;
                        default:
                            //current.addKeyValuePair(attributeCell.getStringCellValue(), "");
                            current = new AttributeMap(attributeCell.getStringCellValue(), false);
                            current.setValue("");
                            dataRow.addData(current);
                    }    
                }
                //System.out.println("");
                loadedAttributes.addAttributeMap(dataRow);

            }
            return loadedAttributes;
        } catch (IOException ex) {
            Logger.getLogger(XLSXReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
        }
        return null;
    }
}
