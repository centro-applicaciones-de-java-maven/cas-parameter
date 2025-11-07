package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_xxxTransactionSource extends Model {

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
            poEntity.updateString("cTranType", Logical.NO);
            poEntity.updateString("cTranType", Logical.NO);
            poEntity.updateString("cWPayType", Logical.NO);
            poEntity.updateNull("sClientTp");
            poEntity.updateObject("dModified", poGRider.getServerDate());
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            ID = poEntity.getMetaData().getColumnLabel(1);

            //initialize other connections
            //end - initialize other connections
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setSourceCode(String sourceCode) {
        return setValue("sSourceCD", sourceCode);
    }

    public String getSourceCode() {
        return (String) getValue("sSourceCD");
    }

    public JSONObject setSourceName(String sourceName) {
        return setValue("sSourceNm", sourceName);
    }

    public String getSourceName() {
        return (String) getValue("sSourceNm");
    }

    public JSONObject setClientType(String clientType) {
        return setValue("sClientTp", clientType);
    }

    public String getClientType() {
        return (String) getValue("sClientTp");
    }

    public JSONObject setTransactionType(String clientType) {
        return setValue("cTranType", clientType);
    }

    public String getTransactionType() {
        return (String) getValue("cTranType");
    }
    

    public JSONObject setWithPayType(String clientType) {
        return setValue("cWPayType", clientType);
    }

    public String getWithPayType() {
        return (String) getValue("cWPayType");
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

    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
    }
}
