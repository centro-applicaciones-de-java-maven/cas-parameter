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

public class Model_Department extends Model {
    private Model_Industry poIndustry;
    
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
            poIndustry = new ParamModels(poGRider).Industry();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setDepartmentId(String departmentId) {
        return setValue("sDeptIDxx", departmentId);
    }

    public String getDepartmentId() {
        return (String) getValue("sDeptIDxx");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDeptName", description);
    }

    public String getDescription() {
        return (String) getValue("sDeptName");
    }

    public JSONObject setDeptHeadId(String employeeId) {
        return setValue("sDeptHead", employeeId);
    }

    public String getDeptHeadId() {
        return (String) getValue("sDeptHead");
    }
    
    public JSONObject setDeptMobileNo(String mobileNo) {
        return setValue("sMobileNo", mobileNo);
    }

    public String getDeptMobileNo() {
        return (String) getValue("sMobileNo");
    }
    
    public JSONObject setDeptEmail(String emailAddress) {
        return setValue("sEMailAdd", emailAddress);
    }

    public String getDeptEmail() {
        return (String) getValue("sEMailAdd");
    }
    
    public JSONObject setDepartmentCode(String departmentCode) {
        return setValue("sDeptCode", departmentCode);
    }

    public String getDepartmentCode() {
        return (String) getValue("sDeptCode");
    }
    
    public JSONObject setDeptHeadAssignedId(String employeeId) {
        return setValue("sHAssgnID", employeeId);
    }

    public String getDeptHeadAssignedId() {
        return (String) getValue("sHAssgnID");
    }
    
    public JSONObject setDeptSupervisorAssignedId(String employeeId) {
        return setValue("sSAssgnID", employeeId);
    }

    public String getDeptSupervisorAssignedId() {
        return (String) getValue("sSAssgnID");
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
    
    public Model_Industry Industry() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sIndstCdx"))){
            if (poIndustry.getEditMode() == EditMode.READY && 
                poIndustry.getIndustryId().equals((String) getValue("sIndstCdx")))
                return poIndustry;
            else{
                poJSON = poIndustry.openRecord((String) getValue("sIndstCdx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poIndustry;
                else {
                    poIndustry.initialize();
                    return poIndustry;
                }
            }
        } else {
            poIndustry.initialize();
            return poIndustry;
        }
    }
    
    @Override
    public String getNextCode() {
        return "";
    }
}
