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
import ph.com.guanzongroup.cas.iface.TownCityImpl;
import ph.com.guanzongroup.cas.model.Model_TownCity;

public class TownCity extends Parameter implements TownCityImpl{
    Model_TownCity poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = ObjectInitiator.createModel(Model_TownCity.class, poGRider, Tables.TOWN_CITY);
        
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
                poJSON.put("message", "Town must not be empty.");
                return poJSON;
            }
            
            if (poModel.getProvinceId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Province must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_TownCity getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Town»Province»Zipp Code»Mncplty Code",
                "sTownIDxx»sTownName»xProvName»sZippCode»sMuncplCd",
                "a.sTownIDxx»a.sTownName»IFNULL(b.sDescript, '')»a.sZippCode»a.sMuncplCd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sTownIDxx"));
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
                            "  a.sTownIDxx" +
                            ", a.sTownName" +
                            ", a.sZippCode" +
                            ", a.sProvIDxx" +
                            ", a.sMuncplCd" +
                            ", a.cHasRoute" +
                            ", a.cBlackLst" +
                            ", a.cRecdStat" +
                            ", a.sModified" + 
                            ", a.dModified" +
                            ", IFNULL(b.sDescript, '') xProvName" +
                        " FROM TownCity a" +
                            " LEFT JOIN Province b ON a.sProvIDxx = b.sProvIDxx";
        
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode, String provinceId) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        if (provinceId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sProvIDxx = " + SQLUtil.toSQL(provinceId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Town»Province»Zipp Code»Mncplty Code",
                "sTownIDxx»sTownName»xProvName»sZippCode»sMuncplCd",
                "a.sTownIDxx»a.sTownName»IFNULL(b.sDescript, '')»a.sZippCode»a.sMuncplCd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sTownIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}