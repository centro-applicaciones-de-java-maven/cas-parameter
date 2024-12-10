package org.guanzon.cas.parameter;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.cas.parameter.model.Model_Province;
import org.json.simple.JSONObject;

public class Province extends Parameter{
    public Model_Province poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_Province();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Province");
        poModel.setTableName("Province");
        poModel.initialize();
    }
    
    @Override
    public Model_Province getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
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
    
    public JSONObject searchProvinceWithStatus(String value, boolean byCode) {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
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
}