package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of kex-value pairs.
 * @author sebok
 */
public class AttributesCollection
{
    private List<AttributeValueMap> attributes;

    public AttributesCollection()
    {
        attributes = new ArrayList<>();
    }
    
    public void addPair(AttributeValueMap newMap)
    {
        try{
            this.attributes.add(newMap);
        }
        catch (NullPointerException ex)
        {
            System.out.println("");
        }
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
    
    
}
