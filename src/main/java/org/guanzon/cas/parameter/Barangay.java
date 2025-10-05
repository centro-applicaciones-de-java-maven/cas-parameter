package org.guanzon.cas.parameter;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Barangay;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Barangay extends Parameter{
    Model_Barangay poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = new ParamModels(poGRider).Barangay();
        
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
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Barangay",
                "sBrgyIDxx»sBrgyName",
                "sBrgyIDxx»sBrgyName",
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
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»xTownName»xProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sDescript, '')",
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
                            "  a.sBrgyIDxx" +
                            ", a.sBrgyName" +
                            ", a.sTownIDxx" +
                            ", a.cHasRoute" +
                            ", a.cBlackLst" +
                            ", IFNULL(b.sTownName, '') xTownName" + 
                            ", IFNULL(c.sDescript, '') xProvName" +
                        " FROM Barangay a" + 
                            " LEFT JOIN TownCity b ON a.sTownIDxx = b.sTownIDxx" +
                            " LEFT JOIN Province c ON b.sProvIDxx = c.sProvIDxx";
        
        lsSQL = MiscUtil.addCondition(lsSQL, lsCondition);
        
        System.out.println(lsSQL);
        return lsSQL;
    }
}