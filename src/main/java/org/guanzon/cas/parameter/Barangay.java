package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.parameter.model.Model_Barangay;
import org.json.simple.JSONObject;

public class Barangay implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psRecdStat;

    Model_Barangay poModel;
    JSONObject poJSON;

    public Barangay(GRider appDriver, boolean withParent) {
        poGRider = appDriver;
        pbWthParent = withParent;

        psRecdStat = Logical.YES;
        poModel = new Model_Barangay(appDriver);
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
    public JSONObject openRecord(String barangayId) {
        return poModel.openRecord(barangayId);
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
                "ID»Barangay»Town»Province",
                "sBrgyIDxx»sBrgyName»sTownName»sProvName",
                "a.sBrgyIDxx»a.sBrgyName»IFNULL(b.sTownName, '')»IFNULL(c.sProvName, '')",
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
    public Model_Barangay getModel() {
        return poModel;
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
            poJSON.put("message", "No record loaded to update.");
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
            poJSON.put("message", "No record loaded to update.");
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

    @Override
    public JSONObject isEntryOkay() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
