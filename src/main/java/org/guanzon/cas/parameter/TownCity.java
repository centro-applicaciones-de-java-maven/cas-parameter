package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_TownCity;
import org.json.simple.JSONObject;

public class TownCity extends Parameter{
    Model_TownCity poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_TownCity();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_TownCity");
        poModel.setTableName("TownCity");
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
            
            if (poModel.getTownName().isEmpty()){
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
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_TownCity getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "ID»Town»Province»Zip Code»Muni Code",
                "sTownIDxx»sTownName»sProvName»sZippCode»sMuncplCd",
                "a.sTownIDxx»a.sTownName»IFNULL(b.sProvName, '')»a.sZippCode»a.sMuncplCd",
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
    
    public JSONObject searchTownByProvince(String value, boolean byCode, String provinceId){
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), "a.sProvIDxx = " + SQLUtil.toSQL(provinceId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Town»Province»Zip Code»Muni Code",
                "sTownIDxx»sTownName»sProvName»sZippCode»sMuncplCd",
                "a.sTownIDxx»a.sTownName»IFNULL(b.sProvName, '')»a.sZippCode»a.sMuncplCd",
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
    
    public JSONObject searchTownWithStatus(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "ID»Town»Province»Zip Code»Muni Code»Has Route»Blacklisted»Record Status",
                "sTownIDxx»sTownName»sProvName»sZippCode»sMuncplCd»cHasRoute»cBlackLst»cRecdStat",
                "a.sTownIDxx»a.sTownName»IFNULL(b.sProvName, '')»a.sZippCode»a.sMuncplCd»a.cHasRoute»a.cBlackLst»a.cRecdStat",
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
        
        return MiscUtil.addCondition(
                    "SELECT" +
                        "  a.sTownIDxx" +
                        ", a.sTownName" +
                        ", a.sZippCode" +
                        ", a.sProvIDxx" +
                        ", a.sMuncplCd" +
                        ", a.cHasRoute" +
                        ", a.cBlackLst" +
                        ", a.cRecdStat" +
                        ", IFNULL(b.sProvName, '') sProvName" +
                    " FROM TownCity a" +
                        " LEFT JOIN Province b ON a.sProvIDxx = b.sProvIDxx", 
            lsCondition);
    }
}