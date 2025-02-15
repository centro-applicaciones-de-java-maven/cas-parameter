package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Bin;
import org.guanzon.cas.parameter.model.Model_Warehouse;
import org.json.simple.JSONObject;

public class Warehouse extends Parameter{
    Model_Warehouse poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_Warehouse();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Warehouse");
        poModel.setTableName("Warehouse");
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
            
            if (poModel.getWarehouseId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Warehouse must not be empty.");
                return poJSON;
            }
            
            if (poModel.getWarehouseName().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Warehouse must not be empty.");
                return poJSON;
            }
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Warehouse getModel() {
        return poModel;
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
                "ID»Description",
                "sWHouseID»sWHouseNm",
                "sWHouseID»sWHouseNm",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sWHouseID"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordWithStatus(String value, boolean byCode) {
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
                "ID»Description",
                "sWHouseID»sWHouseNm",
                "sWHouseID»sWHouseNm",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sWHouseID"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject voidTransaction() {
        poJSON = new JSONObject();

        if (poModel.getWarehouseId() == null || poModel.getWarehouseId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModel.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModel.setRecordStatus("0");
        poModel.setModifyingId(poGRider.getUserID());
        poModel.setModifiedDate(poGRider.getServerDate());
        poJSON = poModel.saveRecord();

        if ("success".equals(poJSON.get("result"))) {
            poGRider.commitTrans();
            poJSON.put("message", "The warehouse has been activated successfully.");
        } else {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to save record. Transaction rolled back.");
        }

        return poJSON;
    }

    
    public JSONObject postTransaction() {
        poJSON = new JSONObject();

        if (poModel.getWarehouseId()== null || poModel.getWarehouseId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModel.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModel.setRecordStatus("1");
        poModel.setModifyingId(poGRider.getUserID());
        poModel.setModifiedDate(poGRider.getServerDate());
        poJSON = poModel.saveRecord();

        if ("success".equals(poJSON.get("result"))) {
            poGRider.commitTrans();
            poJSON.put("message", "The warehouse has been activated successfully.");
        } else {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to save record. Transaction rolled back.");
        }

        return poJSON;
    }
}