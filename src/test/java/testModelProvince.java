
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameter.model.Model_Province;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testModelProvince {
    static GRider instance;
    static Model_Province record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        record = new Model_Province(instance);
    }

//    @Test
//    public void testNewRecord() {
//        JSONObject loJSON;
//
//        loJSON = record.newRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }           
//        
//        loJSON = record.setProvinceName("Bagong Province");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }     
//        
//        loJSON = record.setRegionId("15");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }     
//
//        loJSON = record.saveRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }  
//    }
    
    @Test
    public void testLoadRecord() {
        JSONObject loJSON;

        loJSON = record.openRecord("99");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }       
        
        System.out.println(record.getProvinceId());
        System.out.println(record.getProvinceName());
        System.out.println(record.getRegionId());
        System.out.println(record.getModifyingId());
        System.out.println(record.getModifiedDate());
    }
    
    @Test
    public void testUpdateRecord() {
        JSONObject loJSON;

        loJSON = record.openRecord("99");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }       
        
        loJSON = record.setProvinceName("Yeh Yeh");
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
