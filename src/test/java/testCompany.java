import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameter.Company;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testCompany {
    static GRiderCAS instance;
    static Company record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        try {
            record = new ParamControllers(instance, null).Company();
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }

//    @Test
//    public void testNewRecord() {
//        try {
//            JSONObject loJSON;
//
//            loJSON = record.newRecord();
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }           
//
//            loJSON = record.getModel().setDescription("Mindanao");
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }     
//
//            loJSON = record.getModel().setNationality("Subsaharan");
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }     
//
//            loJSON = record.saveRecord();
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }  
//        } catch (SQLException | GuanzonException | CloneNotSupportedException e) {
//            Assert.fail(e.getMessage());
//        }
//    }
    
@Test
public void testLoadRecord(){
    try {
        JSONObject loJSON;

        loJSON = record.openRecord("M002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }      
        
        System.out.println("ID: " + record.getModel().getCompanyId());
        System.out.println("NAME: " + record.getModel().getCompanyName());
        System.out.println("ADDRESS: " + record.getModel().getCompanyAddress());
        System.out.println("TOWN ID: " + record.getModel().getCompanyTownId());
        System.out.println("TOWN, ID: " + record.getModel().TownCity().getTownId());
        System.out.println("TOWN, NAME: " + record.getModel().TownCity().getDescription());
        System.out.println("PROVINCE, ID: " + record.getModel().TownCity().Province().getProvinceId());
        System.out.println("PROVINCE, NAME: " + record.getModel().TownCity().Province().getDescription());
        System.out.println("REGION, ID: " + record.getModel().TownCity().Province().Region().getRegionId());
        System.out.println("REGION, NAME: " + record.getModel().TownCity().Province().Region().getDescription());
    } catch (SQLException | GuanzonException e) {
        Assert.fail(e.getMessage());
    }
}
   
//    @Test
//    public void testUpdateRecord() {
//        JSONObject loJSON;
//
//        loJSON = record.openRecord("77");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
//        loJSON = record.updateRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }      
//        
//        loJSON = record.getModel().setCountryName("Yeh Yeh");
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
