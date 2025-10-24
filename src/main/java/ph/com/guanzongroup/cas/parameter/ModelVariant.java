package ph.com.guanzongroup.cas.parameter;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.impl.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.cas.constants.Tables;
import ph.com.guanzongroup.cas.core.ObjectInitiator;
import ph.com.guanzongroup.cas.iface.ModelVariantImpl;
import ph.com.guanzongroup.cas.model.Model_Model_Variant;

public class ModelVariant extends Parameter implements ModelVariantImpl{
    Model_Model_Variant poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = (Model_Model_Variant) ObjectInitiator.createModel(poGRider, Tables.MODEL_VARIANT);
        
        super.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Description must not be empty.");
                return poJSON;
            }
            
            if (poModel.getYearModel() == 0){
                poJSON.put("result", "error");
                poJSON.put("message", "Invalid year model.");
                return poJSON;
            }
            
            if (poModel.getModelId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Model must not be empty.");
                return poJSON;
            }
            
            if (poModel.getColorId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Color must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Model_Variant getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Model Name»Variant»Year Model»Color",
                "xModelCde»xModelNme»sDescript»nYearMdlx»xColorNme",
                "IFNULL(b.sModelCde, '')»IFNULL(b.sDescript, '')»a.sDescript»a.nYearMdlx»IFNULL(c.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sVrntIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }   
    
    @Override
    public String getSQ_Browse(){
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        String lsSQL = "SELECT" +
                            "  a.sVrntIDxx" +
                            ", a.sDescript" +
                            ", a.nSelPrice" +
                            ", a.nYearMdlx" +
                            ", a.sPayloadx" +
                            ", a.sModelIDx" +
                            ", a.sColorIDx" +
                            ", a.cRecdStat" +
                            ", a.sModified" +
                            ", a.dModified" +
                            ", IFNULL(b.sModelCde, '') xModelCde" +
                            ", IFNULL(b.sDescript, '') xModelNme" +
                            ", IFNULL(c.sDescript, '') xColorNme" +
                            ", IFNULL(d.sDescript, '') xBrandNme" +
                            ", IFNULL(b.sBrandIDx, '') xBrandIDx" +
                        " FROM Model_Variant a" +
                            " LEFT JOIN Model b ON a.sModelIDx = b.sModelIDx" +
                            " LEFT JOIN Color c ON a.sColorIDx = c.sColorIDx" +
                            " LEFT JOIN Brand d ON b.sBrandIDx = d.sBrandIDx";
        
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode, String modelId) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        if (modelId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sModelIDx = " + SQLUtil.toSQL(modelId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Model Name»Variant»Year Model»Color",
                "xModelCde»xModelNme»sDescript»nYearMdlx»xColorNme",
                "IFNULL(b.sModelCde, '')»IFNULL(b.sDescript, '')»a.sDescript»a.nYearMdlx»IFNULL(c.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sVrntIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }    
    
    @Override
    public JSONObject searchRecordByBrand(String value, boolean byCode, String brandId) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "b.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Model Name»Variant»Year Model»Color",
                "xModelCde»xModelNme»sDescript»nYearMdlx»xColorNme",
                "IFNULL(b.sModelCde, '')»IFNULL(b.sDescript, '')»a.sDescript»a.nYearMdlx»IFNULL(c.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sVrntIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}