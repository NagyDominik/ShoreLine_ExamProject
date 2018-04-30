package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a key-value pair.
 *
 * @author sebok
 */
public class AttributeValueMap {

    private HashMap<String, String> attributeMap;
    private HashMap<String, Integer> keyCount;

    public AttributeValueMap()
    {
        this.attributeMap = new HashMap(10);
        this.keyCount = new HashMap();
    }

    public String getValueBasedOnAttribute(String attribute) {
        return attributeMap.get(attribute);
    }
    
    
    public void addKeyValuePair(String key, String value)
    {
        if (attributeMap.containsKey(key))      //If a key is already present, instead of overwriting it, just append a number to it (Description, Description 1, etc.).
        {
            if (this.keyCount.containsKey(key)) {
                int count = keyCount.get(key);
                count++;
                keyCount.put(key, count);
                key += " " + Integer.toString(count);
            }
            else
            {
                keyCount.put(key, 1);
                key += " 1";
            }
        }

        attributeMap.put(key, value);
    }

    @Override
    public String toString()
    {
        return attributeMap.toString();
    }
    
    public List<String> getAttributes()
    {
        List<String> keys = new ArrayList<String>();
        keys.addAll(attributeMap.keySet());
        
        return keys;
    }
    
}
