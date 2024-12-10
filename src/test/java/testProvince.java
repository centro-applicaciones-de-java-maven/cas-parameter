
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameter.Province;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testProvince {
    static GRider instance;
    static Province record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        record = new Province();
        record.setApplicationDriver(instance);
        record.setWithParentClass(false);
        record.initialize();
    }

    @Test
    public void testNewRecord() {
        JSONObject loJSON;

        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }           
        
        loJSON = record.getModel().setProvinceName("Bagong Province");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }     
        
        loJSON = record.getModel().setRegionId("15");
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
    }
   
    @Test
    public void testUpdateRecord() {
        JSONObject loJSON;

        loJSON = record.openRecord("0100");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }      
        
        loJSON = record.updateRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }      
        
        loJSON = record.getModel().setProvinceName("Yeh Yeh");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }    
        
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        } 
    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
