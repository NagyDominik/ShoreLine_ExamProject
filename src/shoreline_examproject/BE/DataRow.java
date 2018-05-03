package shoreline_examproject.BE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents one row of data (in the case of an .xlsx file)
 *
 * @author sebok
 */
public class DataRow {

    private HashSet<AttributeMap> data;

    public DataRow() {
        data = new HashSet<>();
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
        return new ArrayList<>(this.data);
    }
}
