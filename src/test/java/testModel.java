import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import ph.com.guanzongroup.cas.parameter.Model;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ph.com.guanzongroup.cas.core.ObjectInitiator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testModel {
    static GRiderCAS instance;
    static Model record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        try {
            record = (Model) ObjectInitiator.createParameter(instance, false, null);
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

            loJSON = record.getModel().setModelCode("CCG125SII");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     
            
            loJSON = record.getModel().setDescription("TMX 125 Alpha");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
            
            loJSON = record.getModel().setManufactureYear(2025);
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     
            
            loJSON = record.getModel().setMainModelId("00001");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
            
            loJSON = record.getModel().setBrandId("00001");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
            
            loJSON = record.getModel().setIndustryCode("02");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
            
            loJSON = record.saveRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  
        } catch (SQLException | GuanzonException | CloneNotSupportedException e) {
            Assert.fail(e.getMessage());
        }
    }
   
//    @Test
//    public void testUpdateRecord() {
//        JSONObject loJSON;
//
//        loJSON = record.openRecord("24001");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
//        loJSON = record.updateRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
////        loJSON = record.getModel().setCountryName("Yeh Yeh");
////        if ("error".equals((String) loJSON.get("result"))) {
////            Assert.fail((String) loJSON.get("message"));
////        }    
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
    
//    @Test
//    public void testSearch(){
//        JSONObject loJSON = record.searchRecord("", false);        
//        if ("success".equals((String) loJSON.get("result"))){
//            System.out.println(record.getModel().getCountryId());
//            System.out.println(record.getModel().getCountryName());
//            System.out.println(record.getModel().getNationality());
//        } else System.out.println("No record was selected.");
//    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
