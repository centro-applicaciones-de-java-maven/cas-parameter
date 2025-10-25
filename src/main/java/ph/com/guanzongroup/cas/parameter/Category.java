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
import ph.com.guanzongroup.cas.iface.CategoryImpl;
import ph.com.guanzongroup.cas.model.Model_Category;

public class Category extends Parameter implements CategoryImpl{
    Model_Category poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = ObjectInitiator.createModel(Model_Category.class, poGRider, Tables.CATEGORY);
        
        super.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException {
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getCategoryId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Country must not be empty.");
                return poJSON;
            }
            
            if (poModel.getDescription() == null ||  poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Nationality must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Category getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{

        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    value,
                    "ID»Description»Industry»Inv. Type",
                    "a.sCategrCd»a.sDescript»xIndstDsc»xInvTypNm",
                    "a.sCategrCd»a.sDescript»b.sDescript»c.sDescript",
                    byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sCategrCd"));
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
                            "  a.sCategrCd" +
                            ", a.sDescript" +
                            ", a.sIndstCdx" +
                            ", a.sInvTypCd" +
                            ", a.cRecdStat" +
                            ", b.sDescript xIndstDsc" +
                            ", c.sDescript xInvTypNm" +
                        " FROM Category a" +
                                " LEFT JOIN Industry b ON a.sIndstCdx = b.sIndstCdx" +
                                " LEFT JOIN Inv_Type c ON a.sInvTypCd = c.sInvTypCd";
        
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode, String industryCode) throws SQLException, GuanzonException{

        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), "a.sIndstCdx = " + SQLUtil.toSQL(industryCode));
        
        poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    value,
                    "ID»Description»Industry»Inv. Type",
                    "a.sCategrCd»a.sDescript»xIndstDsc»xInvTypNm",
                    "a.sCategrCd»a.sDescript»b.sDescript»c.sDescript",
                    byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sCategrCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode, String industryCode, String inventoryTypeCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        if (industryCode != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryCode));
        }
        
        if (inventoryTypeCode != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeCode));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    value,
                    "ID»Description»Industry»Inv. Type",
                    "a.sCategrCd»a.sDescript»xIndstDsc»xInvTypNm",
                    "a.sCategrCd»a.sDescript»b.sDescript»c.sDescript",
                    byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sCategrCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}