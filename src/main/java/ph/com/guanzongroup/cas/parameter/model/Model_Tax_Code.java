package ph.com.guanzongroup.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Tax_Code extends Model {

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            
            poEntity.updateObject("dLastUpdt", poGRider.getServerDate());
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


    
    public JSONObject setTaxCode(String taxCode) {
        return setValue("sTaxCodex", taxCode);
    }

    public String getTaxCode() {
        return (String) getValue("sTaxCodex");
    }

    public JSONObject setRegularRate(double rate) {
        return setValue("sRegRatex", rate);
    }

    public double getRegularRate() {
        return Double.parseDouble(String.valueOf(getValue("sRegRatex")));
    }
    
    public JSONObject setGovernmentRate(double rate) {
        return setValue("sGovtRate", rate);
    }

    public double getGovernmentRate() {
        return Double.parseDouble(String.valueOf(getValue("sGovtRate")));
    }
    
    public JSONObject setLastUpdate(Date lastUpdate) {
        return setValue("dLastUpdt", lastUpdate);
    }

    public Date getLastUpdate() {
        return (Date) getValue("dLastUpdt");
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
    
    @Override
    public String getNextCode(){
        return ""; 
    }
}
