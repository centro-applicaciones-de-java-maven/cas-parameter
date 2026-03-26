package org.guanzon.cas.parameter;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Project;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Project extends Parameter{
    Model_Project poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = new ParamModels(poGRider).Project();
        
        super.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException {
        poJSON = new JSONObject();
        
//        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
//            poJSON.put("result", "error");
//            poJSON.put("message", "User is not allowed to save record.");
//            return poJSON;
//        } 
            
        if (poModel.getProjectDescription().isEmpty()){
            poJSON.put("result", "error");
            poJSON.put("message", "Project Description must not be empty.");
            return poJSON;
        }
            
        
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Project getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "Project ID»Project Description»Status",
                "sProjCode»sProjDesc»cRecdStat",
                "sProjCode»sProjDesc»cRecdStat",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sProjCode"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    
}