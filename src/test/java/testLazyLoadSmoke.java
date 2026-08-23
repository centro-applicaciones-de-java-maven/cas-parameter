import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameter.Model;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class testLazyLoadSmoke {
    static GRiderCAS instance;
    static Model record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");
        instance = MiscUtil.Connect();
        try {
            record = new ParamControllers(instance, null).Model();
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testOpenRecordThenLazyBrandAndIndustry() {
        try {
            JSONObject loJSON = record.openRecord("M00126024");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }

            System.out.println("Model Id: " + record.getModel().getModelId());
            System.out.println("Brand FK: " + record.getModel().getBrandId());
            System.out.println("Brand Description: " + record.getModel().Brand().getDescription());
            System.out.println("Industry Description: " + record.getModel().Brand().Industry().getDescription());

            //Second Model instance looking up the SAME brand should be served from ReferenceCache.
            Model record2 = new ParamControllers(instance, null).Model();
            JSONObject loJSON2 = record2.openRecord("M00126024");
            if ("error".equals((String) loJSON2.get("result"))) {
                Assert.fail((String) loJSON2.get("message"));
            }
            System.out.println("Second lookup, Brand Description: " + record2.getModel().Brand().getDescription());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
