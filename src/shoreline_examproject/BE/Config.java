package shoreline_examproject.BE;

import java.util.HashMap;

/**
 * Maps relations between input and output data.
 *
 * @author sebok
 */
public class Config {

    private int id;
    private String name;

    private HashMap<String, String> relations;  //Holds the relation between the input and output attributes

    public Config(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Config(String name) {
        this.name = name;
    }

    /**
     * Adds an input-export relation to this configuration.
     *
     * @param inputAttribute The attribute that was imported (from the file).
     * This will be the key.
     * @param exportAttribute The attribute that will be exported (to a JSON
     * file). This will be the value.
     */
    public void addRelation(String inputAttribute, String exportAttribute) {
        if (relations.containsKey(inputAttribute)) {
            throw new IllegalArgumentException("Should not add one attribute more than once!");
        }

        relations.put(inputAttribute, exportAttribute);
    }

    /**
     * Removes the given relation from this configuration.
     *
     * @param inputAttribute The attribute that was imported (from the file).
     * This is the key.
     * @param exportAttribute The attribute that would have been exported. This
     * is the value.
     */
    public void removeRelation(String inputAttribute, String exportAttribute) {
        if (!relations.containsKey(inputAttribute)) {
            throw new IllegalArgumentException("Can not remove not existing values!");
        }

        if (!relations.remove(inputAttribute, exportAttribute)) // If we cannot remove the specified key-value pair, notify the user.
        {
            throw new IllegalArgumentException(String.format("Could not remove specified key-value pair (Key: {%s}; Value: {%s}). Most probable cause: no value was associated with the specified key.", inputAttribute, exportAttribute));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean containsKey(String key) {
        return relations.containsKey(key);
    }

    public String getValue(String attribute) {
        if (!relations.containsKey(attribute)) {
            throw new IllegalArgumentException("Provided attribute is not found!");
        }

        return relations.get(attribute);
    }
}
