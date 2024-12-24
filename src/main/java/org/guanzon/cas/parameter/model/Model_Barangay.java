package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Barangay extends Model{
    //other model connections
    Model_TownCity poTownCity;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateString("cBlackLst", Logical.NO);
            poEntity.updateString("cHasRoute", Logical.YES);
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            //initialize other connections
            poTownCity = new ParamModels(poGRider).TownCity();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public Model_TownCity Town(){
        if (!"".equals((String) getValue("sTownIDxx"))){
            if (poTownCity.getEditMode() == EditMode.READY && 
                poTownCity.getTownId().equals((String) getValue("sTownIDxx")))
                return poTownCity;
            else{
                poJSON = poTownCity.openRecord((String) getValue("sTownIDxx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poTownCity;
                else {
                    poTownCity.initialize();
                    return poTownCity;
                }
            }
        } else {
            poTownCity.initialize();
            return poTownCity;
        }
    }
    
    public JSONObject setBarangayId(String barangayId){
        return setValue("sBrgyIDxx", barangayId);
    }
    
    public String getBarangayId(){
        return (String) getValue("sBrgyIDxx");
    }
    
    public JSONObject setBarangayName(String barangayName){
        return setValue("sBrgyName", barangayName);
    }
    
    public String getBarangayName(){
        return (String) getValue("sBrgyName");
    }
    
    public JSONObject setTownId(String townId){
        return setValue("sTownIDxx", townId);
    }
    
    public String getTownId(){
        return (String) getValue("sTownIDxx");
    }
    
    public JSONObject hasRoute(boolean hasRoute){
        return setValue("cHasRoute", hasRoute == true ? "1" : "0");
    }
    
    public boolean hasRoute(){
        return "1".equals((String) getValue("cHasRoute"));
    }
    
    public JSONObject isBlacklisted(boolean isBlacklisted){
        return setValue("cBlackLst", isBlacklisted == true ? "1" : "0");
    }
    
    public boolean isBlacklisted(){
        return "1".equals((String) getValue("cBlackLst"));
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
