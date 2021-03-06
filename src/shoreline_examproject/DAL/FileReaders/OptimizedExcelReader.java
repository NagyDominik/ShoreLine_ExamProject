package shoreline_examproject.DAL.FileReaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import shoreline_examproject.BE.AttributeMap;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.DataRow;
import shoreline_examproject.Utility.EventLogger;

/**
 * Excel file reader that can process large XLSX files. The implementation is
 * based on:
 * http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/xssf/eventusermodel/examples/FromHowTo.java
 *
 * @author Dominik
 */
public class OptimizedExcelReader extends CustomFileReader {

    private static AttributesCollection data;
    private SheetHandler sheethandler;
    private static List<String> attributes;

    @Override
    public AttributesCollection getData(File file) {
        try {
            attributes = new ArrayList<>();
            data = new AttributesCollection();
            data.setImportPath(file.getPath());
            return process(file.getPath());
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured in the getData() method: " + ex.getMessage());
            Logger.getGlobal().logp(Level.SEVERE, OptimizedExcelReader.class.getName(), "getData", "An exception has occured in the getData() method!", ex);
        }
        return null;
    }

    public AttributesCollection process(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);

        try (OPCPackage pack = OPCPackage.open(fis)) {
            XSSFReader reader = new XSSFReader(pack);
            SharedStringsTable stringtable = reader.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(stringtable);

            InputStream sheet = reader.getSheet("rId1"); // Look up the Sheet Name / Sheet Order / rID (rId# or rSheet#)
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            data.addAttributeMap(sheethandler.getLastRow());
            return data;
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable stringtable) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        sheethandler = new SheetHandler(stringtable);
        ContentHandler handler = sheethandler;
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {

        private SharedStringsTable stringtable;
        private String lastContents;
        private boolean nextIsString;
        private DataRow row = new DataRow();
        private int cellcount = -1;
        private int currentRow = 0;
        private int repeatcount = 2;
        private String repeatingAttribute;

        private SheetHandler(SharedStringsTable stringtable) {
            this.stringtable = stringtable;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (name.equals("c")) { // c => cell
                cellcount++;
                //System.out.print(attributes.getValue("r") + " - "); // Print the cell reference, r => position of a cell
                if (splitnumber(attributes.getValue("r")) > currentRow) {
                    if (currentRow > 1) {
                        data.addAttributeMap(row);
                    }
                    row = new DataRow();
                    cellcount = 0;
                }
                currentRow = splitnumber(attributes.getValue("r"));

                String cellType = attributes.getValue("t"); // Figure out if the value is an index in the stringtable
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            lastContents = ""; // Clear contents cache
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(stringtable.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if (name.equals("v")) {
                if (currentRow == 1) {
                    checkIfExists(lastContents);
                    attributes.add(repeatingAttribute);
                } else {
                    row.addData(createAM(attributes.get(cellcount), lastContents));
                    //System.out.println(lastContents);
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents += new String(ch, start, length);
        }

        /**
         * Splits the row number from the position reference of the cell
         *
         * @param index The position of the cell
         * @return The row index of the given position
         */
        private int splitnumber(String index) {
            int splitindex = -1;
            for (int i = 0; i < index.length(); i++) {
                if (Character.isDigit(index.charAt(i))) {
                    return Integer.parseInt(index.substring(i, index.length()));
                }
            }
            return splitindex;
        }

        private AttributeMap createAM(String key, String vslue) {
            AttributeMap celldata = new AttributeMap(key, false);
            celldata.addValue(vslue);
            return celldata;
        }

        /**
         * Checks if the provided attribute is already existing and sets the new
         * attribute name to the next unused one.
         *
         * @param attribute Attribute name
         */
        private void checkIfExists(String attribute) {
            if (repeatcount == 2) {
                repeatingAttribute = attribute;
            }
            if (attributes.contains(attribute)) {
                attribute = repeatingAttribute + "(" + repeatcount + ")";
                repeatcount++;
                checkIfExists(attribute);
            } else {
                repeatingAttribute = attribute;
            }
            repeatcount = 2;
        }

        public DataRow getLastRow() {
            return row;
        }
    }

}
