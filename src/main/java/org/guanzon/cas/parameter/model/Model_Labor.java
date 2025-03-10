package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Labor extends Model {

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            
            poEntity.updateObject("nPriceLv1", 0.00);
            poEntity.updateObject("nPriceLv2", 0.00);
            poEntity.updateObject("nPriceLv3", 0.00);
            poEntity.updateString("cInHousex", Logical.NO);
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

    public JSONObject setLaborId(String laborId) {
        return setValue("sLaborIDx", laborId);
    }

    public String getLaborId() {
        return (String) getValue("sLaborIDx");
    }

    public JSONObject setLaborName(String laborName) {
        return setValue("sLaborNme", laborName);
    }

    public String getLaborName() {
        return (String) getValue("sLaborNme");
    }
    
    public JSONObject setPriceLevel1(Number priceLevel1){
        return setValue("nPriceLv1", priceLevel1);
    }
    
    public Number getPriceLevel1(){
        return (Number) getValue("nPriceLv1");
    } 
    
    public JSONObject setPriceLevel2(Number priceLevel2){
        return setValue("nPriceLv2", priceLevel2);
    }
    
    public Number getPriceLevel2(){
        return (Number) getValue("nPriceLv2");
    }
    
    public JSONObject setPriceLevel3(Number priceLevel3){
        return setValue("nPriceLv3", priceLevel3);
    }
    
    public Number getPriceLevel3(){
        return (Number) getValue("nPriceLv3");
    }
    
    public JSONObject isInhouse(boolean isInhouse){
        return setValue("cInHousex", isInhouse ? "1" : "0");
    }

    public boolean isInhouse(){
        return ((String) getValue("cInHousex")).equals("1");
    }
    
    public JSONObject setLaborType(String laborType) {
        return setValue("cLaborTyp", laborType);
    }

    public String getLaborType() {
        return (String) getValue("cLaborTyp");
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
        return  MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
    }
}
