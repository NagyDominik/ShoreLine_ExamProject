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

    private List<DataPair> data = new ArrayList<>(15);

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

    public void addRelation(String key, String value, boolean isPlanning, boolean isDef) {
        // TODO: dummy method, finish this (optimize?).
        DataPair dataPair = new DataPair(Type.STRING, key, value, isPlanning);
        dataPair.setDefault(isDef);
        this.data.add(dataPair);
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
            if (!dataPair.isPlanning) {
                sb.append(dataPair.outputName).append("->> ").append(dataPair.inputName).append("\n");
            }
            else {
                sb.append("\t");
                sb.append(dataPair.outputName).append("->> ").append(dataPair.inputName).append("\n");
            }
        }

        return sb.toString();
    }
    
    void addDefaultValuesToDataRow(DataRow convertedRow) {
        for (DataPair dataPair : data) {
            if (dataPair.isDefault) {
                AttributeMap map;
                if (dataPair.isPlanning) {
                    map = new AttributeMap(null, true);
                    AttributeMap ac = new AttributeMap(dataPair.outputName, false);
                    ac.addValue(dataPair.inputName);
                    map.addValue(ac);
                }
                else {
                    map= new AttributeMap(dataPair.outputName, false);
                    map.addValue(dataPair.inputName);
                }
                convertedRow.addData(map);
            }
        }
    }


    private class DataPair implements Serializable{

        private final Type outputType;

        private String inputName;
        private String outputName;
        private boolean isPlanning;
        private boolean isDefault;
        
        public DataPair(Type outputType, String oldName, String newName, boolean isPlanning) {
            this.outputType = outputType;
            this.inputName = newName;
            this.outputName = oldName;
            this.isPlanning = isPlanning;
            this.isDefault =false;
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

        public boolean isDefault() {
            return this.isDefault;
        }
        
        public void setDefault(boolean def) {
            this.isDefault = def;
        }
    }
}
