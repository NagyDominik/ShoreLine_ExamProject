/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.FileReaders;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dominik
 */
public class FileReaderFactoryTest {

    /**
     * Test of CreateFileReader method, of class FileReaderFactory.
     */
    @Test
    public void testCreateFileReader() {
        System.out.println("CreateFileReader");
        String path1 = "Filename.xlsx";
        String path2 = "Filename.xls";

        FileReader result1 = FileReaderFactory.CreateFileReader(path1);
        FileReader result2 = FileReaderFactory.CreateFileReader(path2);
        assertTrue(result1 instanceof OptimizedExcelReader);
        assertTrue(result2 instanceof ExcelReader);
    }

}
