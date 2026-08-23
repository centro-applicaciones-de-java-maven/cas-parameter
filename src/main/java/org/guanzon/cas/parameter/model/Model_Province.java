package org.guanzon.cas.parameter.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.agent.services.ReferenceCache;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Province extends Model{
    //other model connections
    private Model_Region poRegion;
    
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

            
            //poRegion is intentionally NOT constructed here - see Region()/setRegionId() below,
            //which build it lazily on first access so opening this record never touches Region.
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
       
    public JSONObject setProvinceId(String provinceId){
        return setValue("sProvIDxx", provinceId);
    }
    
    public String getProvinceId(){
        return (String) getValue("sProvIDxx");
    }
    
    public JSONObject setDescription(String description){
        return setValue("sDescript", description);
    }
    
    public String getDescription(){
        return (String) getValue("sDescript");
    }
    
    public JSONObject setRegionId(String regionId){
        if (poRegion == null) {
            poRegion = new Model_Region();
            poRegion.setApplicationDriver(poGRider);
            poRegion.setXML("Model_Region");
            poRegion.setTableName("Region");
            poRegion.initialize();
        }

        poJSON = setValue("sRegionID", regionId);

        if ("success".equals(poJSON.get("result"))){
            if (!regionId.isEmpty()) {
                if (poRegion.getRegionId()== null ||
                    !poRegion.getRegionId().equals(regionId)) {

                    try {
                        //Region is a small, rarely-changing parameter table - serve repeat
                        //lookups for the same id from memory instead of hitting the database.
                        if (!ReferenceCache.tryLoad("Region", regionId, poRegion)) {
                            poJSON = poRegion.openRecord(regionId);

                            if (!"success".equals(poJSON.get("result"))){
                                return poJSON;
                            }

                            ReferenceCache.store("Region", regionId, poRegion);
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
    
    public String getRegionId(){
        return (String) getValue("sRegionID");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }
    
    public String getRecordStatus(){
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
    
    public Model_Region Region() throws SQLException, GuanzonException{
        if (poRegion == null) {
            poRegion = new Model_Region();
            poRegion.setApplicationDriver(poGRider);
            poRegion.setXML("Model_Region");
            poRegion.setTableName("Region");
            poRegion.initialize();

            if (!getRegionId().isEmpty()){
                //load the region object if just constructed but id has a value
                setRegionId(getRegionId());
            }
        }

        return poRegion;
    }
    
    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), "");
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
                
                setRegionId((String) getValue("sRegionID"));
                
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