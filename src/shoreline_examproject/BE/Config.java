package shoreline_examproject.BE;

import java.util.HashSet;

/**
 * Maps relations between input and output data.
 *  Works similarly as an AtttributeMap to allow saving multi-layered objects.
 * @author sebok
 */
public class Config extends MultiLayerBase {
    
    
    private int id;
    private String name;

    public Config(String name, String key, boolean isTreeRoot)
    {
        super(key, isTreeRoot);
        this.name = name;   
    }
    
    public Config() {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
