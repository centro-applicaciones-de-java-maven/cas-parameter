import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import ph.com.guanzongroup.cas.parameter.Industry;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ph.com.guanzongroup.cas.core.ObjectInitiator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testIndustry {
    static GRiderCAS instance;
    static Industry record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        try {
            record = (Industry) ObjectInitiator.createParameter(instance, false, null);
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testNewRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.newRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }           

            loJSON = record.getModel().setDescription("Main Office");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  

            loJSON = record.getModel().setModifyingId(instance.getUserID());
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     

            loJSON = record.getModel().setModifiedDate(instance.getServerDate());
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     

            loJSON = record.saveRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  
        } catch (SQLException | GuanzonException | CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
