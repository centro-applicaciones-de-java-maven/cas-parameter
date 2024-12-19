package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Brand extends Model {
@Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setBrandId(String brandId) {
        return setValue("sModelIDx", brandId);
    }

    public String getBrandId() {
        return (String) getValue("sModelIDx");
    }

    public JSONObject setModelCode(String modelCode) {
        return setValue("sModelIDx", modelCode);
    }

    public String getModelCode() {
        return (String) getValue("sModelIDx");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }

//    public JSONObject setBrandId(String brandId) {
//        return setValue("sBrandIDx", brandId);
//    }
//
//    public String getBrandId() {
//        return (String) getValue("sBrandIDx");
//    }

    public JSONObject setSeriesId(String seriesId) {
        return setValue("sSeriesID", seriesId);
    }

    public String getSeriesId() {
        return (String) getValue("sSeriesID");
    }

    public JSONObject setYearModel(Integer yearModel) {
        return setValue("nYearModel", yearModel);
    }

    public Integer getYearModel() {
        return (Integer) getValue("nYearModel");
    }

    public JSONObject setEndOfLife(String endOfLife) {
        return setValue("cEndOfLfe", endOfLife);
    }

    public String getEndOfLife() {
        return (String) getValue("cEndOfLfe");
    }    
    
    public JSONObject setRecordStatus(String recordStatus) {
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    } 
    
    public JSONObject setModifyingId(String modifyingId){
        return setValue("sModified", modifyingId);
    }
    
    public String getModifyingId(){
        return (String) getValue("sModified");
    }
    
    public JSONObject setModifiedDate(Date modifiedDate){
        return setValue("dModified", modifiedDate);
    }
    
    public Date getModifiedDate(){
        return (Date) getValue("dModified");
    }
}
