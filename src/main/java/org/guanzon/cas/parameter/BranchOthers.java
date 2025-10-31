package org.guanzon.cas.parameter;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.json.simple.JSONObject;
import org.guanzon.cas.parameter.model.Model_Branch_Others;
import org.guanzon.cas.parameter.services.ParamModels;

/**
 *
 * @author 12mnv
 */
public class BranchOthers extends Parameter {

    Model_Branch_Others poModel;
 

    public void initialize() throws SQLException, GuanzonException {
        this.poModel = (new ParamModels(this.poGRider)).BranchOthers();
        super.initialize();
    }

    public JSONObject isEntryOkay() throws SQLException {
        this.poJSON = new JSONObject();
        if (this.poGRider.getUserLevel() < 16) {
            this.poJSON.put("result", "error");
            this.poJSON.put("message", "User is not allowed to save record.");
            return this.poJSON;
        }
        this.poJSON = new JSONObject();
        if (this.poModel.getBranchCode().isEmpty()) {
            this.poJSON.put("result", "error");
            this.poJSON.put("message", "Branch Code must not be empty.");
            return this.poJSON;
        }
        if (this.poModel.getDivision().isEmpty()) {
            this.poJSON.put("result", "error");
            this.poJSON.put("message", "Division must not be empty.");
            return this.poJSON;
        }
        this.poModel.setModifyingId(this.poGRider.Encrypt(this.poGRider.getUserID()));
        this.poModel.setModifiedDate(this.poGRider.getServerDate());
        this.poJSON.put("result", "success");
        return this.poJSON;
    }

    public Model_Branch_Others getModel() {
        return this.poModel;
    }

    public JSONObject searchRecord(String value) throws SQLException, GuanzonException {
        String lsCondition = "";
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);
        this.poJSON = ShowDialogFX.Search(this.poGRider,
                lsSQL,
                value,
                "Code",
                "sBranchCd", 
                "sBranchCd", 0);
        if (this.poJSON != null) {
            return this.poModel.openRecord((String) this.poJSON.get("sBrgyIDxx"));
        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;
    }
}
