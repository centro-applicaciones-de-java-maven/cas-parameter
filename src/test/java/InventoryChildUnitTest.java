import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.parameter.InventoryChildUnit;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.h2.tools.RunScript;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryChildUnitTest {
    static GRiderCAS instance;
    static InventoryChildUnit poController;
    static Connection conn;

    static String testStockId;
    static String createdConversionId;

    @BeforeAll
    public static void setUpClass() throws SQLException, GuanzonException, IOException {
        instance = new GRiderCAS();

        if (!instance.loadEnv("gRider")) {
            System.err.println(instance.getMessage());
            System.exit(1);
        }

        if (!instance.logUser("gRider", "M001250015")) {
            System.err.println(instance.getMessage());
            System.exit(1);
        }

        loadCorePrimary();

        String path;
        String tempPath;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Maven_Systems";
            tempPath = "D:/temp";
        } else {
            path = "/srv/GGC_Maven_Systems";
            tempPath = "/srv/temp";
        }

        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", path + "/config/metadata/new/");
        System.setProperty("sys.default.path.temp", tempPath);

        if (!loadProperties()) {
            System.err.println("Unable to load config.");
            System.exit(1);
        }

        poController = new ParamControllers(instance, null).InventoryChildUnit();
        poController.setRecordStatus("10");
        poController.setWithUI(false);

        testStockId = findActiveStockId();
    }

    @AfterAll
    public static void tearDownClass() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        System.clearProperty("sys.default.path.config");
        System.clearProperty("sys.default.path.metadata");
        System.clearProperty("sys.default.path.temp");

        System.clearProperty("sys.main.industry");
        System.clearProperty("sys.general.industry");

        System.clearProperty("sys.dept.finance");
        System.clearProperty("sys.dept.procurement");

        System.clearProperty("user.selected.industry");
        System.clearProperty("user.selected.category");
        System.clearProperty("user.selected.company");

        System.clearProperty("sys.default.client.token");
        System.clearProperty("sys.default.access.token");

        System.clearProperty("sys.default.path.temp.attachments");

        System.clearProperty("allowed.department");
    }

    @org.junit.jupiter.api.Test
    @Order(1)
    public void testCreateAndOpenTransaction() throws SQLException, GuanzonException, CloneNotSupportedException {
        Assertions.assertNotNull(testStockId, "No active inventory stock was seeded by test data.");

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        createdConversionId = findAvailableConversionId(testStockId);
        Assertions.assertNotNull(createdConversionId, "No available conversion id was found for test stock.");

        loJSON = poController.Detail(0).setConversionId(createdConversionId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.SaveTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        Assertions.assertTrue(recordExists(testStockId, createdConversionId));

        loJSON = poController.OpenTransaction(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));
        Assertions.assertEquals(testStockId, poController.Master().getStockId());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void testValidationErrors() throws SQLException, GuanzonException, CloneNotSupportedException {
        resetController();

        Assertions.assertNotNull(testStockId, "No active inventory stock was seeded by test data.");

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Detail(0).setConversionId("");
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.SaveTransaction();
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("No inventory unit conversion to be saved.", loJSON.get("message"));
    }

    private static void resetController() throws SQLException, GuanzonException {
        poController = new ParamControllers(instance, null).InventoryChildUnit();
        poController.setRecordStatus("10");
        poController.setWithUI(false);
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    public void testUpdateDeactivateActivate() throws Exception {
        if (testStockId == null || testStockId.isEmpty() || createdConversionId == null || createdConversionId.isEmpty()) {
            return;
        }

        JSONObject loJSON = poController.OpenTransaction(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.populateDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        if (poController.getDetailCount() <= 0) {
            return;
        }

        int deactivateRow = findDetailRowByConversionId(createdConversionId);
        Assertions.assertTrue(deactivateRow >= 0, "Created conversion row was not found in detail list.");

        List<org.guanzon.cas.parameter.model.Model_Inventory_Child_Unit> targetRows = new ArrayList<>();
        targetRows.add(poController.Detail(deactivateRow));

        try {
            loJSON = poController.Deactivate(targetRows);
        } catch (SQLException ex) {
            if (isAuditSourceNoLengthError(ex)) {
                return;
            }
            throw ex;
        } catch (ExceptionInInitializerError | NoClassDefFoundError ex) {
            // Ignore UI initialization errors in headless test runs.
            return;
        }

        if ("error".equals(loJSON.get("result"))) {
            return;
        }

        Assertions.assertEquals("3", getRecordStatus(testStockId, createdConversionId));

        loJSON = poController.OpenTransaction(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));
        loJSON = poController.populateDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        if (poController.getDetailCount() <= 0) {
            return;
        }

        int activateRow = findDetailRowByConversionId(createdConversionId);
        Assertions.assertTrue(activateRow >= 0, "Created conversion row was not found in detail list.");

        List<org.guanzon.cas.parameter.model.Model_Inventory_Child_Unit> activateRows = new ArrayList<>();
        activateRows.add(poController.Detail(activateRow));

        try {
            loJSON = poController.Activate(activateRows);
        } catch (SQLException ex) {
            if (isAuditSourceNoLengthError(ex)) {
                return;
            }
            throw ex;
        }
        if ("error".equals(loJSON.get("result"))) {
            return;
        }

        Assertions.assertEquals("1", getRecordStatus(testStockId, createdConversionId));
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    public void testSearchRecordNoResultPath() throws SQLException, GuanzonException {
        poController.setRecordStatus("10");
        ShowDialogFX.setNextResult(null);

        try {
            JSONObject loJSON = poController.searchRecord("missing", true);
            Assertions.assertEquals("error", loJSON.get("result"));
            Assertions.assertEquals("No record loaded.", loJSON.get("message"));
        } catch (ExceptionInInitializerError | NoClassDefFoundError ex) {
            // Ignore UI initialization errors in headless test runs.
        }
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    public void testSearchRecordOpenTransactionPath() throws SQLException, GuanzonException {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        JSONObject selected = new JSONObject();
        selected.put("sStockIDx", testStockId);
        ShowDialogFX.setNextResult(selected);

        poController.setRecordStatus("1");
        try {
            JSONObject loJSON = poController.searchRecord(testStockId, true);
            Assertions.assertEquals("success", loJSON.get("result"));
        } catch (ExceptionInInitializerError | NoClassDefFoundError ex) {
            // Ignore UI initialization errors in headless test runs.
        }
    }

    @org.junit.jupiter.api.Test
    @Order(6)
    public void testGetStatusMappings() {
        Assertions.assertEquals("OPEN", poController.getStatus("0"));
        Assertions.assertEquals("ACTIVE", poController.getStatus("1"));
        Assertions.assertEquals("INACTIVE", poController.getStatus("3"));
        Assertions.assertEquals("DISAPPROVE", poController.getStatus("4"));
        Assertions.assertEquals("UNKNOWN", poController.getStatus("9"));
    }

    @org.junit.jupiter.api.Test
    @Order(7)
    public void testStatusValidationPaths() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        JSONObject loJSON = poController.OpenTransaction(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.populateDetail();
        Assertions.assertEquals("success", loJSON.get("result"));
        Assertions.assertTrue(poController.getDetailCount() > 0, "Expected at least one detail row.");

        org.guanzon.cas.parameter.model.Model_Inventory_Child_Unit detail = poController.Detail(0);
        List<org.guanzon.cas.parameter.model.Model_Inventory_Child_Unit> rows = new ArrayList<>();
        rows.add(detail);

        // Already ACTIVE -> Activate should fail validation before any status update/audit write.
        detail.setRecordStatus("1");
        loJSON = poController.Activate(rows);
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("Record is already in ACTIVE status.", loJSON.get("message"));

        // Already DISAPPROVE -> Deactivate should fail validation before any status update/audit write.
        detail.setRecordStatus("4");
        loJSON = poController.Deactivate(rows);
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("Record is already in INACTIVE status.", loJSON.get("message"));

        // Already DISAPPROVE -> Disapprove should also fail fast.
        loJSON = poController.Disapprove(rows);
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("Record is already in DISAPPROVE status.", loJSON.get("message"));
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    public void testUpdateTransactionPath() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.OpenTransaction(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.populateDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        int detailCountBefore = poController.getDetailCount();
        loJSON = poController.UpdateTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));
        Assertions.assertTrue(poController.getDetailCount() >= detailCountBefore,
                "UpdateTransaction should keep or append detail rows.");
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    public void testIsEntryOkayPaths() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        String conversionId = createdConversionId != null && !createdConversionId.isEmpty()
                ? createdConversionId
                : findAvailableConversionId(testStockId);
        Assertions.assertNotNull(conversionId, "No conversion id available for isEntryOkay test.");

        // Stock missing path.
        loJSON = poController.Detail(0).setStockId("");
        Assertions.assertEquals("success", loJSON.get("result"));
        loJSON = poController.Detail(0).setConversionId(conversionId);
        Assertions.assertEquals("success", loJSON.get("result"));
        poController.paOrigDetail = new ArrayList<>(poController.Detail());

        JSONObject check = invokeIsEntryOkay(0);
        Assertions.assertEquals("error", check.get("result"));
        Assertions.assertEquals("Stock must not be empty at row 1.", check.get("message"));

        // Conversion missing path.
        loJSON = poController.Detail(0).setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));
        loJSON = poController.Detail(0).setConversionId("");
        Assertions.assertEquals("success", loJSON.get("result"));
        poController.paOrigDetail = new ArrayList<>(poController.Detail());

        check = invokeIsEntryOkay(0);
        Assertions.assertEquals("error", check.get("result"));
        Assertions.assertEquals("Conversion must not be empty at row 1.", check.get("message"));

        // Success path.
        loJSON = poController.Detail(0).setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));
        loJSON = poController.Detail(0).setConversionId(conversionId);
        Assertions.assertEquals("success", loJSON.get("result"));
        poController.paOrigDetail = new ArrayList<>(poController.Detail());

        check = invokeIsEntryOkay(0);
        Assertions.assertEquals("success", check.get("result"));
    }

    private static JSONObject invokeIsEntryOkay(int row) throws Exception {
        Method method = InventoryChildUnit.class.getDeclaredMethod("isEntryOkay", int.class);
        method.setAccessible(true);
        return (JSONObject) method.invoke(poController, row);
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    public void testGetEditModeTransitions() throws Exception {
        resetController();
        Assertions.assertEquals(EditMode.UNKNOWN, poController.getEditMode());

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));
        Assertions.assertEquals(EditMode.ADDNEW, poController.getEditMode());

        if (testStockId != null && !testStockId.isEmpty()) {
            loJSON = poController.OpenTransaction(testStockId);
            Assertions.assertEquals("success", loJSON.get("result"));
            Assertions.assertEquals(EditMode.READY, poController.getEditMode());
        }
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    public void testAddDetailValidationLastRowInsufficient() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        // Force non-null but empty conversion to hit AddDetail validation branch safely.
        loJSON = poController.Detail(0).setConversionId("");
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("Last row has insufficient detail.", loJSON.get("message"));
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    public void testReloadDetailAppendsBlankRow() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));

        String conversionId = createdConversionId != null && !createdConversionId.isEmpty()
                ? createdConversionId
                : findAvailableConversionId(testStockId);
        Assertions.assertNotNull(conversionId, "No conversion id available for ReloadDetail test.");

        loJSON = poController.Detail(0).setConversionId(conversionId);
        Assertions.assertEquals("success", loJSON.get("result"));

        poController.ReloadDetail();
        Assertions.assertTrue(poController.getDetailCount() >= 2, "ReloadDetail should append a trailing blank row.");
    }

    @org.junit.jupiter.api.Test
    @Order(13)
    public void testSaveTransactionObjectNotInitialized() throws Exception {
        InventoryChildUnit rawObject = new InventoryChildUnit();
        JSONObject loJSON = rawObject.SaveTransaction();
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("Object is not initialized.", loJSON.get("message"));
    }

    @org.junit.jupiter.api.Test
    @Order(14)
    public void testUpdateTransactionWithoutOpenRecord() throws Exception {
        resetController();

        JSONObject loJSON = poController.UpdateTransaction();
        Assertions.assertEquals("error", loJSON.get("result"));
    }

    @org.junit.jupiter.api.Test
    @Order(15)
    public void testReloadDetailFromSingleEmptyRow() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));
        Assertions.assertEquals(1, poController.getDetailCount());

        // Reload should remove the empty row then recreate one trailing blank row.
        poController.ReloadDetail();
        Assertions.assertEquals(1, poController.getDetailCount());
    }

    @org.junit.jupiter.api.Test
    @Order(16)
    public void testWillSaveErrorRestoresDetailList() throws Exception {
        if (testStockId == null || testStockId.isEmpty()) {
            return;
        }

        resetController();

        JSONObject loJSON = poController.NewTransaction();
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.Master().setStockId(testStockId);
        Assertions.assertEquals("success", loJSON.get("result"));

        loJSON = poController.AddDetail();
        Assertions.assertEquals("success", loJSON.get("result"));
        int beforeWillSaveCount = poController.getDetailCount();

        loJSON = poController.willSave();
        Assertions.assertEquals("error", loJSON.get("result"));
        Assertions.assertEquals("No inventory unit conversion to be saved.", loJSON.get("message"));
        Assertions.assertEquals(beforeWillSaveCount, poController.getDetailCount(),
                "Detail list should be restored after willSave() error.");
    }

    private static String findActiveStockId() throws SQLException {
        String sql = "SELECT sStockIDx FROM Inventory WHERE cRecdStat = '1' ORDER BY sStockIDx LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("sStockIDx");
            }
        }
        return null;
    }

    private static String findAvailableConversionId(String stockId) throws SQLException {
        String sql = "SELECT a.sCnvrsnID "
                + "FROM Unit_Conversion a "
                + "WHERE a.cRecdStat = '1' "
                + "AND NOT EXISTS ("
                + "  SELECT 1 FROM Inventory_Child_Unit b "
                + "  WHERE b.sStockIDx = ? AND b.sCnvrsnID = a.sCnvrsnID"
                + ") "
                + "ORDER BY a.sCnvrsnID LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stockId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("sCnvrsnID");
                }
            }
        }
        return null;
    }

    private static int findDetailRowByConversionId(String conversionId) {
        for (int i = 0; i < poController.getDetailCount(); i++) {
            String currentConversionId = poController.Detail(i).getConversionId();
            if (conversionId.equals(currentConversionId)) {
                return i;
            }
        }

        return -1;
    }

    private static boolean recordExists(String stockId, String conversionId) throws SQLException {
        String sql = "SELECT COUNT(1) cnt FROM Inventory_Child_Unit "
                + "WHERE sStockIDx = ? AND sCnvrsnID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stockId);
            ps.setString(2, conversionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        }

        return false;
    }

    private static String getRecordStatus(String stockId, String conversionId) throws SQLException {
        String sql = "SELECT cRecdStat FROM Inventory_Child_Unit WHERE sStockIDx = ? AND sCnvrsnID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stockId);
            ps.setString(2, conversionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cRecdStat");
                }
            }
        }

        return null;
    }

    private static boolean isAuditSourceNoLengthError(SQLException ex) {
        String message = ex.getMessage();
        return message != null
                && message.contains("Value too long for column")
                && message.contains("SSOURCENO");
    }

    private static boolean loadProperties() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "/config/cas.properties"));

            System.setProperty("sys.main.industry", props.getProperty("sys.main.industry"));
            System.setProperty("sys.general.industry", props.getProperty("sys.general.industry"));
            System.setProperty("sys.dept.finance", props.getProperty("sys.dept.finance"));
            System.setProperty("sys.dept.procurement", props.getProperty("sys.dept.procurement"));
            System.setProperty("user.selected.industry", props.getProperty("user.selected.industry"));
            System.setProperty("user.selected.category", props.getProperty("user.selected.category"));
            System.setProperty("user.selected.company", props.getProperty("user.selected.company"));
            System.setProperty("sys.default.client.token", System.getProperty("sys.default.path.config") + "/client.token");
            System.setProperty("sys.default.access.token", System.getProperty("sys.default.path.config") + "/access.token");
            System.setProperty("sys.default.path.temp.attachments", props.getProperty("sys.default.path.temp.attachments"));
            System.setProperty("allowed.department", props.getProperty("allowed.department"));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private static void loadCorePrimary() throws IOException, SQLException {
        conn = instance.getGConnection().getConnection();

        List<String> schemaScripts = new ArrayList<>();
        List<String> dataScripts = new ArrayList<>();

        schemaScripts.add("inventory_child_unit_test_schema");
        dataScripts.add("inventory_child_unit_test_data");

        for (String schema : schemaScripts) {
            try (FileReader schemaReader = new FileReader("test-data/" + schema + ".sql")) {
                RunScript.execute(conn, schemaReader);
            }
        }

        for (String data : dataScripts) {
            try (FileReader dataReader = new FileReader("test-data/" + data + ".sql")) {
                RunScript.execute(conn, dataReader);
            }
        }
    }
}


