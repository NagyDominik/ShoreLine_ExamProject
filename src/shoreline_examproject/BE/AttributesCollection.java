package shoreline_examproject.BE;

import java.util.List;

/**
 * Represents a collection of kex-value pairs.
 *
 * @author sebok
 */
public class AttributesCollection {

    private List<AttributeValueMap> attributes;

    public AttributesCollection() {
    }

    public void addPair(AttributeValueMap newMap) {
        this.attributes.add(newMap);
    }
}
