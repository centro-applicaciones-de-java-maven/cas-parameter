package ph.com.guanzongroup.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import ph.com.guanzongroup.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Labor_Model extends Model {
        Model_Model poModel;
        Model_Labor poLabor;
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            
            poEntity.updateObject("nAmountxx", 0.00);
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sLaborIDx";
            ID2 = "sModelIDx";
            
            ParamModels model = new ParamModels(poGRider);
            poModel = model.Model();
            poLabor = model.Labor();            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public Model_Model Model() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sModelIDx"))) {
            if (poModel.getEditMode() == EditMode.READY
                    && poModel.getModelId().equals((String) getValue("sModelIDx"))) {
                return poModel;
            } else {
                poJSON = poModel.openRecord((String) getValue("sModelIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poModel;
                } else {
                    poModel.initialize();
                    return poModel;
                }
            }
        } else {
            poModel.initialize();
            return poModel;
        }
    }
    
    public Model_Labor Labor() throws SQLException, GuanzonException{
        System.out.println("laborid == " + (String) getValue("sLaborIDx"));
        if (!"".equals((String) getValue("sLaborIDx"))) {
            if (poLabor.getEditMode() == EditMode.READY
                    && poLabor.getLaborId().equals((String) getValue("sLaborIDx"))) {
                return poLabor;
            } else {
                poJSON = poLabor.openRecord((String) getValue("sLaborIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poLabor;
                } else {
                    poLabor.initialize();
                    return poLabor;
                }
            }
        } else {
            poLabor.initialize();
            return poLabor;
        }
    }

    public JSONObject setLaborId(String laborId) {
        return setValue("sLaborIDx", laborId);
    }

    public String getLaborId() {
        return (String) getValue("sLaborIDx");
    }

    public JSONObject setModelId(String modelId) {
        return setValue("sModelIDx", modelId);
    }

    public String getModelId() {
        return (String) getValue("sModelIDx");
    }
    
    public JSONObject setAmount(Number amount){
        return setValue("nAmountxx", amount);
    }
    
    public Number getAmount(){
        return (Number) getValue("nAmountxx");
    } 
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }

    public JSONObject setModifyingId(String modifyingId) {
        return setValue("sModified", modifyingId);
    }

    public String getModifyingId() {
        return (String) getValue("sModified");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }
    
    
}
