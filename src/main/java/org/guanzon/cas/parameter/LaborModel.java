package org.guanzon.cas.parameter;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Color;
import org.guanzon.cas.parameter.model.Model_Labor;
import org.guanzon.cas.parameter.model.Model_Labor_Model;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LaborModel extends Parameter {

    Model_Labor_Model poModelLabor;
    Labor poLabor;
    Model poModel;
    List<Model_Labor_Model> poModelList;
    String modelID = "";
    private CachedRowSet cacheLaborList;  // Stores the cached labor list

    @Override
    public void initialize() {
        psRecdStat = Logical.YES;

        poModelLabor = new Model_Labor_Model();
        poModelLabor.setApplicationDriver(poGRider);
        poModelLabor.setXML("Model_Labor_Model");
        poModelLabor.setTableName("Labor_Model");
        poModelLabor.initialize();
    }

    @Override
    public JSONObject isEntryOkay() {
        poJSON = new JSONObject();

        if (poGRider.getUserLevel() < UserRight.SYSADMIN) {
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();

            if (poModelLabor.getLaborId().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Labor must not be empty.");
                return poJSON;
            }

            if (poModelLabor.getModelId() == null || poModelLabor.getModelId().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Model must not be empty.");
                return poJSON;
            }
        }

        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public Model_Labor_Model getModel() {
        return poModelLabor;
    }

    @Override
    public JSONObject searchRecord(String value, boolean byCode) {
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);
        lsSQL = (lsSQL + " GROUP BY a.sModelIDx");

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description",
                "sModelIDx»sDescript",
                "a.sModelIDx»b.sDescript",
                byCode ? 0 : 1);
        poModelLabor.setModelId(poJSON.get("sModelIDx").toString());
        return openRecord(poJSON);
//        if (poJSON != null) {
//            return poModelLabor.openRecord((String) poJSON.get("sLaborIDx"),(String) poJSON.get("sModelIDx"));
//        } else {
//            poJSON = new JSONObject();
//            poJSON.put("result", "error");
//            poJSON.put("message", "No record loaded.");
//            return poJSON;
//        }
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
                "sLaborIDx»sLaborNme",
                "sLaborIDx»sLaborNme",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModelLabor.openRecord((String) poJSON.get("sLaborIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject voidTransaction() {
        poJSON = new JSONObject();

        if (poModelLabor.getLaborId() == null || poModelLabor.getLaborId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModelLabor.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModelLabor.setRecordStatus("0");
        poModelLabor.setModifyingId(poGRider.getUserID());
        poModelLabor.setModifiedDate(poGRider.getServerDate());
        poJSON = poModelLabor.saveRecord();

        if ("success".equals(poJSON.get("result"))) {
            poGRider.commitTrans();
            poJSON.put("message", "The category has been activated successfully.");
        } else {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to save record. Transaction rolled back.");
        }

        return poJSON;
    }

    public JSONObject postTransaction() {
        poJSON = new JSONObject();

        if (poModelLabor.getLaborId() == null || poModelLabor.getLaborId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModelLabor.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModelLabor.setRecordStatus("1");
        poModelLabor.setModifyingId(poGRider.getUserID());
        poModelLabor.setModifiedDate(poGRider.getServerDate());
        poJSON = poModelLabor.saveRecord();

        if ("success".equals(poJSON.get("result"))) {
            poGRider.commitTrans();
            poJSON.put("message", "The category has been activated successfully.");
        } else {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to save record. Transaction rolled back.");
        }

        return poJSON;
    }

    @Override
    public String getSQ_Browse() {
        String lsSQL;
        String lsRecdStat = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsRecdStat += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsRecdStat = "a.cRecdStat IN (" + lsRecdStat.substring(2) + ")";
        } else {
            lsRecdStat = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        lsSQL = "SELECT "
                + "  a.sLaborIDx "
                + ", b.sModelIDx "
                + ", b.sDescript "
                + ", a.nAmountxx "
                + ", b.sModelCde "
                + ", b.sBrandIDx "
                + ", b.sSeriesID "
                + ", b.nYearModl "
                + ", b.cEndOfLfe "
                + ", a.cRecdStat "
                + ", b.cRecdStat "
                + ", a.sModified "
                + ", a.dModified "
                + " FROM labor_model a "
                + " LEFT JOIN Model b ON a.sModelIDx = b.sModelIDx ";

//        if (!psRecdStat.isEmpty()) {
//            lsSQL = MiscUtil.addCondition(lsSQL, lsRecdStat);
//        }
        return lsSQL;
    }

    public JSONObject searchRecordwithBarrcode(String value, boolean byCode) {

        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "BarCode»Description»Selling Price»ID",
                "sBarCodex»sDescript»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        return openRecord(poJSON);
    }

    private JSONObject openRecord(JSONObject json) {
        if (json != null) {
            poJSON = poModelLabor.openRecord((String) poJSON.get("sLaborIDx"),(String) poJSON.get("sModelIDx"));

            if (!"success".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            //load reference records
            poModel.openRecord("sModelIDx");
            //end -load reference records
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        return poJSON;
    }

    public JSONObject LaborList(String fsValue) {
        StringBuilder lsSQL = new StringBuilder(
                "SELECT a.sLaborIDx, a.sLaborNme, "
                + "COALESCE(b.sModelIDx, '') AS sModelIDx, "
                + "COALESCE(b.nAmountxx, 0) AS nAmountxx, "
                + "b.cRecdStat "
                + "FROM Labor a "
                + "LEFT JOIN labor_model b ON a.sLaborIDx = b.sLaborIDx ");

        lsSQL.append(MiscUtil.addCondition("", "b.sModelIDx = " + SQLUtil.toSQL(fsValue) + " OR b.sModelIDx IS NULL "));
        lsSQL.append(" ORDER BY a.sLaborIDx");

        System.out.println("Executing SQL: " + lsSQL.toString());

        ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
        JSONObject poJSON = new JSONObject();
        modelID = fsValue;
        try {
            cacheLaborList = new CachedRowSetImpl();  // ✅ Initialize CachedRowSet
            cacheLaborList.populate(loRS); // ✅ Store result in cache

            poJSON.put("result", "success");
            poJSON.put("message", "Record loaded successfully.");

        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }

    // ✅ Getter method to access cache from UI Controller
    public CachedRowSet getCachedLaborList() {
        return cacheLaborList;
    }

    private Model_Labor_Model ModelLabor(String laborID, String modelID) {
        Model_Labor_Model object = new ParamModels(poGRider).LaborModel();

        JSONObject loJSON = object.openRecord(laborID, modelID);
        System.out.println(" ");
        System.out.println("------------------------------------------------------");
        System.out.println("ModelLabor = " + loJSON.toString());
        System.out.println("laborID = " + laborID);
        System.out.println("modelID = " + modelID);
        System.out.println(" ");
        if ("success".equals((String) loJSON.get("result"))) {
            return object;
        } else {
            return new ParamModels(poGRider).LaborModel();
        }
    }

    public int getListCount() {
        return poModelList.size();
    }

    public Model_Labor_Model LaborModel(int row) {
        return poModelList.get(row);
    }

    /**
     *
     */
    public JSONObject saveRecord() {
    JSONObject poJSON = new JSONObject();

    if (cacheLaborList == null) {
        poJSON.put("result", "error");
        poJSON.put("message", "No records to save.");
        return poJSON;
    }

    try {
        poGRider.beginTrans(); // ✅ Start transaction
        cacheLaborList.beforeFirst(); // ✅ Reset cursor

        while (cacheLaborList.next()) {
            double amount = cacheLaborList.getDouble("nAmountxx");
            String laborId = cacheLaborList.getString("sLaborIDx");
            String modelId = modelID;
            String recordStat = cacheLaborList.getString("cRecdStat");

            // ✅ Check if record exists
            String checkSQL = String.format(
                "SELECT COUNT(*) AS record_count FROM labor_model WHERE sLaborIDx = '%s' AND sModelIDx = '%s'",
                laborId, modelId
            );

            ResultSet rs = poGRider.executeQuery(checkSQL);
            boolean exists = rs.next() && rs.getInt("record_count") > 0;
            rs.close();

            if (exists) {
                if (amount == 0.00) {
                    // ✅ Delete record if amount is updated to zero
                    String deleteSQL = String.format(
                        "DELETE FROM labor_model WHERE sLaborIDx = '%s' AND sModelIDx = '%s';",
                        laborId, modelId
                    );
                    poGRider.executeUpdate(deleteSQL);
                    System.out.println("🗑️ Deleting Labor ID: " + laborId);
                } else {
                    // ✅ Update existing record
                    String updateSQL = String.format(
                            "UPDATE labor_model SET nAmountxx = %f, cRecdStat = '%s' WHERE sLaborIDx = '%s' AND sModelIDx = '%s';",
                            amount, recordStat, laborId, modelId // ✅ Added cRecdStat
                    );
                    poGRider.executeUpdate(updateSQL);
                    System.out.println("✅ Updating Labor ID: " + laborId + " | Amount: " + amount + " | Status: " + recordStat);

                }
            } else {
                if (amount > 0.00) { // ✅ Insert only if amount > 0
                    poJSON = poModelLabor.newRecord();
                    if (!"success".equals(poJSON.get("result"))) {
                        poGRider.rollbackTrans();
                        poJSON.put("message", "Failed to update record.");
                        return poJSON;
                    }
                    poModelLabor.setLaborId(laborId);
                    poModelLabor.setModelId(modelId);
                    poModelLabor.setAmount(amount);
                    poModelLabor.setModifyingId(poGRider.getUserID());
                    poModelLabor.setModifiedDate(poGRider.getServerDate());
                    poJSON = poModelLabor.saveRecord();
                }
            }
        }

        poGRider.commitTrans(); // ✅ Commit transaction
        poJSON.put("result", "success");
        poJSON.put("message", "Record(s) saved successfully.");
    } catch (SQLException e) {
        poGRider.rollbackTrans(); // ✅ Ensure rollback on failure
        poJSON.put("result", "error");
        poJSON.put("message", "Error saving records: " + e.getMessage());
    }

    return poJSON;
}


}
