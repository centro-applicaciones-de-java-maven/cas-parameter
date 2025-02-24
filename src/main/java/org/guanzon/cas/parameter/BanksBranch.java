package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Banks;
import org.guanzon.cas.parameter.model.Model_Banks_Branch;
import org.guanzon.cas.parameter.model.Model_Brand;
import org.json.simple.JSONObject;

public class BanksBranch extends Parameter{
    Model_Banks_Branch poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_Banks_Branch();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Banks_Branches");
        poModel.setTableName("Banks_Branches");
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
            
            if (poModel.getBranchBankName()== null ||poModel.getBranchBankName().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Branch Bank Name must not be empty.");
                return poJSON;
            }
            
            if (poModel.getBranchBankCode()== null || poModel.getBranchBankCode().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Branch Bank Code must not be empty.");
                return poJSON;
            }
            
            if (poModel.getBankID()== null || poModel.getBankID().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Bank must not be empty.");
                return poJSON;
            }
            
            if (poModel.getContactPerson()== null || poModel.getContactPerson().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Contact person must not be empty.");
                return poJSON;
            }
            
            if (poModel.getAddress()== null || poModel.getAddress().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Address must not be empty.");
                return poJSON;
            }
            
            if (poModel.getTownID()== null || poModel.getTownID().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Town must not be empty.");
                return poJSON;
            }
            
            if (poModel.getTelephoneNo()== null || poModel.getTelephoneNo().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Telephone must not be empty.");
                return poJSON;
            }
            
            if (poModel.getFaxNo()== null || poModel.getFaxNo().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Fax No must not be empty.");
                return poJSON;
            }
            
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Banks_Branch getModel() {
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
                "ID»Description»Branch Code",
                "sBrBankID»sBrBankNm»sBrBankCD",
                "sBrBankID»sBrBankNm»sBrBankCD",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrBankID"));
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
                "ID»Description»Category",
                "sBrandIDx»sDescript»sCategrCd",
                "sBrandIDx»sDescript»sCategrCd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrandIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
}