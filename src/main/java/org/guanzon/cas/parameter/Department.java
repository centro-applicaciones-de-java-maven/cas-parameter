package org.guanzon.cas.parameter;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Department;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Department extends Parameter{
    Model_Department poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        ParamModels model = new ParamModels(poGRider);
        poModel = model.Department();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getDescription() == null ||poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Description must not be empty.");
                return poJSON;
            }
            
            if (poModel.getDepartmentCode()== null || poModel.getDepartmentCode().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Department code must not be empty.");
                return poJSON;
            }
        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Department getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description»Code",
                "sDeptIDxx»sDeptName»sDeptCode",
                "sDeptIDxx»sDeptName»sDeptCode",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sDeptIDxx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}