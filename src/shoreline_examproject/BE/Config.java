package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps relations between input and output data.
 *  Works similarly as an AtttributeMap to allow saving multi-layered objects.
 * @author sebok
 */
public class Config {

    private enum Type {STRING, INT, DATE}
    private String name;
    
    List<DataPair> data = new ArrayList<>(15);
    
    public boolean containsKey(String key) {
        for (DataPair dataPair : data) {
            if (dataPair.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public String getNewKey(String oldKey) {
        for (DataPair dataPair : data) {
            if (dataPair.containsKey(oldKey)) {
                return dataPair.getNewValue();
            }
        }
        
        throw new IllegalArgumentException("This object does not contain the data associated with the provided key!");
    }

    public void addRelation(String key, String value)
    {
        // TODO: dummy method, finish this (optimize?).
        this.data.add(new DataPair(false, Type.STRING, key, value));
    }
    
    public void updateValue(String key, String newValue)
    {
        for (DataPair dataPair : data) {
            if (dataPair.containsKey(key)) {
                dataPair.updateValue(newValue);
                return;
            }
        }
        
        throw new IllegalArgumentException("This object does not contain the data associated with the provided key!");
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (DataPair dataPair : data) {
            sb.append(dataPair.inputName).append("->> ").append(dataPair.outputName).append("\n");
        }
        
        return sb.toString();
    }
    
    
    
    class DataPair
    {
        boolean isPlanning;
        
        private Type outputType;
        
        private String inputName;
        private String outputName;

        public DataPair(boolean isPlanning, Type outputType, String inputName, String outputName)
        {
            this.isPlanning = isPlanning;
            this.outputType = outputType;
            this.inputName = inputName;
            this.outputName = outputName;
        }
        
        public boolean containsKey(String key) {
           return this.inputName.equals(key);
        }
        
        
        public String getNewValue() {
            return this.outputName;
        }

        private void updateValue(String newValue)
        {
            this.outputName = newValue;
        }

        public String getInputName()
        {
            return inputName;
        }

        public String getOutputName()
        {
            return outputName;
        }
    }
}
