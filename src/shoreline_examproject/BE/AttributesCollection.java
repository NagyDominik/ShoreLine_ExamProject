package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of kex-value pairs.
 *
 * @author sebok
 */
public class AttributesCollection {

    private List<DataRow> attributes;

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
    
}
