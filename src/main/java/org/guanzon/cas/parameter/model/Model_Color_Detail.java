package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.agent.services.ReferenceCache;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Color_Detail extends Model {
    
    private Model_Color poColor;
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

            ID = ("sColorIDx");
            //poColor is intentionally NOT constructed here - see Color() below, which builds it
            //lazily on first access so opening this record never touches the Color table.

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public Model_Color Color() throws SQLException, GuanzonException{
        if (poColor == null) {
            poColor = new Model_Color();
            poColor.setApplicationDriver(poGRider);
            poColor.setXML("Model_Color");
            poColor.setTableName("Color");
            poColor.initialize();
        }

        String colorCode = (String) getValue("sColorCde");

        if (!"".equals(colorCode)){
            if (poColor.getEditMode() == EditMode.READY &&
                poColor.getColorId().equals(colorCode))
                return poColor;
            else{
                if (ReferenceCache.tryLoad("Color", colorCode, poColor)) {
                    return poColor;
                }

                poJSON = poColor.openRecord(colorCode);

                if ("success".equals((String) poJSON.get("result"))){
                    ReferenceCache.store("Color", colorCode, poColor);
                    return poColor;
                }
                else {
                    poColor.initialize();
                    return poColor;
                }
            }
        } else {
            poColor.initialize();
            return poColor;
        }
    }

    public JSONObject setColorId(String colorId) {
        return setValue("sColorIDx", colorId);
    }

    public String getColorId() {
        return (String) getValue("sColorIDx");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }
    
    public JSONObject setColorCode(String colorCode) {
        return setValue("sColorCde", colorCode);
    }

    public String getColorCode() {
        return (String) getValue("sColorCde");
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
}
