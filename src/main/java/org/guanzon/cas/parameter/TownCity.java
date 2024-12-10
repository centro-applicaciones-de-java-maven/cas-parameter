package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.parameter.model.Model_TownCity;
import org.json.simple.JSONObject;

public class TownCity implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psRecdStat;

    Model_TownCity poModel;
    JSONObject poJSON;

    public TownCity(GRider appDriver, boolean withParent) {
        poGRider = appDriver;
        pbWthParent = withParent;

        psRecdStat = Logical.YES;
        poModel = new Model_TownCity(appDriver);
    }

    @Override
    public void setRecordStatus(String recordStatus) {
        psRecdStat = recordStatus;
    }

    @Override
    public int getEditMode() {
        return poModel.getEditMode();
    }

    @Override
    public JSONObject newRecord() {        
        return poModel.newRecord();
    }

    @Override
    public JSONObject openRecord(String townId) {
        return poModel.openRecord(townId);
    }

    @Override
    public JSONObject updateRecord() {
        return poModel.updateRecord();
    }

    @Override
    public JSONObject saveRecord() {
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        poJSON = poModel.saveRecord();

        if ("success".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.commitTrans();
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
        }

        return poJSON;
    }

    @Override
    public JSONObject deleteRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject deactivateRecord() {
        poJSON = new JSONObject();
        
        if (poModel.getEditMode() != EditMode.READY ||
            poModel.getEditMode() != EditMode.UPDATE){
        
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
        }
        
        
        if (poModel.getEditMode() == EditMode.READY) {
            poJSON = updateRecord();
            if ("error".equals((String) poJSON.get("result"))) return poJSON;
        } 

        poJSON = poModel.setRecordStatus("0");

        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        return poModel.saveRecord();
    }

    @Override
    public JSONObject activateRecord() {
        poJSON = new JSONObject();
        
        if (poModel.getEditMode() != EditMode.READY ||
            poModel.getEditMode() != EditMode.UPDATE){
        
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
        }
        
        
        if (poModel.getEditMode() == EditMode.READY) {
            poJSON = updateRecord();
            if ("error".equals((String) poJSON.get("result"))) return poJSON;
        } 

        poJSON = poModel.setRecordStatus("1");

        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        return poModel.saveRecord();
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
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    @Override
    public Model_TownCity getModel() {
        return poModel;
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
            poJSON.put("message", "No record loaded to update.");
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
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }
    
    private String getSQ_Browse(){
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
