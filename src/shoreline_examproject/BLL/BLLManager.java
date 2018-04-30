package shoreline_examproject.BLL;

import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BLL.Converters.ConvertType;
import shoreline_examproject.BLL.Converters.IDataConverter;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.DAL.IDataAccess;


public class BLLManager implements BLLInterface
{

    private IDataAccess dal;
    private IDataConverter converter;
    
    //Could use dependency injection here
        
//    public BLLManager(IDataAccess dalm)
//    {
//        this.dal = dalm;
//    }
    
    
    public BLLManager()
    {
        this.dal = new DALManager();
    }
    
    
    @Override
    public void saveLog(EventLog log)
    {
        dal.saveLog(log);
    }

    @Override
    public void saveConfig(Config config)
    {
        dal.saveConfig(config);
    }

    @Override
    public AttributesCollection loadFileData(String filePath)
    {
        return dal.loadFileData(filePath);
    }
//
//    @Override
//    public AttributesCollection convertData(AttributesCollection inputData, ConvertType from, ConvertType to)
//    {
//        return converter.convertData(inputData, from, to);
//    }

    @Override
    public AttributesCollection convertData(AttributesCollection inputData, ConvertType from, ConvertType to)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
