package shoreline_examproject.BE;

import java.util.HashMap;

/**
 * Represents a collection of attributes, and their associated values.
 * @author sebok
 */
public class AttributesCollection
{
    private HashMap<String, String> attributeMap;

    public AttributesCollection()
    {
        this.attributeMap = new HashMap();
    }
    
    
    public String getValueBasedOnAttribute(String attribute)
    {
        return attributeMap.get(attribute);
    }
    
    
    public void addKeyValuePair(String key, String value)
    {
        if (attributeMap.containsKey(key))
        {
            throw new IllegalArgumentException("Cannott add already existing key to the collection!");
        }
        
        attributeMap.put(key, value);
    }
}
