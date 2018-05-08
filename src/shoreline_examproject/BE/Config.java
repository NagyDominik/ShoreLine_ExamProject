package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shoreline_examproject.Utility.EventLogger;

/**
 * Maps relations between input and output data.
 *  Works similarly as an AtttributeMap to allow saving multi-layered objects.
 * @author sebok
 */
public class Config {
    protected static enum DataType {NUMBER, STRING, DATE}
    
    private String name;
    private int id;
    private boolean isTreeRoot;
    private HashMap<String, ConversionData> relations; // A map of the conversion data
    private List<Config> innerConfigs;  // Make nested configs possible
    
    public Config(String name) {
        this.name = name;
        this.relations = new HashMap<>(40);
        this.innerConfigs = new ArrayList<>();
    }
    
    public Config() {
        this.relations = new HashMap<>(40);
        this.innerConfigs = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean isIsTreeRoot()
    {
        return isTreeRoot;
    }

    public void setIsTreeRoot(boolean isTreeRoot)
    {
        this.isTreeRoot = isTreeRoot;
    }
    
    public void addRelation(String key, String value)
    {
        ConversionData cd = new ConversionData(value, DataType.STRING);
        
        relations.put(key, cd);
    }
    
    public void addRelation(String key, String value, DataType outputType)
    {
        ConversionData cd = new ConversionData(value, outputType);
        
        relations.put(key, cd);
    }
    
    public void removeRelation(String key)
    {
        relations.remove(key);
    }

    public void updateValue(String key, String newValue) throws IllegalAccessException {
        ConversionData cd  = relations.get(key);
        if (cd == null) {
            EventLogger.log(EventLogger.Level.ERROR, "Invalid key!");
            throw new IllegalAccessException("Invalid key");
        }
        
        cd.setValue(newValue);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        relations.entrySet().stream().map((entry) -> {
            sb.append(entry.getKey()).append(" ->> ").append(entry.getValue().getValue());
            return entry;
        }).forEachOrdered((_item) -> {
            sb.append("\n");
        });
        return sb.toString();
    }

    
    /**
     * Contains data about the desired format of the output data after conversion;
     */
    class ConversionData {
        
        private String value; // The value of the output field
        
        private DataType outputType;

        public ConversionData(String value, DataType outputType)
        {
            this.value = value;
            this.outputType = outputType;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
        
 
    }
}
