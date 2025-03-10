package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Banks_Branch extends Model {
private Model_Banks poBanks;
private Model_TownCity poTown;
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
            poBanks = new ParamModels(poGRider).Banks();
            poTown = new ParamModels(poGRider).TownCity();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public Model_Banks Banks() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sBankIDxx"))){
            if (poBanks.getEditMode() == EditMode.READY && 
                poBanks.getBankID().equals((String) getValue("sBankIDxx")))
                return poBanks;
            else{
                poJSON = poBanks.openRecord((String) getValue("sBankIDxx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poBanks;
                else {
                    poBanks.initialize();
                    return poBanks;
                }
            }
        } else {
            poBanks.initialize();
            return poBanks;
        }
    }
    
    public Model_TownCity TownCity() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sTownIDxx"))){
            if (poTown.getEditMode() == EditMode.READY && 
                poTown.getTownId().equals((String) getValue("sTownIDxx")))
                return poTown;
            else{
                poJSON = poTown.openRecord((String) getValue("sTownIDxx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poTown;
                else {
                    poTown.initialize();
                    return poTown;
                }
            }
        } else {
            poTown.initialize();
            return poTown;
        }
    }
    
    public JSONObject setBranchBankID(String branchbankId) {
        return setValue("sBrBankID", branchbankId);
    }

    public String getBranchBankID() {
        return (String) getValue("sBrBankID");
    }

    public JSONObject setBranchBankName(String branchBankName) {
        return setValue("sBrBankNm", branchBankName);
    }

    public String getBranchBankName() {
        return (String) getValue("sBrBankNm");
    }
    
    public JSONObject setBranchBankCode(String branchbankCode) {
        return setValue("sBrBankCD", branchbankCode);
    }

    public String getBranchBankCode() {
        return (String) getValue("sBrBankCD");
    }

    public JSONObject setBankID(String bankId) {
        return setValue("sBankIDxx", bankId);
    }

    public String getBankID() {
        return (String) getValue("sBankIDxx");
    }
    
    public JSONObject setContactPerson(String contactPerson) {
        return setValue("sContactP", contactPerson);
    }

    public String getContactPerson() {
        return (String) getValue("sContactP");
    }
    
    public JSONObject setAddress(String adress) {
        return setValue("sAddressx", adress);
    }

    public String getAddress() {
        return (String) getValue("sAddressx");
    }
    
    public JSONObject setTownID(String townID) {
        return setValue("sTownIDxx", townID);
    }

    public String getTownID() {
        return (String) getValue("sTownIDxx");
    }
    
    public JSONObject setTelephoneNo(String telephoneNo) {
        return setValue("sTelNoxxx", telephoneNo);
    }

    public String getTelephoneNo() {
        return (String) getValue("sTelNoxxx");
    }
    
    public JSONObject setFaxNo(String faxNo) {
        return setValue("sFaxNoxxx", faxNo);
    }

    public String getFaxNo() {
        return (String) getValue("sFaxNoxxx");
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
    @Override
    public String getNextCode() {
        return   MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
    }
}
