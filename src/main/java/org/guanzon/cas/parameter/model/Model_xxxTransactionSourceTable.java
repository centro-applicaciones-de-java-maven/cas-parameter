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
import org.json.simple.JSONObject;

public class Model_xxxTransactionSourceTable extends Model {

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
            poEntity.updateString("cTablePri", "1");
            poEntity.updateString("cTableTyp", "0");
            poEntity.updateString("cRecdStat", "1");
           
            //end - assign default values

            ID = "sSourceCD";
            ID2 = "sTableNme";

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

    public JSONObject setTableNme(String tblName) {
        return setValue("sTableNme", tblName);
    }

    public String getTableNme() {
        return (String) getValue("sTableNme");
    }

    public JSONObject setTablePri(String tblPri) {
        return setValue("cTablePri", tblPri);
    }

    public String getTablePri() {
        return (String) getValue("cTablePri");
    }

    public JSONObject setTableType(String tblType) {
        return setValue("cTableTyp", tblType);
    }

    public String getTableType() {
        return (String) getValue("cTableTyp");
    }
    
    public JSONObject setRecordStatus(String status) {
        return setValue("cRecdStat", status);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }
    

    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
    }
    
}
