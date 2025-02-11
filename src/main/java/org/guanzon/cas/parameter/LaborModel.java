package org.guanzon.cas.parameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import org.json.simple.JSONObject;

public class LaborModel extends Parameter{
    Model_Labor_Model poModelLabor;
    Labor poLabor;
    Model poModel;
    List<Model_Labor_Model> poModelList;
    
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
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModelLabor.getLaborId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Labor must not be empty.");
                return poJSON;
            }
            
            if (poModelLabor.getModelId()== null ||  poModelLabor.getModelId().isEmpty()){
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

        if (poModelLabor.getLaborId()== null || poModelLabor.getLaborId().isEmpty()) {
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

        if (poModelLabor.getLaborId()== null || poModelLabor.getLaborId().isEmpty()) {
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
    public String getSQ_Browse(){
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
        
        if (!psRecdStat.isEmpty()) lsSQL = MiscUtil.addCondition(lsSQL, lsRecdStat);

        System.out.println("select == " + lsSQL);
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
    
    private JSONObject openRecord(JSONObject json){
        if (json != null) {
            poJSON = poModelLabor.openRecord((String) poJSON.get("sLaborIDx"), (String) poJSON.get("sModelIDx"));
            
            if (!"success".equals((String) poJSON.get("result"))) return poJSON;
            
            //load reference records
            poLabor.openRecord("sLaborIDx");
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
          StringBuilder lsSQL = new StringBuilder( "SELECT "
                  + "a.sLaborIDx,"
                  + " a.sLaborNme,"
                  + " b.sModelIDx,"
                  + " b.nAmountxx " 
                  + "FROM Labor a " 
                  + "LEFT JOIN labor_model b ON a.sLaborIDx = b.sLaborIDx");

        // Use SQLUtil.toSQL for handling the dates
//        String condition = "sColorIDx = " + SQLUtil.toSQL(fsColorID);
//        lsSQL.append(MiscUtil.addCondition("", condition));
//        lsSQL.append(" ORDER BY a.nLedgerNo ASC");
        lsSQL.append(MiscUtil.addCondition("", "b.sModelIDx = " + SQLUtil.toSQL(fsValue) + " OR b.sModelIDx IS NULL "));
        lsSQL.append(" ORDER BY a.sLaborIDx");
        System.out.println("Executing SQL: " + lsSQL.toString());

        ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
        JSONObject poJSON = new JSONObject();

        try {
            int lnctr = 0;

            if (MiscUtil.RecordCount(loRS) >= 0) {
                poModelList = new ArrayList<>();
                while (loRS.next()) {
                    // Print the result set

                    System.out.println("sLaborIDx: " + loRS.getString("sLaborIDx"));
                    System.out.println("sLaborNme: " + loRS.getString("sLaborNme"));
                    System.out.println("sModelIDx: " + loRS.getString("sModelIDx"));
                    System.out.println("nAmountxx: " + loRS.getString("nAmountxx"));
                    System.out.println("------------------------------------------------------------------------------");

                    poModelList.add(ModelLabor(loRS.getString("sLaborIDx"),loRS.getString("sModelIDx")));
                    poModelList.get(poModelList.size() - 1)
                            .openRecord(loRS.getString("sLaborIDx"),loRS.getString("sModelIDx"));
                    lnctr++;
                }

                System.out.println("Records found: " + lnctr);
                poJSON.put("result", "success");
                poJSON.put("message", "Record loaded successfully.");

            } else {
                poModelList = new ArrayList<>();
//                addInvLedger();
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record found .");
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        System.out.println("RESULT == " + poJSON);
        return poJSON;
    }
    
    private Model_Labor_Model ModelLabor (String laborID,String modelID) {
        Model_Labor_Model object = new ParamModels(poGRider).LaborModel();

        JSONObject loJSON = object.openRecord(laborID,modelID);

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
    
}