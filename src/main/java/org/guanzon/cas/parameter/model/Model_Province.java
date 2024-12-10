package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Province extends Model{
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);

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
    
    public JSONObject setProvinceId(String provinceId){
        return setValue("sProvIDxx", provinceId);
    }
    
    public String getProvinceId(){
        return (String) getValue("sProvIDxx");
    }
    
    public JSONObject setProvinceName(String provinceName){
        return setValue("sProvName", provinceName);
    }
    
    public String getProvinceName(){
        return (String) getValue("sProvName");
    }
    
    public JSONObject setRegionId(String regionId){
        return setValue("sRegionID", regionId);
    }
    
    public String getRegionId(){
        return (String) getValue("sRegionID");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }
    
    public String getRecordStatus(){
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
