package shoreline_examproject.BE;

import java.util.HashMap;

/**
 * Represents a key-value pair.
 *
 * @author sebok
 */
public class AttributeValueMap {

    private HashMap<String, String> attributeMap;

    public AttributeValueMap() {
        this.attributeMap = new HashMap(1);
    }

    public String getValueBasedOnAttribute(String attribute) {
        return attributeMap.get(attribute);
    }

    public void addKeyValuePair(String key, String value) {
        if (attributeMap.containsKey(key)) {
            throw new IllegalArgumentException("Cannott add already existing key to the collection!");
        }

        attributeMap.put(key, value);
    }
}
