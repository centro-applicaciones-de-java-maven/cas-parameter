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

public class Model_Model_Variant extends Model {
    private Model_Model poModel;
    private Model_Color poColor;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateObject("nSelPrice", 0);
            poEntity.updateObject("nYearMdlx", 0);
            poEntity.updateObject("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            //initialize other connections
            poModel = new ParamModels(poGRider).Model();
            poColor = new ParamModels(poGRider).Color();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setVariantId(String seriesId) {
        return setValue("sVrntIDxx", seriesId);
    }

    public String getVariantId() {
        return (String) getValue("sVrntIDxx");
    }


    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }
    
    public JSONObject setSellingPrice(double price) {
        return setValue("nSelPrice", price);
    }

    public double getSellingPrice() {
        return (double) getValue("nSelPrice");
    }

    public JSONObject setYearModel(int yearModel) {
        return setValue("nYearMdlx", yearModel);
    }

    public int getYearModel() {
        return (int) getValue("nYearMdlx");
    }
    
    public JSONObject setPayload(String payload) {
        return setValue("sPayloadx", payload);
    }

    public String getPayload() {
        return (String) getValue("sPayloadx");
    }
    
    public JSONObject setModelId(String modelId) {
        return setValue("sModelIDx", modelId);
    }

    public String getModelId() {
        return (String) getValue("sModelIDx");
    }
    
    public JSONObject setColorId(String colorId) {
        return setValue("sColorIDx", colorId);
    }

    public String getColorId() {
        return (String) getValue("sColorIDx");
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
    
    public Model_Model Model() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sModelIDx"))){
            if (poModel.getEditMode() == EditMode.READY && 
                poModel.getModelId().equals((String) getValue("sModelIDx")))
                return poModel;
            else{
                poJSON = poModel.openRecord((String) getValue("sModelIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poModel;
                else {
                    poModel.initialize();
                    return poModel;
                }
            }
        } else {
            poModel.initialize();
            return poModel;
        }
    }
    
    public Model_Color Color() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sColorIDx"))){
            if (poColor.getEditMode() == EditMode.READY && 
                poColor.getColorId().equals((String) getValue("sColorIDx")))
                return poColor;
            else{
                poJSON = poColor.openRecord((String) getValue("sColorIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poColor;
                else {
                    poModel.initialize();
                    return poColor;
                }
            }
        } else {
            poColor.initialize();
            return poColor;
        }
    }
    
    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), "");
    }
}
