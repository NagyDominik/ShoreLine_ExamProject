package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import shoreline_examproject.Utility.EventLogger;

/**
 * Represents either a single key-value pair or a tree-like structure.
 *
 * @author sebok
 */
public class AttributeMap {

    private String key;
    private boolean isTreeRoot;

    private String value; // Used if the object represents a single key-value pair.
    //  private HashSet<AttributeMap> values; // Used if the object represent a tree-like structure of data
    private List<AttributeMap> values; // Used if the object represent a tree-like structure of data

    public AttributeMap(String key, boolean isTreeRoot) {
        this.key = key;
        this.isTreeRoot = isTreeRoot;

        if (!isTreeRoot) {
            values = null;
        } else {
            value = null;
            values = new ArrayList<>(10);
        }
    }

    public AttributeMap() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isTreeRoot) {
            sb.append(String.format("%s -> %s\n", key, value));
        } else {
            values.forEach((value1) -> {
                sb.append("\t");
                sb.append(value1.toString());
            });
        }
        return sb.toString();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIsTreeRoot(boolean isTR) {
        if (!isTR) {
            this.values = null;
            this.isTreeRoot = isTR;
        } else {
            this.value = null;
            this.values = new ArrayList<>(10);
            this.isTreeRoot = isTR;
        }
    }

    public void addValue(String value) {
        if (!isTreeRoot) {
            this.value = value;
        }
    }

    public String getKey() {
        return key;
    }

    public boolean isIsTreeRoot() {
        return isTreeRoot;
    }

    /**
     * Retrieves the value of this instance if this instance is not a tree root.
     *
     * @return The value stored in this instance.
     * @throws NoSuchFieldException If the instance is a tree root, and thus the
     * value is null.
     */
    public String getValue() throws NoSuchFieldException {
        if (isTreeRoot) {
            if (values.size() == 1) {
                return values.get(0).getValue();
            }
        } else {
            return value;
        }

        return null;
    }

    /**
     * Retrieves the collection of AttributeMap objects that are mapped to this
     * instance (if this instance is a tree root).
     *
     * @return The AttributeMap objects that are mapped to this instance.
     * @throws NoSuchFieldException If the instance is not a tree root.
     */
    public List<AttributeMap> getValues() throws NoSuchFieldException {
        if (!isTreeRoot) {
            EventLogger.log(EventLogger.Level.ERROR, "Attempted to access the values collection of a node that is not a tree root!");
            throw new NoSuchFieldException("This instance represents a single key-value pair.");
        }
        return values;
    }

    public List<String> getAttributes() {
        List<String> attributes = new ArrayList<>();
        if (!isTreeRoot) {
            attributes.add(key);
        } else {
            for (AttributeMap am : values) {
                attributes.addAll(am.getAttributes());
            }
        }
        return attributes;
    }

    public void addValue(AttributeMap convertMap) {
        if (!this.values.add(convertMap)) {
            throw new IllegalArgumentException("Could not add attribute map to this instance!");
        }
    }
    
}
