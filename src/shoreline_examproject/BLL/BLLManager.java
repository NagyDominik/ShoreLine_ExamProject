package shoreline_examproject.BLL;

import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.DAL.IDataAccess;

public class BLLManager implements IBLLManager {

    private IDataAccess dal;

    //Could use dependency injection here
//    public BLLManager(IDataAccess dalm)
//    {
//        this.dal = dalm;
//    }
    
    public BLLManager() {
        this.dal = new DALManager();
        saveToJSON(dal.loadFileData("Import_data.xlsx"));
    }

    @Override
    public void saveLog(EventLog log) {
        dal.saveLog(log);
    }

    @Override
    public void saveConfig(Config config) {
        dal.saveConfig(config);
    }

    @Override
    public AttributesCollection loadFileData(String filePath) {
        return dal.loadFileData(filePath);
    }

    @Override
    public AttributesCollection convertData(AttributesCollection inputData, Config config) {
        return null;
    }

    public void saveToJSON(AttributesCollection data) {
        dal.saveData(data);
    }

}
