package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of key-value pairs.
 *
 * @author sebok
 */
public class AttributesCollection {

    private List<DataRow> attributes;
    private String importPath;
    private String exportPath;

    public AttributesCollection() {
        attributes = new ArrayList<>();
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

        return attributes.get(0).getAttributesAsString();
    }

    public List<DataRow> getAttributes() {
        return attributes;
    }

    public List<DataRow> getData() {
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
