package org.guanzon.cas.parameter;

import com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.agent.services.ReferenceCache;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.parameter.model.Model_Term;
import org.guanzon.cas.parameter.model.Model_Unit_Conversion;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ph.com.guanzongroup.cas.cashflow.AccountChart;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UnitConversion extends Parameter{
    Model_Unit_Conversion poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = new ParamModels(poGRider).UnitConversion();
        
        super.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
//        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
//            poJSON.put("result", "error");
//            poJSON.put("message", "User is not allowed to save record.");
//            return poJSON;
//        } else
        {
            poJSON = new JSONObject();
            
            if (poModel.getConversionID() == null || poModel.getConversionID().isEmpty() ){
                poJSON.put("result", "error");
                poJSON.put("message", "Conversion ID must not be empty.");
                return poJSON;
            }
            
            if (poModel.getMeasureID().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Measure ID must not be empty.");
                return poJSON;
            }

            if (poModel.getConvertedID().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Converted ID must not be empty.");
                return poJSON;
            }
            if (poModel.getQuantityConverted()<= 0){
                poJSON.put("result", "error");
                poJSON.put("message", "Quantity converted must be greater than 0.");
                return poJSON;
            }

        }
        
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Unit_Conversion getModel() {
        return poModel;
    }

    @Override
    protected void saveComplete() {
        //Every lazy UnitConversion() accessor across the model layer serves repeat lookups for
        //this id from ReferenceCache - drop the stale snapshot now that the record has changed.
        ReferenceCache.invalidate("Unit_Conversion", poModel.getConversionID());
    }


    public JSONObject SearchMeasure(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException {
        Measure object = new ParamControllers(poGRider, logwrapr).Measurement();
        object.setRecordStatus("1");
        object.setWithParentClass(true);

        poJSON = object.searchRecord(value, byCode);

        if ("success".equals((String) poJSON.get("result"))) {
            poModel.setMeasureID((String)object.getModel().getMeasureId());
        }

        return poJSON;
    }

    public JSONObject SearchConversion(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException {
        Measure object = new ParamControllers(poGRider, logwrapr).Measurement();
        object.setRecordStatus("1");
        object.setWithParentClass(true);

        poJSON = object.searchRecord(value, byCode);

        if ("success".equals((String) poJSON.get("result"))) {
            poModel.setConvertedID((String)object.getModel().getMeasureId());
        }

        return poJSON;
    }


    public JSONObject searchRecord(String value, int byCode) throws SQLException, GuanzonException{
        List<String> lsFilter = new ArrayList<>();
        String lsSQL = "SELECT "
                + "  a.sCnvrsnID, "
                + "  a.sMeasurID, "
                + "  a.sConvrtID, "
                + "  a.nQtyCnvrt, "
                + "  a.cRecdStat, "
                + "  b.sDescript AS ConvertionFrom, "
                + "  c.sDescript AS ConvertionTo "
                + "FROM Unit_Conversion a "
                + "LEFT JOIN Measure b ON a.sMeasurID = b.sMeasurID "
                + "LEFT JOIN Measure c ON a.sConvrtID = c.sMeasurID ";

        System.out.println("Executing SQL: " + lsSQL);
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsFilter.add("a.cRecdStat IN (" + lsCondition.substring(2) + ")");
        } else {
            lsFilter.add( "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat));
        }

        if (!lsFilter.isEmpty()) {
            lsSQL += " WHERE " + String.join(" AND ", lsFilter);
        }
//        lsSQL += " ORDER BY a.sMeasurID,a.sConvrtID ASC";
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Convert Fom»Convert To»Qty Convert",
                "sCnvrsnID»ConvertionFrom»ConvertionTo»nQtyCnvrt",
                "a.sCnvrsnID»b.sDescript»c.sDescript»a.nQtyCnvrt",
                byCode);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sCnvrsnID"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    public JSONObject RetriveMeasurements(String measureID)
            throws SQLException, GuanzonException {

        poJSON = new JSONObject();
        String lsTransStat = "";
        try {

            String lsSQL = "SELECT "
                    + "  a.sCnvrsnID, "
                    + "  a.sMeasurID, "
                    + "  a.sConvrtID, "
                    + "  a.nQtyCnvrt, "
                    + "  a.cRecdStat, "
                    + "  b.sDescript AS ConvertionFrom, "
                    + "  c.sDescript AS ConvertionTo "
                    + "FROM Unit_Conversion a "
                    + "LEFT JOIN Measure b ON a.sMeasurID = b.sMeasurID "
                    + "LEFT JOIN Measure c ON a.sConvrtID = c.sMeasurID ";

            List<String> lsFilter = new ArrayList<>();
            if (measureID != null && !measureID.trim().isEmpty()) {
                lsFilter.add("a.sMeasurID = " + SQLUtil.toSQL(measureID));
            }

            if (!lsFilter.isEmpty()) {
                lsSQL += " WHERE " + String.join(" AND ", lsFilter);
            }

            lsSQL += " ORDER BY a.sMeasurID,a.sConvrtID ASC";
            System.out.println("Executing SQL: " + lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);

            if (loRS == null) {
                poJSON.put("result", "error");
                poJSON.put("message", "Query execution failed.");
                return poJSON;
            }

            int lnctr = 0;
            JSONArray dataArray = new JSONArray();
            while (loRS.next()) {
                JSONObject record = new JSONObject();
                record.put("ConvertionFrom", loRS.getString( "ConvertionFrom"));
                record.put("ConvertionTo", loRS.getString( "ConvertionTo"));
                record.put("nQtyCnvrt", loRS.getString( "nQtyCnvrt"));
                record.put("cRecdStat", loRS.getString("cRecdStat"));
                dataArray.add(record);
                lnctr++;
            }

            MiscUtil.close(loRS);

            if (lnctr > 0) {
                poJSON.put("result", "success");
                poJSON.put("message", "Record(s) loaded successfully.");
                poJSON.put("data", dataArray);
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "No records found.");
                poJSON.put("data", new JSONArray());
            }

        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }

    public JSONObject seekApproval() throws SQLException, GuanzonException {

        if (poGRider.getUserLevel() <= UserRight.ENCODER) {
            poJSON = ShowDialogFX.getUserApproval(poGRider);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            if (Integer.parseInt(poJSON.get("nUserLevl").toString())
                    <= UserRight.ENCODER) {
                poJSON.put("result", "error");
                poJSON.put("message",
                        "User is not an authorized approving officer..");
                return poJSON;
            }
        }

        poJSON.put("result", "success");
        return poJSON;
    }

    /**
     * Constants representing Unit Conversion record statuses.
     */
    public static class UnitConversionConstant {

        /**
         * Open status
         */
        public static final String OPEN = "0";

        /**
         * DISAPPROVE status
         */
        public static final String DISAPPROVE = "4";

        /**
         * ACTIVE status
         */
        public static final String ACTIVE = "1";

        /**
         * INACTIVE status
         */
        public static final String INACTIVE = "3";

    }


    public JSONObject ActivateRecord(String remarks)
            throws SQLException, GuanzonException, ParseException, CloneNotSupportedException {

        String lsStatus = UnitConversionConstant.ACTIVE;
        poJSON = new JSONObject();
        boolean lbConfirm = true;

        if (getEditMode() != EditMode.READY
                || getEditMode() != EditMode.UPDATE) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
        }

        if (lsStatus.equals(poModel.getRecordStatus())) {
            poJSON.put("error", "Record was already Active.");
            return poJSON;
        }

        poJSON = isEntryOkay();
        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        if (!pbWthParent) {
            poJSON = seekApproval();
            if ("error".equals(poJSON.get("result"))) {
                return poJSON;
            }
        }

        poJSON = statusChange(poModel.getTable(),
                (String) poModel.getValue("sCnvrsnID"),
                remarks, lsStatus, !lbConfirm, false);

        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        poJSON.put("result", "success");
        poJSON.put("message", "Record successfully confirm.");
        return poJSON;
    }
    public JSONObject DeActivateRecord(String remarks)
            throws SQLException, GuanzonException, ParseException, CloneNotSupportedException {

        String lsStatus = UnitConversionConstant.INACTIVE;
        poJSON = new JSONObject();
        boolean lbConfirm = true;

        if (getEditMode() != EditMode.READY
                || getEditMode() != EditMode.UPDATE) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
        }

        if (lsStatus.equals(poModel.getRecordStatus())) {
            poJSON.put("error", "Record was already Inactive.");
            return poJSON;
        }

        poJSON = checkInventoryChildUnit(poModel.getConversionID());
        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = isEntryOkay();
        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        if (!pbWthParent) {
            poJSON = seekApproval();
            if ("error".equals(poJSON.get("result"))) {
                return poJSON;
            }
        }

        poJSON = statusChange(poModel.getTable(),
                (String) poModel.getValue("sCnvrsnID"),
                remarks, lsStatus, !lbConfirm, false);

        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        poJSON.put("result", "success");
        poJSON.put("message", "Record successfully confirm.");
        return poJSON;
    }

    public JSONObject DisapproveRecord(String remarks)
            throws SQLException, GuanzonException, ParseException, CloneNotSupportedException {

        String lsStatus = UnitConversionConstant.DISAPPROVE;
        poJSON = new JSONObject();
        boolean lbConfirm = true;

        if (getEditMode() != EditMode.READY
                || getEditMode() != EditMode.UPDATE) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
        }

        if (lsStatus.equals(poModel.getRecordStatus())) {
            poJSON.put("error", "Record was already Disapproved.");
            return poJSON;
        }

        poJSON = isEntryOkay();
        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        if (!pbWthParent) {
            poJSON = seekApproval();
            if ("error".equals(poJSON.get("result"))) {
                return poJSON;
            }
        }

        poJSON = statusChange(poModel.getTable(),
                (String) poModel.getValue("sCnvrsnID"),
                remarks, lsStatus, !lbConfirm, false);

        if ("error".equals(poJSON.get("result"))) {
            return poJSON;
        }

        poJSON.put("result", "success");
        poJSON.put("message", "Record successfully confirm.");
        return poJSON;
    }

    public void ShowStatusHistory() throws SQLException, GuanzonException, Exception{
        CachedRowSet crs = getStatusHistory();

        crs.beforeFirst();

        while(crs.next()){
            switch (crs.getString("cRefrStat")){
                case "":
                    crs.updateString("cRefrStat", "-");
                    break;
                case UnitConversion.UnitConversionConstant.OPEN:
                    crs.updateString("cRefrStat", "OPEN");
                    break;
                case UnitConversion.UnitConversionConstant.ACTIVE:
                    crs.updateString("cRefrStat", "ACTIVE");
                    break;
                case UnitConversion.UnitConversionConstant.INACTIVE:
                    crs.updateString("cRefrStat", "INACTIVE");
                    break;
                case UnitConversion.UnitConversionConstant.DISAPPROVE:
                    crs.updateString("cRefrStat", "DISAPPROVE");
                    break;
                default:
                    char ch = crs.getString("cRefrStat").charAt(0);
                    String stat = String.valueOf((int) ch - 64);

                    switch (stat){
                        case UnitConversion.UnitConversionConstant.OPEN:
                            crs.updateString("cRefrStat", "OPEN");
                            break;
                        case UnitConversion.UnitConversionConstant.ACTIVE:
                            crs.updateString("cRefrStat", "ACTIVE");
                            break;
                        case UnitConversion.UnitConversionConstant.INACTIVE:
                            crs.updateString("cRefrStat", "INACTIVE");
                            break;
                        case UnitConversion.UnitConversionConstant.DISAPPROVE:
                            crs.updateString("cRefrStat", "DISAPPROVE");
                            break;

                    }
            }
            crs.updateRow();
        }

        JSONObject loJSON  = getEntryBy();
        String entryBy = "";
        String entryDate = "";

        if ("success".equals((String) loJSON.get("result"))){
            entryBy = (String) loJSON.get("sCompnyNm");
            entryDate = (String) loJSON.get("sEntryDte");
        }

        showStatusHistoryUI("Unit Conversion", (String) poModel.getValue("sCnvrsnID"), entryBy, entryDate, crs);
    }

    public JSONObject getEntryBy() throws SQLException, GuanzonException {
        poJSON = new JSONObject();
        String lsEntry = "";
        String lsEntryDate = "";
        String lsSQL =  " SELECT b.sModified, b.dModified "
                + " FROM Unit_Conversion a "
                + " LEFT JOIN xxxAuditLogMaster b ON b.sSourceNo = a.sCnvrsnID AND b.sEventNme LIKE 'ADD%NEW' AND b.sRemarksx = " + SQLUtil.toSQL(poModel.getTable());
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sCnvrsnID =  " + SQLUtil.toSQL(poModel.getConversionID())) ;
        System.out.println("Execute SQL : " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        try {
            if (MiscUtil.RecordCount(loRS) > 0L) {
                if (loRS.next()) {
                    if(loRS.getString("sModified") != null && !"".equals(loRS.getString("sModified"))){
                        if(loRS.getString("sModified").length() > 10){
                            lsEntry = getSysUser(poGRider.Decrypt(loRS.getString("sModified")));
                        } else {
                            lsEntry = getSysUser(loRS.getString("sModified"));
                        }
                        // Get the LocalDateTime from your result set
                        LocalDateTime dModified = loRS.getObject("dModified", LocalDateTime.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                        lsEntryDate =  dModified.format(formatter);
                    }
                }
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            return poJSON;
        }

        poJSON.put("result", "success");
        poJSON.put("sCompnyNm", lsEntry);
        poJSON.put("sEntryDte", lsEntryDate);
        return poJSON;
    }

    public String getSysUser(String fsId) throws SQLException, GuanzonException {
        String lsEntry = "";
        String lsSQL =   " SELECT b.sCompnyNm from xxxSysUser a "
                + " LEFT JOIN Client_Master b ON b.sClientID = a.sEmployNo ";
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sUserIDxx =  " + SQLUtil.toSQL(fsId)) ;
        System.out.println("SQL " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        try {
            if (MiscUtil.RecordCount(loRS) > 0L) {
                if (loRS.next()) {
                    lsEntry = loRS.getString("sCompnyNm");
                }
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return lsEntry;
    }
    public JSONObject getConfirmedBy() throws SQLException, GuanzonException {
        String lsConfirm = "";
        String lsDate = "";
        String lsSQL = "SELECT b.sModified,b.dModified FROM Unit_Conversion a "
                + " LEFT JOIN Parameter_Status_History b ON b.sSourceNo = a.sCnvrsnID AND b.sTableNme = 'Unit_Conversion' "
                + " AND ( b.cRefrStat = "+ SQLUtil.toSQL(UnitConversion.UnitConversionConstant.ACTIVE)
                + " OR (ASCII(b.cRefrStat) - 64)  = "+ SQLUtil.toSQL(UnitConversion.UnitConversionConstant.ACTIVE) + " )";
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sCnvrsnID = " + SQLUtil.toSQL(poModel.getConversionID())) ;
        System.out.println("Execute SQL : " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        try {
            if (MiscUtil.RecordCount(loRS) > 0L) {
                if (loRS.next()) {
                    if (loRS.getString("sModified") != null && !"".equals(loRS.getString("sModified"))) {
                        if (loRS.getString("sModified").length() > 10) {
                            lsConfirm = getSysUser(poGRider.Decrypt(loRS.getString("sModified")));
                        } else {
                            lsConfirm = getSysUser(loRS.getString("sModified"));
                        }
                        // Get the LocalDateTime from your result set
                        LocalDateTime dModified = loRS.getObject("dModified", LocalDateTime.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                        lsDate = dModified.format(formatter);
                    }
                }
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            return poJSON;
        }

        poJSON.put("result", "success");
        poJSON.put("sConfirmed", lsConfirm);
        poJSON.put("sConfrmDte", lsDate);
        return poJSON;
    }

    public JSONObject checkConversionDuplicate(String fsMeasureID, String fsConvertID) throws SQLException, GuanzonException {
        JSONObject loJSON = new JSONObject();

        String lsSQL = "SELECT sCnvrsnID, sMeasurID, sConvrtID "
                + "FROM Unit_Conversion "
                + "WHERE sMeasurID = " + SQLUtil.toSQL(fsMeasureID)
                + " AND sConvrtID = " + SQLUtil.toSQL(fsConvertID)
                + " AND cRecdStat IN ('0','1','3')";

        System.out.println("EXECUTING SQL: " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        if (loRS.next()) {
            loJSON.put("result", "error");
            loJSON.put("message", "This Conversion  already exists.\n"
                    + "Conversion Code: " + loRS.getString("sCnvrsnID"));
        } else {
            loJSON.put("result", "success");
            loJSON.put("message", "No duplicate found. You can proceed.");
        }

        loRS.close();
        return loJSON;
    }

    public JSONObject checkInventoryChildUnit(String sCnvrsnID) throws SQLException, GuanzonException {
        JSONObject loJSON = new JSONObject();

        String lsSQL = "SELECT sStockIDx, sCnvrsnID, cRecdStat "
                + "FROM Inventory_Child_Unit "
                + "WHERE sCnvrsnID = " + SQLUtil.toSQL(sCnvrsnID);

        System.out.println("EXECUTING SQL: " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        if (loRS.next()) {
            String lsRecdStat = loRS.getString("cRecdStat");

            if ("0".equals(lsRecdStat) || "1".equals(lsRecdStat)) {
                loJSON.put("result", "error");
                loJSON.put("message", "This Conversion is being used.\n"
                        + "Inventroy Child Unit Stock ID: " + loRS.getString("sStockIDx"));
            } else if("3".equals(lsRecdStat) || "4".equals(lsRecdStat)) {
                loJSON.put("result", "success");
                loJSON.put("message", "You can proceed.");
            }

        } else {
            loJSON.put("result", "success");
            loJSON.put("message", "You can proceed.");
        }

        loRS.close();
        return loJSON;
    }

}