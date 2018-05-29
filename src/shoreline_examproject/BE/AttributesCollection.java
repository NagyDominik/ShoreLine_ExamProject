package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a collection of key-value pairs.
 *
 * @author sebok
 */
public class AttributesCollection {

    private ConcurrentLinkedQueue<DataRow> attributes;
    private String importPath;
    private String exportPath;

    public AttributesCollection() {
        attributes = new ConcurrentLinkedQueue<>();
    }

    public void addAttributeMap(DataRow newData) {
        this.attributes.add(newData);
    }

    public int getNumberOfDataEntries() {
        return this.attributes.size();
    }

    public List<String> getAttributesAsString() {
        if (attributes == null || attributes.isEmpty()) {
            throw new NullPointerException("Attributes list is empty or null!");
        }

        return attributes.peek().getAttributesAsString();
    }

    public ConcurrentLinkedQueue<DataRow> getAttributes() {
        return attributes;
    }

    public ConcurrentLinkedQueue<DataRow> getData() {
        return this.attributes;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
    
}
