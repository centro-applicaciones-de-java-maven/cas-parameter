package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Region extends Model {

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
            poEntity.updateDouble("nMinWages", 0.0d);
            poEntity.updateDouble("nColaAmtx", 0.0d);
            poEntity.updateDouble("nMinWage2", 0.0d);
            poEntity.updateDouble("nColaAmt2", 0.0d);
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            ID = poEntity.getMetaData().getColumnLabel(1);

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setRegionId(String regionId) {
        return setValue("sRegionID", regionId);
    }

    public String getRegionId() {
        return (String) getValue("sRegionID");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }

    public JSONObject setMinimumWage(Double minimumWage) {
        return setValue("nMinWages", minimumWage);
    }

    public Double getMinimumWage() {
        return Double.parseDouble(getValue("nMinWages").toString());
    }

    public JSONObject setCOLAmount(Double colAmount) {
        return setValue("nColaAmtx", colAmount);
    }

    public Double getCOLAmount() {
        return Double.parseDouble(getValue("nColaAmtx").toString());
    }

    public JSONObject setMinimumWage2(Double minimumWage2) {
        return setValue("nMinWage2", minimumWage2);
    }

    public Double getMinimumWage2() {
        return Double.parseDouble(getValue("nMinWage2").toString());
    }

    public JSONObject setCOLAmount2(Double colAmount2) {
        return setValue("nColaAmt2", colAmount2);
    }

    public Double getCOLAmount2() {
        return Double.parseDouble(getValue("nColaAmt2").toString());
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
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), "");
    }
}
