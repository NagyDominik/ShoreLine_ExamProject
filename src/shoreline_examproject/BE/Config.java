package shoreline_examproject.BE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps relations between input and output data. Works similarly as an
 * AtttributeMap to allow saving multi-layered objects.
 *
 * @author sebok
 */
public class Config implements Serializable {

    private static final long serialVersionUID = 123L;

    private enum Type {
        STRING, INT, DATE
    }
    private String name;
    private int id;

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

    public void addRelation(String key, String value, boolean isPlanning) {
        // TODO: dummy method, finish this (optimize?).
        this.data.add(new DataPair(Type.STRING, key, value, isPlanning));
    }
    
    boolean isPlanning(String oldKey) {
        for (DataPair dataPair : data) {
            if (dataPair.containsKey(oldKey)) {
                return dataPair.isPlanning;
            }
        }
        return false;
    }
    

    public void updateOutputName(String value, String key) {
        for (DataPair dataPair : data) {
            if (dataPair.hasValue(value)) {
                dataPair.updateKey(key);
                return;
            }
        }
    }

//    public void updateValue(String key, String newValue)
//    {
//        for (DataPair dataPair : data) {
//            if (dataPair.containsKey(key)) {
//                dataPair.updateValue(newValue);
//                return;
//            }
//        }
//        
//        throw new IllegalArgumentException("This object does not contain the data associated with the provided key!");
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getAssociationMap() {
        StringBuilder sb = new StringBuilder();
        for (DataPair dataPair : data) {
            sb.append(dataPair.outputName).append("->> ").append(dataPair.inputName).append("\n");
        }

        return sb.toString();
    }

    private class DataPair implements Serializable{

        private final Type outputType;

        private String inputName;
        private String outputName;
        private boolean isPlanning;

        public DataPair(Type outputType, String oldName, String newName, boolean isPlanning) {
            this.outputType = outputType;
            this.inputName = newName;
            this.outputName = oldName;
            this.isPlanning = isPlanning;
        }

        public boolean containsKey(String key) {
            return this.inputName.equals(key);
        }

        public String getNewValue() {
            return this.outputName;
        }

        private void updateKey(String key) {
            this.inputName = key;
        }

        public String getInputName() {
            return inputName;
        }

        public String getOutputName() {
            return outputName;
        }

        public boolean hasValue(String value) {
            return this.outputName.equals(value);
        }

        public boolean isPlanning() {
            return isPlanning;
        }

        public void setIsPlanning(boolean isPlanning) {
            this.isPlanning = isPlanning;
        }
        
    }
}
