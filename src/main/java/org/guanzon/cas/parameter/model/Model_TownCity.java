package org.guanzon.cas.parameter.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_TownCity extends Model{
    //other model connections
    private Model_Province poProvince;
    
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
            poProvince = new ParamModels(poGRider).Province();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setTownId(String townId){
        return setValue("sTownIDxx", townId);
    }
    
    public String getTownId(){
        return (String) getValue("sTownIDxx");
    }
    
    public JSONObject setDescription(String townName){
        return setValue("sTownName", townName);
    }
    
    public String getDescription(){
        return (String) getValue("sTownName");
    }
    
    public JSONObject setZipCode(String zipCode){
        return setValue("sZippCode", zipCode);
    }
    
    public String getZipCode(){
        return (String) getValue("sZippCode");
    }
    
    public JSONObject setProvinceId(String provinceId){     
        poJSON = setValue("sProvIDxx", provinceId);
        
        if ("success".equals(poJSON.get("result"))){
            if (!provinceId.isEmpty()) {
                if (poProvince.getProvinceId() == null ||
                    !poProvince.getProvinceId().equals(provinceId)) {
                    
                    try {
                        poJSON = poProvince.openRecord(provinceId);
                        
                        if (!"success".equals(poJSON.get("result"))){
                            return poJSON;
                        }
                    } catch (SQLException | GuanzonException e) {
                        poJSON = new JSONObject();
                        poJSON.put("result", "error");
                        poJSON.put("message", e.getMessage());
                        return poJSON;
                    }
                }
            }
        }
        
        return poJSON;
    }
    
    public String getProvinceId(){
        return (String) getValue("sProvIDxx");
    }
    
    public JSONObject setMunicpalCode(String municipalCode){
        return setValue("sMuncplCd", municipalCode);
    }
    
    public String getMunicpalCode(){
        return (String) getValue("sMuncplCd");
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
    
    public Model_Province Province() throws SQLException, GuanzonException{
        if (!getProvinceId().isEmpty() && poProvince == null){
            //load the province object if null but id has a value
            setProvinceId(getProvinceId());
        }
        
        return poProvince;
    }
    
    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), "");
    }
    
    @Override
    public JSONObject openRecord(String id) throws SQLException, GuanzonException {
        poJSON = new JSONObject();

        String lsSQL = MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, ID + " = " + SQLUtil.toSQL(id));

        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()) {
                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++) {
                    setValue(lnCtr, loRS.getObject(lnCtr));
                }
                
                MiscUtil.close(loRS);               
                
                setProvinceId((String) getValue("sProvIDxx"));
                
                pnEditMode = EditMode.READY;

                poJSON = new JSONObject();
                poJSON.put("result", "success");
                poJSON.put("message", "Record loaded successfully.");
            } else {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "No record to load.");
            }
        } catch (SQLException e) {
            logError(getCurrentMethodName() + "»" + e.getMessage());
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }
}
