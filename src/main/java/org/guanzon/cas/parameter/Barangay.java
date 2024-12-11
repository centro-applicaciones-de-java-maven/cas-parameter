package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Barangay;
import org.json.simple.JSONObject;

public class Barangay extends Parameter{
    Model_Barangay poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_Barangay();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Barangay");
        poModel.setTableName("Barangay");
        poModel.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() {
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
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Barangay getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')",
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
    
    public JSONObject searchBarangayByTown(String value, boolean byCode, String townId){
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), "a.sTownIDxx = " + SQLUtil.toSQL(townId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')",
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
    
    public JSONObject searchBarangayByProvince(String value, boolean byCode, String provinceId){
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), "b.sProvIDxx = " + SQLUtil.toSQL(provinceId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')",
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
    
    public JSONObject searchBarangayByTownAndProvince(String value, boolean byCode, String townId, String provinceId){
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                    "a.sTownIDxx = " + SQLUtil.toSQL(townId) +
                                                        " AND b.sProvIDxx = " + SQLUtil.toSQL(provinceId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sTownIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }
    
    public JSONObject searchBarangayWithStatus(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "ID»Barangay»Town»Province»Has Route»Blacklisted»Record Status",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName»cHasRoute»cBlackLst»cRecdStat",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')»a.cHasRoute»a.cBlackLst»a.cRecdStat",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrgyIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
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
        
        return MiscUtil.addCondition(
                    "SELECT" +
                        "  a.sBrgyIDxx" +
                        ", a.sBrgyName" +
                        ", a.sTownIDxx" +
                        ", a.cHasRoute" +
                        ", a.cBlackLst" +
                        ", a.cRecdStat" +
                        ", IFNULL(b.sTownName, '') sTownName" +
                        ", IFNULL(b.sZippCode, '') sZippCode" +
                        ", IFNULL(b.sMuncplCd, '') sMuncplCd" +
                        ", IFNULL(c.sProvName, '') sProvName" +
                        ", IFNULL(c.sProvIDxx, '') sProvIDxx" +
                    " FROM Barangay a" +
                        ", TownCity b" +
                        ", Province c" +
                    " WHERE a.sTownIDxx = b.sTownIDxx" +
                        " AND b.sProvIDxx = c.sProvIDxx",
            lsCondition);
    }
}