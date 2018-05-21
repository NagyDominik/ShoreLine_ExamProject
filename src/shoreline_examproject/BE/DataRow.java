package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one row of data (in the case of an .xlsx file)
 *
 * @author sebok
 */
public class DataRow {

    //private HashSet<AttributeMap> data;
    private List<AttributeMap> data;
    
    private List<AttributeMap> removeList;
    
    public DataRow() {
        //data = new HashSet<>();
        data = new ArrayList<>();
        removeList = new ArrayList<>();
    }

    public void addData(AttributeMap data) {
        this.data.add(data);
    }

    public List<String> getAttributesAsString() {
        if (data == null || data.isEmpty()) {
            throw new NullPointerException("Attributes list is null or empty!");
        }

        List<String> attributesString = new ArrayList<>();

        for (AttributeMap attribute : data) {
            attributesString.addAll(attribute.getAttributes());
        }

        return attributesString;
    }

    public List<AttributeMap> getData()
    {
        //return new ArrayList<>(this.data);
        return data;
    }
    
    public void addToRemoveList(AttributeMap am) {
        this.removeList.add(am);
    }
    
    public void remove() {
        for (AttributeMap attributeMap : removeList) {
            data.remove(attributeMap);
        }
        removeList.clear();
    }
}
