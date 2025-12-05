package org.guanzon.cas.parameter.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Company extends Model {
    private Model_TownCity poTownCity;
    
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
            
            //initialize other connections
            poTownCity = new ParamModels(poGRider).TownCity();
            //end - initialize other connections

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setCompanyId(String companyId) {
        return setValue("sCompnyID", companyId);
    }

    public String getCompanyId() {
        return (String) getValue("sCompnyID");
    }

    public JSONObject setCompanyName(String companyName) {
        return setValue("sCompnyNm", companyName);
    }

    public String getCompanyName() {
        return (String) getValue("sCompnyNm");
    }
    
    public JSONObject setCompanyCode(String companyCode) {
        return setValue("sCompnyCd", companyCode);
    }

    public String getCompanyCode() {
        return (String) getValue("sCompnyCd");
    }
    
    public JSONObject setCompanyAddress(String companyAddress) {
        return setValue("sAddressx", companyAddress);
    }

    public String getCompanyAddress() {
        return (String) getValue("sAddressx");
    }
    
    public JSONObject setCompanyTownId(String companyTownId) {       
        poJSON = setValue("sTownIDxx", companyTownId);
        
        if ("success".equals(poJSON.get("result"))){
            if (!companyTownId.isEmpty()) {
                if (poTownCity.getTownId() == null || 
                    !poTownCity.getTownId().equals(companyTownId)) {
                    
                    try {
                        poJSON = poTownCity.openRecord(companyTownId);
                        
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

    public String getCompanyTownId() {
        return (String) getValue("sTownIDxx");
    }
    
    public JSONObject setTIN(String TIN) {
        return setValue("sTaxIDNox", TIN);
    }

    public String getTIN() {
        return (String) getValue("sTaxIDNox");
    }
    
    public JSONObject setEmployerNo(String employerNo) {
        return setValue("sEmplyrNo", employerNo);
    }

    public String getEmployerNo() {
        return (String) getValue("sEmplyrNo");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }

    public JSONObject setModifyingId(String modifyingId) {
        return setValue("sModified", modifyingId);
    }

    public String getModifyingId() {
        return (String) getValue("sModified");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }
    
    public Model_TownCity TownCity() throws SQLException, GuanzonException{
        if (!getCompanyTownId().isEmpty() && poTownCity == null){
            //load the province object if null but id has a value
            setCompanyTownId(getCompanyTownId());
        }
        
        return poTownCity;
    }
    
    @Override
    public String getNextCode() {
        return "";
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
                
                setCompanyTownId((String) getValue("sTownIDxx"));
                
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
