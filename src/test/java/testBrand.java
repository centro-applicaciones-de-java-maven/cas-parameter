

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameter.Brand;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.CategoryLevel2;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testBrand {
    static GRider instance;
    static Brand record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        record = new Brand();
        record.setApplicationDriver(instance);
        record.setWithParentClass(false);
        record.initialize();
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
//        loJSON = record.getModel().setDescription("Franchise");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        } 
//        
//        loJSON = record.getModel().setCategoryCode("0002");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }  
//        
//        loJSON = record.getModel().setModifyingId(instance.getUserID());
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }     
//        
//        loJSON = record.getModel().setModifiedDate(instance.getServerDate());
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }     
//        
//        loJSON = record.saveRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }  
//    }
//   
//    @Test
//    public void testUpdateRecord() {
//        JSONObject loJSON;
//
//        loJSON = record.openRecord("24009");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
//        loJSON = record.updateRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
//        loJSON = record.getModel().setModifyingId(instance.getUserID());
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }     
//        
//        loJSON = record.getModel().setModifiedDate(instance.getServerDate());
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
    public void testSearch(){
        JSONObject loJSON = record.searchRecord("", false);        
        if ("success".equals((String) loJSON.get("result"))){
            System.out.println(record.getModel().getBrandId());
            System.out.println(record.getModel().getDescription());
        } else System.out.println("No record was selected.");
    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
