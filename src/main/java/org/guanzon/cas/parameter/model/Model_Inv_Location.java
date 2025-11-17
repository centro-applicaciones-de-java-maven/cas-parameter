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

public class Model_Inv_Location extends Model {
    private Model_Warehouse poWarehouse;
    private Model_Section poSection;
    
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
            poWarehouse = new ParamModels(poGRider).Warehouse();
            poSection = new ParamModels(poGRider).Section();
            
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setLocationId(String locationId) {
        return setValue("sLocatnID", locationId);
    }

    public String getLocationId() {
        return (String) getValue("sLocatnID");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }
    
    public JSONObject setWarehouseId(String warehouseId) {
        return setValue("sWHouseID", warehouseId);
    }

    public String getWarehouseId() {
        return (String) getValue("sWHouseID");
    }
    
    public JSONObject setSectionId(String sectionId) {
        return setValue("sSectnIDx", sectionId);
    }

    public String getSectionId() {
        return (String) getValue("sSectnIDx");
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
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), "");
    }
    
    public Model_Warehouse Warehouse() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sWHouseID"))){
            if (poWarehouse.getEditMode() == EditMode.READY && 
                poWarehouse.getWarehouseId().equals((String) getValue("sWHouseID")))
                return poWarehouse;
            else{
                poJSON = poWarehouse.openRecord((String) getValue("sWHouseID"));

                if ("success".equals((String) poJSON.get("result")))
                    return poWarehouse;
                else {
                    poWarehouse.initialize();
                    return poWarehouse;
                }
            }
        } else {
            poWarehouse.initialize();
            return poWarehouse;
        }
    }
    
    public Model_Section Section() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sSectnIDx"))){
            if (poSection.getEditMode() == EditMode.READY && 
                poSection.getSectionId().equals((String) getValue("sSectnIDx")))
                return poSection;
            else{
                poJSON = poSection.openRecord((String) getValue("sSectnIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poSection;
                else {
                    poSection.initialize();
                    return poSection;
                }
            }
        } else {
            poSection.initialize();
            return poSection;
        }
    }
}
