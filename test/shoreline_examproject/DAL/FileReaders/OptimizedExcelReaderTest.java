/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.FileReaders;

import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_examproject.BE.AttributesCollection;

/**
 *
 * @author Dominik
 */
public class OptimizedExcelReaderTest {

    /**
     * Test of process method, of class OptimizedExcelReader.
     */
    @Test
    public void testProcess() throws Exception {
        System.out.println("process");
        String filename = "Import_data.xlsx";
        OptimizedExcelReader instance = new OptimizedExcelReader();
        AttributesCollection data = instance.process(filename);
        int expSize = 49;
        int expNumOfAttributes1 = 39;
        int expNumOfAttributes2 = 41;

        int resSize = data.getNumberOfDataEntries();
        int resNumOfAttributes = data.getAttributes().peek().getAttributesAsString().size();

        assertEquals(expSize, resSize);
        assertTrue(resNumOfAttributes == expNumOfAttributes1 || resNumOfAttributes == expNumOfAttributes2);
    }

}
