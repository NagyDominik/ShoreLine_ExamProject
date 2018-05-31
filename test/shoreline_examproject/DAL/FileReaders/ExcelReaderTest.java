/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.FileReaders;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_examproject.BE.AttributesCollection;

/**
 *
 * @author Dominik
 */
public class ExcelReaderTest {

    /**
     * Test of getData method, of class ExcelReader.
     */
    @Test
    public void testGetData() throws Exception {
        System.out.println("getData");
        File file = new File("Import_data.xlsx");
        CustomFileReader instance = new ExcelReader();
        AttributesCollection data = instance.getData(file);
        int expSize = 49;
        int expNumOfAttributes = 41;

        int resSize = data.getNumberOfDataEntries();
        int resNumOfAttributes = data.getAttributes().peek().getAttributesAsString().size();

        assertEquals(expSize, resSize);
        assertEquals(expNumOfAttributes, resNumOfAttributes);
    }
    
}
