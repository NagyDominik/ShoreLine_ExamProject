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
    
    public Config(String name) {
        this.name = name;
        this.relations = new HashMap<>(40);
    }
    
    public Config() {
        this.relations = new HashMap<>(40);
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

    public String getNewKey(String oldKey) throws IllegalAccessException
    {
        ConversionData cd = relations.get(oldKey);
        if (cd == null) {
            EventLogger.log(EventLogger.Level.ERROR, "Invalid key!");
            throw new IllegalAccessException("Invalid key");
        }
        
        return cd.getValue();
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
        for (Map.Entry<String, ConversionData> entry : relations.entrySet()) {
            if (!entry.getValue().isTreeRoot()) {
                sb.append(String.format("%s ->> %s\n", entry.getKey(), entry.getValue().getValue()));
            }
            else {
                sb.append(entry.getKey());
                sb.append("\n");
                for (Config innerConfig : entry.getValue().getInnerConfigs()) {
                    sb.append("\t");    // Only indents the first entry.
                    sb.append(innerConfig.toString());
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * Designate the ConversionData with the provided key as a tree root.
     * @param key The provided key.
     */
    public void designateAsRoot(String key) throws IllegalAccessException
    {
        ConversionData cd  = relations.get(key);
        if (cd == null) {
            EventLogger.log(EventLogger.Level.ERROR, "Invalid key!");
            throw new IllegalAccessException("Invalid key");
        }   
        
        cd.designateAsRoot();
        
    }
    
    /**
     * Contains data about the desired format of the output data after conversion;
     */
    class ConversionData {
        
        private String value; // The value of the output field
        
        private boolean isTreeRoot;
        
        private List<Config> innerConfigs; 
        
        private final DataType outputType;

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

        public boolean isTreeRoot()
        {
            return isTreeRoot;
        }        

        public List<Config> getInnerConfigs()
        {
            return innerConfigs;
        }
        
        /**
         * Designate this instance as a tree root.
         */
        public void designateAsRoot()
        {
            this.isTreeRoot = true;
            this.innerConfigs = new ArrayList<>(5);
            this.value = null;
        }

        /**
         * If this instance is a tree rot, make it a non-tree root.
         */
        public void disableTreeRoot()
        {
            if (isTreeRoot) {
                this.isTreeRoot = false;
                this.innerConfigs = null;
            }
        }
    }
}
