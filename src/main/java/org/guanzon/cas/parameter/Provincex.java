package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.parameter.model.Model_Provincex;
import org.json.simple.JSONObject;

public class Provincex implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psRecdStat;

    Model_Provincex poModel;
    JSONObject poJSON;

    public Provincex(GRider appDriver, boolean withParent) {
        poGRider = appDriver;
        pbWthParent = withParent;

        psRecdStat = Logical.YES;
        poModel = new Model_Provincex(appDriver);
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
    public JSONObject openRecord(String provinceId) {
        return poModel.openRecord(provinceId);
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
                "ID»Province",
                "sProvIDxx»sProvName",
                "sProvIDxx»sProvName",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sProvIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    @Override
    public Model_Provincex getModel() {
        return poModel;
    }
    
    public JSONObject searchProvinceWithStatus(String value, boolean byCode) {
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
                "ID»Province»Record Status",
                "sProvIDxx»sProvName»cRecdStat",
                "sProvIDxx»sProvName»cRecdStat",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sProvIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }
    
    @Override
    public String getSQ_Browse(){
        return MiscUtil.makeSelect(poModel);
    }

    @Override
    public JSONObject isEntryOkay() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}