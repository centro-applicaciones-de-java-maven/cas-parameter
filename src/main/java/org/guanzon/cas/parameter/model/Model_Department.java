package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.client.model.Model_Client_Master;
import org.guanzon.cas.client.services.ClientModels;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Department extends Model {

    private Model_Industry poIndustry;
    private Model_Client_Master poDeptHead;
    private Model_Client_Master poHead;
    private Model_Client_Master poSupervisor;
    private Model_Client_Master poGeneral;
    private Model_Branch poMain;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            //assign default values
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            poEntity.updateNull("sDeptHead");
            poEntity.updateNull("sMobileNo");
            poEntity.updateNull("sEMailAdd");
            poEntity.updateNull("sMainIDxx");
            poEntity.updateNull("sHAssgnID");
            poEntity.updateNull("sSAssgnID");
            poEntity.updateNull("sGenMgrID");
            poEntity.updateNull("cEntLevel");
            //end - assign default values

            ID = poEntity.getMetaData().getColumnLabel(1);

            //initialize other connections
            poIndustry = new ParamModels(poGRider).Industry();
            poDeptHead = new ClientModels(poGRider).ClientMaster();
            poHead = new ClientModels(poGRider).ClientMaster();
            poSupervisor = new ClientModels(poGRider).ClientMaster();
            poGeneral = new ClientModels(poGRider).ClientMaster();
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

    public JSONObject setMainID(String mainid) {
        return setValue("sMainIDxx", mainid);
    }

    public String getMainID() {
        return (String) getValue("sMainIDxx");
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

    public JSONObject setGeneralAssignedId(String generalassignid) {
        return setValue("sGenMgrID", generalassignid);
    }

    public String getGeneralAssignedId() {
        return (String) getValue("sGenMgrID");
    }

    public JSONObject setEntLevel(String entlevel) {
        return setValue("cEntLevel", entlevel);
    }

    public String getEntLevel() {
        return (String) getValue("cEntLevel");
    }

    public JSONObject setRecordStatus(String recordStatus) {
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

    public Model_Industry Industry() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sIndstCdx"))) {
            if (poIndustry.getEditMode() == EditMode.READY
                    && poIndustry.getIndustryId().equals((String) getValue("sIndstCdx"))) {
                return poIndustry;
            } else {
                poJSON = poIndustry.openRecord((String) getValue("sIndstCdx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poIndustry;
                } else {
                    poIndustry.initialize();
                    return poIndustry;
                }
            }
        } else {
            poIndustry.initialize();
            return poIndustry;
        }
    }

    public Model_Client_Master DepartmentHeadAssign() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sDeptHead"))) {
            if (poDeptHead.getEditMode() == EditMode.READY
                    && poDeptHead.getClientId().equals((String) getValue("sDeptHead"))) {
                return poDeptHead;
            } else {
                poJSON = poDeptHead.openRecord((String) getValue("sDeptHead"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poDeptHead;
                } else {
                    poDeptHead.initialize();
                    return poDeptHead;
                }
            }
        } else {
            poDeptHead.initialize();
            return poDeptHead;
        }
    }

    public Model_Client_Master HeadAssign() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sHAssgnID"))) {
            if (poHead.getEditMode() == EditMode.READY
                    && poHead.getClientId().equals((String) getValue("sHAssgnID"))) {
                return poHead;
            } else {
                poJSON = poHead.openRecord((String) getValue("sHAssgnID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poHead;
                } else {
                    poHead.initialize();
                    return poHead;
                }
            }
        } else {
            poHead.initialize();
            return poHead;
        }
    }

    public Model_Client_Master SupervisorAssign() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sSAssgnID"))) {
            if (poSupervisor.getEditMode() == EditMode.READY
                    && poSupervisor.getClientId().equals((String) getValue("sSAssgnID"))) {
                return poSupervisor;
            } else {
                poJSON = poSupervisor.openRecord((String) getValue("sSAssgnID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poSupervisor;
                } else {
                    poSupervisor.initialize();
                    return poSupervisor;
                }
            }
        } else {
            poSupervisor.initialize();
            return poSupervisor;
        }
    }

    public Model_Client_Master GeneralAssign() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sGenMgrID"))) {
            if (poGeneral.getEditMode() == EditMode.READY
                    && poGeneral.getClientId().equals((String) getValue("sGenMgrID"))) {
                return poGeneral;
            } else {
                poJSON = poGeneral.openRecord((String) getValue("sGenMgrID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poGeneral;
                } else {
                    poGeneral.initialize();
                    return poGeneral;
                }
            }
        } else {
            poGeneral.initialize();
            return poGeneral;
        }
    }

    public Model_Branch MainBranch() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sMainIDxx"))) {
            if (poMain.getEditMode() == EditMode.READY
                    && poMain.getBranchCode().equals((String) getValue("sMainIDxx"))) {
                return poMain;
            } else {
                poJSON = poMain.openRecord((String) getValue("sMainIDxx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poMain;
                } else {
                    poMain.initialize();
                    return poMain;
                }
            }
        } else {
            poMain.initialize();
            return poMain;
        }
    }

    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), poGRider.getBranchCode().substring(0, 1));
    }
}
