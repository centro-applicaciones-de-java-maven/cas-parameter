package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Region extends Model{
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
    
    public JSONObject setRegionId(String regionId){
        return setValue("sRegionID", regionId);
    }
    
    public String getRegionId(){
        return (String) getValue("sRegionID");
    }
    
    public JSONObject setRegionName(String regionName){
        return setValue("sRegionNm", regionName);
    }
    
    public String getRegioneName(){
        return (String) getValue("sRegionNm");
    }
    
    public JSONObject setMinimumWage(Number minimumWage){
        return setValue("nMinWages", minimumWage);
    }
    
    public Number getMinimumWage(){
        return (Number) getValue("nMinWages");
    }
    
    public JSONObject setCOLAmount(Number colAmount){
        return setValue("nColaAmtx", colAmount);
    }
    
    public Number getCOLAmount(){
        return (Number) getValue("nColaAmtx");
    }
        
    public JSONObject setMinimumWage2(Number minimumWage2){
        return setValue("nMinWage2", minimumWage2);
    }
    
    public Number getMinimumWage2(){
        return (Number) getValue("nMinWage2");
    }
    
    public JSONObject setCOLAmount2(Number colAmount2){
        return setValue("nColaAmt2", colAmount2);
    }
    
    public Number getCOLAmount2(){
        return (Number) getValue("nColaAmt2");
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