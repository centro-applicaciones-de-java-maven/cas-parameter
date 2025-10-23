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
import ph.com.guanzongroup.cas.model.Model_Term;

public class Term extends Parameter{
    Model_Term poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = (Model_Term) ObjectInitiator.createModel(poGRider, Tables.TERM);
        
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
            
            if (poModel.getTermId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Term must not be empty.");
                return poJSON;
            }
            
            if (poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Term must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Term getModel() {
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
                "ID»Description»Coverage",
                "sTermCode»sDescript»nTermValx",
                "sTermCode»sDescript»nTermValx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sTermCode"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}