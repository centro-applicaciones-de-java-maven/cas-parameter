package ph.com.guanzongroup.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.impl.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import ph.com.guanzongroup.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Category_Level2 extends Model {
    private Model_Category poCategory;
    private Model_Inv_Type poInvType;
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            //initialize other connections
            poCategory = new ParamModels(poGRider).Category();
            poInvType = new ParamModels(poGRider).InventoryType();
            //end - initialize other connections
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public Model_Category Category() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sMainCatx"))){
            if (poCategory.getEditMode() == EditMode.READY && 
                poCategory.getCategoryId().equals((String) getValue("sMainCatx")))
                return poCategory;
            else{
                poJSON = poCategory.openRecord((String) getValue("sMainCatx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poCategory;
                else {
                    poCategory.initialize();
                    return poCategory;
                }
            }
        } else {
            poCategory.initialize();
            return poCategory;
        }
    }
    public Model_Inv_Type InvType() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sInvTypCd"))){
            if (poInvType.getEditMode() == EditMode.READY && 
                poInvType.getInventoryTypeId().equals((String) getValue("sInvTypCd")))
                return poInvType;
            else{
                poJSON = poInvType.openRecord((String) getValue("sInvTypCd"));

                if ("success".equals((String) poJSON.get("result")))
                    return poInvType;
                else {
                    poInvType.initialize();
                    return poInvType;
                }
            }
        } else {
            poInvType.initialize();
            return poInvType;
        }
    }

    public JSONObject setCategoryId(String categoryCode) {
        return setValue("sCategrCd", categoryCode);
    }

    public String getCategoryId() {
        return (String) getValue("sCategrCd");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }
    
    public JSONObject setInventoryTypeCode(String inventoryTypeCode) {
        return setValue("sInvTypCd", inventoryTypeCode);
    }

    public String getInventoryTypeCode() {
        return (String) getValue("sInvTypCd");
    }
    
    public JSONObject setMainCategory(String mainCategory) {
        return setValue("sMainCatx", mainCategory);
    }

    public String getMainCategory() {
        return (String) getValue("sMainCatx");
    }
    
    public JSONObject setClassify(String classify) {
        return setValue("cClassify", classify);
    }

    public String getClassify() {
        return (String) getValue("cClassify");
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
