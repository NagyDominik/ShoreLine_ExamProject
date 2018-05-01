package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of kex-value pairs.
 *
 * @author sebok
 */
public class AttributesCollection {

    private List<AttributeValueMap> attributes;

    public AttributesCollection()
    {
        attributes = new ArrayList<>();
    }
    
    public void addAttributeMap(AttributeValueMap newMap)
    {
         this.attributes.add(newMap);
    }

    @Override
    public String toString()
    {
        String s = "";
        for (AttributeValueMap attribute : attributes) {
            s += " " + attribute.toString() + "\n";
        }
        return s;
    }
    
    public List<String> getAttributes()
    {
        if (attributes == null ||attributes.isEmpty()) {
            throw new NullPointerException("Attributes list is null or empty!");
        }
        
        AttributeValueMap avm = attributes.get(0);
        return avm.getAttributes();
    }

    public List<AttributeValueMap> getAttributeValueMap() {
        return attributes;
    }
    
    
    public int getNumberOfDataEntries()
    {
        return this.attributes.size();
    }
}
