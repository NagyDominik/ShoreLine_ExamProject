package shoreline_examproject.BLL.Converters;

import shoreline_examproject.BE.AttributesCollection;

/**
 *
 * @author sebok
 */
public interface IDataConverter
{
    public AttributesCollection convertData(AttributesCollection inputData, ConvertType from, ConvertType to);
}
