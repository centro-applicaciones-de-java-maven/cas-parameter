import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import ph.com.guanzongroup.cas.parameter.TaxCode;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ph.com.guanzongroup.cas.core.ObjectInitiator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testTaxCode {
    static GRiderCAS instance;
    static TaxCode record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        try {
            record = (TaxCode) ObjectInitiator.createParameter(instance, false, null);
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }

    //@Test
    public void testNewRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.newRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  

            loJSON = record.getModel().setTaxCode("WC777");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  

            loJSON = record.getModel().setRegularRate(10.00);
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }

            loJSON = record.getModel().setGovernmentRate(15.00);
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
   
    //@Test
    public void testUpdateRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.openRecord("WC777");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      

            loJSON = record.updateRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      
            
            loJSON = record.getModel().setRegularRate(1.00);
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }

            loJSON = record.getModel().setGovernmentRate(2.00);
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
            Assert.fail(e.getMessage());
        } 
    }
    
    //@Test
    public void testDeactivateRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.openRecord("WC777");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      

            loJSON = record.deactivateRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      
        } catch (SQLException | GuanzonException  e) {
            Assert.fail(e.getMessage());
        } 
    }
    
    @Test
    public void testActivateRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.openRecord("WC777");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      

            loJSON = record.activateRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }      
        } catch (SQLException | GuanzonException  e) {
            Assert.fail(e.getMessage());
        } 
    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
