package org.guanzon.cas.parameter;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Labor_Category;
import org.guanzon.cas.parameter.model.Model_Labor_Model;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class LaborCategory extends Parameter {

    Model_Labor_Category poModelCategory;
    Labor poLabor;
    Model poModel;
    List<Model_Labor_Category> poCategoryList;
    String categoryID = "";
    private CachedRowSet cacheLaborList;  // Stores the cached labor list

    @Override
    public void initialize() {
        psRecdStat = Logical.YES;

        poModelCategory = new Model_Labor_Category();
        poModelCategory.setApplicationDriver(poGRider);
        poModelCategory.setXML("Model_Labor_Category");
        poModelCategory.setTableName("Labor_Category");
        poModelCategory.initialize();
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

            if (poModelCategory.getLaborId().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Labor must not be empty.");
                return poJSON;
            }

            if (poModelCategory.getCategoryID() == null || poModelCategory.getCategoryID().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Model must not be empty.");
                return poJSON;
            }
        }

        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public Model_Labor_Category getModel() {
        return poModelCategory;
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
        lsSQL = (lsSQL + " GROUP BY a.sCategrCd");

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description",
                "sCategrCd»sDescript",
                "a.sCategrCd»b.sDescript",
                byCode ? 0 : 1);
        poModelCategory.setCategoryID(poJSON.get("sCategrCd").toString());
        System.out.println("ito dapat = " + poModelCategory.getCategoryID());
        return openRecord(poJSON);
//        if (poJSON != null) {
//            return poModelCategory.openRecord((String) poJSON.get("sLaborIDx"),(String) poJSON.get("sCategrCd"));
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
            return poModelCategory.openRecord((String) poJSON.get("sLaborIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject voidTransaction() {
        poJSON = new JSONObject();

        if (poModelCategory.getLaborId() == null || poModelCategory.getLaborId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModelCategory.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModelCategory.setRecordStatus("0");
        poModelCategory.setModifyingId(poGRider.getUserID());
        poModelCategory.setModifiedDate(poGRider.getServerDate());
        poJSON = poModelCategory.saveRecord();

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

        if (poModelCategory.getLaborId() == null || poModelCategory.getLaborId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }

        poGRider.beginTrans(); // Start transaction

        poJSON = poModelCategory.updateRecord();
        if (!"success".equals(poJSON.get("result"))) {
            poGRider.rollbackTrans();
            poJSON.put("message", "Failed to update record.");
            return poJSON;
        }

        poModelCategory.setRecordStatus("1");
        poModelCategory.setModifyingId(poGRider.getUserID());
        poModelCategory.setModifiedDate(poGRider.getServerDate());
        poJSON = poModelCategory.saveRecord();

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
                + ", b.sCategrCd "
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
                + " LEFT JOIN Model b ON a.sCategrCd = b.sCategrCd ";

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
            poJSON = poModelCategory.openRecord((String) poJSON.get("sLaborIDx"),(String) poJSON.get("sCategrCd"));

            if (!"success".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            //load reference records
            poModel.openRecord("sCategrCd");
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
                + "COALESCE(b.sCategrCd, '') AS sCategrCd, "
                + "COALESCE(b.nAmountxx, 0) AS nAmountxx, "
                + "b.cRecdStat "
                + "FROM Labor a "
                + "LEFT JOIN Labor_Category b ON a.sLaborIDx = b.sLaborIDx ");

        lsSQL.append(MiscUtil.addCondition("", "b.sCategrCd = " + SQLUtil.toSQL(fsValue) + " OR b.sCategrCd IS NULL OR b.sCategrCd = '' "));
        lsSQL.append(" ORDER BY a.sLaborIDx");

        System.out.println("Executing SQL: " + lsSQL.toString());

        ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
        JSONObject poJSON = new JSONObject();
        categoryID = fsValue;
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

    private Model_Labor_Category ModelCategory(String laborID, String categoryID) {
        Model_Labor_Category object = new ParamModels(poGRider).LaborCategory();

        JSONObject loJSON = object.openRecord(laborID, categoryID);
        System.out.println(" ");
        System.out.println("------------------------------------------------------");
        System.out.println("ModelLabor = " + loJSON.toString());
        System.out.println("laborID = " + laborID);
        System.out.println("modelID = " + categoryID);
        System.out.println(" ");
        if ("success".equals((String) loJSON.get("result"))) {
            return object;
        } else {
            return new ParamModels(poGRider).LaborCategory();
        }
    }

    public int getListCount() {
        return poCategoryList.size();
    }

    public Model_Labor_Category LaborCategory(int row) {
        return poCategoryList.get(row);
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
            String categId = categoryID;
            String recordStat = cacheLaborList.getString("cRecdStat");

            // ✅ Check if record exists
            String checkSQL = String.format(
                "SELECT COUNT(*) AS record_count FROM Labor_Category WHERE sLaborIDx = '%s' AND sCategrCd = '%s'",
                laborId, categId
            );

            ResultSet rs = poGRider.executeQuery(checkSQL);
            boolean exists = rs.next() && rs.getInt("record_count") > 0;
            rs.close();

            if (exists) {
                if (amount == 0.00) {
                    // ✅ Delete record if amount is updated to zero
                    String deleteSQL = String.format(
                        "DELETE FROM Labor_Category WHERE sLaborIDx = '%s' AND sCategrCd = '%s';",
                        laborId, categId
                    );
                    poGRider.executeUpdate(deleteSQL);
                    System.out.println("🗑️ Deleting Labor ID: " + laborId);
                } else {
                    // ✅ Update existing record
                    String updateSQL = String.format(
                            "UPDATE Labor_Category SET nAmountxx = %f, cRecdStat = '%s' WHERE sLaborIDx = '%s' AND sCategrCd = '%s';",
                            amount, recordStat, laborId, categId // ✅ Added cRecdStat
                    );
                    poGRider.executeUpdate(updateSQL);
                    System.out.println("✅ Updating Labor ID: " + laborId + " | Amount: " + amount + " | Status: " + recordStat);

                }
            } else {
                if (amount > 0.00) { // ✅ Insert only if amount > 0
                    poJSON = poModelCategory.newRecord();
                    if (!"success".equals(poJSON.get("result"))) {
                        poGRider.rollbackTrans();
                        poJSON.put("message", "Failed to update record.");
                        return poJSON;
                    }
                    poModelCategory.setLaborId(laborId);
                    poModelCategory.setCategoryID(categId);
                    poModelCategory.setAmount(amount);
                    poModelCategory.setModifyingId(poGRider.getUserID());
                    poModelCategory.setModifiedDate(poGRider.getServerDate());
                    poJSON = poModelCategory.saveRecord();
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
