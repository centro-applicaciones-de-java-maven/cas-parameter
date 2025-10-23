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
import ph.com.guanzongroup.cas.model.Model_Barangay;

public class Barangay extends Parameter{
    Model_Barangay poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = (Model_Barangay) ObjectInitiator.createModel(poGRider, Tables.BARANGAY);
        
        super.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() != UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getBarangayName().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Barangay must not be empty.");
                return poJSON;
            }
            
            if (poModel.getTownId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Town must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Barangay getModel() {
        return poModel;
    }
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID翡arangay",
                "sBrgyIDxx製BrgyName",
                "sBrgyIDxx製BrgyName",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrgyIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecord(String value, boolean byCode, String townId) throws SQLException, GuanzonException{
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), "a.sTownIDxx = " + SQLUtil.toSQL(townId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID翡arangay蓉own與rovince",
                "sBrgyIDxx製BrgyName製TownName製ProvName",
                "a.sBrgyIDxx蒼.sBrgyName膏FNULL(b.sTownName, '')膏FNULL(c.sProvName, '')",
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