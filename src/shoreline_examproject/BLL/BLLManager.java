package shoreline_examproject.BLL;

import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BLL.Conversion.Converter;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.DAL.IDataAccess;

public class BLLManager implements IBLLManager {

    private IDataAccess dal;
    private Converter converter;

    //Could use dependency injection here
//    public BLLManager(IDataAccess dalm)
//    {
//        this.dal = dalm;
//    }
    
    public BLLManager() {
        this.dal = new DALManager();
        converter = new Converter();
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

    public void saveToJSON(AttributesCollection data) {
        dal.saveData(data);
    }

    @Override
    public void convertData() {
        converter.convertAll();
    }

    @Override
    public ConversionTask createConversionTask(Config c, AttributesCollection ac)
    {
        return converter.createConversionTask(c, ac);
    }

    @Override
    public void stopConversion(ConversionTask selectedItem) {
        converter.stopTask(selectedItem);
    }

    @Override
    public void pauseConversion(ConversionTask selectedItem) {
        converter.pauseTask(selectedItem);
    }

}
