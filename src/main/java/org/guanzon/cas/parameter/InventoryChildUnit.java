package org.guanzon.cas.parameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.agent.systables.ParameterStatusHistory;
import org.guanzon.appdriver.agent.systables.SysTableContollers;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.Inventory;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.guanzon.cas.inv.services.InvControllers;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Inventory_Child_Unit;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ph.com.guanzongroup.cas.cashflow.status.DisbursementStatic;


//Arsiela 07-11-2026
public class InventoryChildUnit extends Parameter{   
    private String psApprover = "";
    private int pnEditMode = EditMode.UNKNOWN;
    
    Model_Inventory poModel;
    Model_Inventory_Child_Unit poDetail;
    public List<Model_Inventory_Child_Unit> paDetail;
    public List<Model_Inventory_Child_Unit> paOrigDetail;
    
    @Override
    public void initialize() throws SQLException, GuanzonException {
        psRecdStat = Logical.YES;

        ParamModels model = new ParamModels(poGRider);
        poDetail = model.Inventory_Child_Unit();
        poModel = new InvModels(poGRider).Inventory();
        
        paDetail = new ArrayList<>();
        pnEditMode = EditMode.UNKNOWN;
        super.initialize();
    }
    
    @Override
    public int getEditMode(){
        return pnEditMode;
    }

    public static class RecordStatus {
        public static final String OPEN = "0";
        public static final String ACTIVE = "1";
        public static final String DEACTIVATE = "3";
        public static final String DISAPPROVE = "4";

    }

    public String getStatus(String fsStatus) {
        switch (fsStatus){
            case RecordStatus.OPEN:
                return "OPEN";
            case RecordStatus.ACTIVE:
                return "ACTIVE";
            case RecordStatus.DEACTIVATE:
                return "INACTIVE";
            case RecordStatus.DISAPPROVE:
                return "DISAPPROVE";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Requests approval if the current user lacks sufficient rights and
     * validates the approving officer.
     *
     * @return result as a {@link JSONObject}
     */
    public JSONObject callApproval(){
        poJSON = new JSONObject();
        if (poGRider.getUserLevel() <= UserRight.ENCODER) {
            poJSON = ShowDialogFX.getUserApproval(poGRider);
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            if (Integer.parseInt(poJSON.get("nUserLevl").toString()) <= UserRight.ENCODER) {
                poJSON.put("result", "error");
                poJSON.put("message", "User is not an authorized approving officer.");
                return poJSON;
            }
//            setApproving((String) poJSON.get("sUserIDxx"));
            psApprover = (String) poJSON.get("sUserIDxx");
        }   
        
        poJSON.put("result", "success");
        poJSON.put("message", "success");
        return poJSON;
    }
    
    public JSONObject NewTransaction() throws CloneNotSupportedException, SQLException, GuanzonException{   
        poJSON = poModel.newRecord();
        if (!"success".equals((String) poJSON.get("result"))){
            pnEditMode = EditMode.UNKNOWN;
            return poJSON;
        }    
        pnEditMode = EditMode.ADDNEW;
        return poJSON;
    }
    
    public JSONObject OpenTransaction(String fsStockId) throws CloneNotSupportedException, SQLException, GuanzonException{      
        poJSON = poModel.openRecord(fsStockId);
        if (!"success".equals((String) poJSON.get("result"))){
            return poJSON;
        }

        populateDetail();
        
        pnEditMode = poModel.getEditMode();
        return poJSON;
    }
    
    public JSONObject UpdateTransaction() throws CloneNotSupportedException, SQLException{
        poJSON = poModel.updateRecord();
        if (!"success".equals((String) poJSON.get("result"))){
            return poJSON;
        }   
        
        for(int lnCtr = 0; lnCtr <= getDetailCount() - 1; lnCtr++){
            if(Detail(lnCtr).getEditMode() == EditMode.READY){
                poJSON = Detail(lnCtr).updateRecord();
                if (!"success".equals((String) poJSON.get("result"))){
                    return poJSON;
                }    
            }
        }
        
        AddDetail(); //Mandatory add detail
        pnEditMode = poModel.getEditMode();
        return poJSON;
    }
    
    public JSONObject Activate(List<Model_Inventory_Child_Unit> faObject)
            throws ParseException, SQLException, GuanzonException, CloneNotSupportedException, ScriptException {
        poJSON = new JSONObject();

        poJSON = updateStatus(faObject, RecordStatus.ACTIVE);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Record/s activated successfully.");
        return poJSON;
    }

    public JSONObject Deactivate(List<Model_Inventory_Child_Unit> faObject)
            throws ParseException, SQLException, GuanzonException, CloneNotSupportedException, ScriptException {
        poJSON = new JSONObject();

        poJSON = updateStatus(faObject, RecordStatus.DEACTIVATE);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Record/s deactivate successfully.");
        return poJSON;
    }

    public JSONObject Disapprove(List<Model_Inventory_Child_Unit> faObject)
            throws ParseException, SQLException, GuanzonException, CloneNotSupportedException, ScriptException {
        poJSON = new JSONObject();

        poJSON = updateStatus(faObject, RecordStatus.DISAPPROVE);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Record/s dis-approve successfully.");
        return poJSON;
    }

    private JSONObject updateStatus(List<Model_Inventory_Child_Unit> faObject, String fsStatus)
            throws ParseException, SQLException, GuanzonException, CloneNotSupportedException, ScriptException {
        poJSON = new JSONObject();

        poJSON = validateRecordStatus(faObject, fsStatus);
        if (!"success".equals(poJSON.get("result"))) {
            return poJSON;
        }

        String lsRemarks = "";
        psApprover = poGRider.getUserID();
        if(fsStatus.equals(RecordStatus.ACTIVE)
            || fsStatus.equals(RecordStatus.DEACTIVATE)) {
            poJSON = callApproval();
            if (!"success".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
        }
        if (pbWithUI){
            try {
                lsRemarks = ShowDialogFX.getStatusChangeNotes();
            } catch (Exception e) {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", e.getMessage());
                return poJSON;
            }
        }

        for(int lnCtr = 0; lnCtr < faObject.size(); lnCtr++){
            Model_Inventory_Child_Unit faItem = faObject.get(lnCtr);
            String lsSourceNo = faItem.getStockId()+faItem.getEntryNo()+faItem.getConversionId();

            //Save to parameter status history
            poGRider.beginTrans("UPDATE STATUS", lsRemarks, "TSHx",lsSourceNo );
            ParameterStatusHistory loStatus = (new SysTableContollers(poGRider, logwrapr)).ParameterStatusHistory();
            loStatus.setWithParentClass(true);
            poJSON = loStatus.newRecord();
            if (!"success".equals(poJSON.get("result"))) {
                poGRider.rollbackTrans();
                return poJSON;
            }
            loStatus.getModel().setTransactionTable(faItem.getTable());
            loStatus.getModel().setSourceNo(lsSourceNo);
            loStatus.getModel().setRemarks(lsRemarks);
            loStatus.getModel().setStatusRequest(fsStatus);
            loStatus.getModel().setTransactionStatus("1");
            loStatus.getModel().setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
            poJSON = loStatus.saveRecord();
            if (!"success".equals(poJSON.get("result"))) {
                poGRider.rollbackTrans();
                return poJSON;
            }
            String lsSQL = "UPDATE " + faItem.getTable() + " SET   cRecdStat = " + SQLUtil.toSQL(fsStatus);
            String lsCondition = ("sStockIDx = " + SQLUtil.toSQL(faItem.getStockId()))
                                + " AND nEntryNox = " + SQLUtil.toSQL(faItem.getEntryNo())
                                + " AND sCnvrsnID = " + SQLUtil.toSQL(faItem.getConversionId());
            lsSQL = MiscUtil.addCondition(lsSQL, lsCondition);
            if (poGRider.executeQuery(lsSQL, faItem.getTable(), poGRider.getBranchCode(), "", "") <= 0L) {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "Error updating the parameter status.");
                poGRider.rollbackTrans();
                return poJSON;
            }
            poGRider.commitTrans();
        }

        poJSON.put("result", "success");
        return poJSON;
    }

    private JSONObject validateRecordStatus(List<Model_Inventory_Child_Unit> faObject, String fsStatus){
        poJSON = new JSONObject();

        for(int lnCtr = 0; lnCtr < faObject.size(); lnCtr++) {
            Model_Inventory_Child_Unit foObject = faObject.get(lnCtr);
            System.out.println("STATUS : " + foObject.getRecordStatus());
            System.out.println("UPDATE STATUS : " + fsStatus);

            if (foObject.getRecordStatus().equals(fsStatus)) {
                poJSON.put("result", "error");
                poJSON.put("message", "Record is already in " + getStatus(fsStatus) + " status.");
                return poJSON;
            }

            if (foObject.getRecordStatus().equals(RecordStatus.DISAPPROVE)) {
                poJSON.put("result", "error");
                poJSON.put("message", "Record is already in " + getStatus(foObject.getRecordStatus()) + " status.");
                return poJSON;
            }

            if (foObject.getRecordStatus().equals(RecordStatus.OPEN) && fsStatus.equals(RecordStatus.DEACTIVATE)) {
                poJSON.put("result", "error");
                poJSON.put("message", "OPEN record status cannot be deactivated.");
                return poJSON;
            }

            if (foObject.getRecordStatus().equals(RecordStatus.ACTIVE) && fsStatus.equals(RecordStatus.DISAPPROVE)) {
                poJSON.put("result", "error");
                poJSON.put("message", "ACTIVE record status cannot be disapprove.");
                return poJSON;
            }
        }

        poJSON.put("result", "success");
        return poJSON;

    }

    /*Search Master References*/   

    /**
     *
     * @param value
     * @param byCode
     * @return
     * @throws SQLException
     * @throws GuanzonException
     */
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        String lsCondition = "";
        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        lsSQL =  MiscUtil.addCondition(lsSQL, lsCondition);
        lsSQL = lsSQL + " GROUP BY a.sStockIDx ";
        System.out.println("Executing SQL: " + lsSQL);
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Stock Id»Barcode»Description",
                "sStockIDx»Barcode»Inventory",
                "a.sStockIDx»e.sBarCodex»e.sDescript",
                byCode ? 1 : 2);

        if (poJSON != null) {
            try {
                return OpenTransaction((String) poJSON.get("sStockIDx"));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, MiscUtil.getException(ex), ex);
                poJSON.put("result", "error");
                poJSON.put("message", MiscUtil.getException(ex));
                return poJSON;
            }
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject SearchInventory(String value, boolean byCode)
            throws SQLException,
            GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        Inventory object = new InvControllers(poGRider, logwrapr).Inventory();
        object.setRecordStatus(RecordStatus.ACTIVE);
        poJSON = object.searchRecord(value, byCode);
        System.out.println("result" + (String) poJSON.get("result"));
        if ("success".equals((String) poJSON.get("result"))) {
            Master().setStockId(object.getModel().getStockId());

            poJSON = populateDetail();
            if (!"success".equals((String) poJSON.get("result"))){
                return poJSON;
            }
        }

        return poJSON;
    }
    
    public JSONObject populateDetail() throws SQLException, GuanzonException, CloneNotSupportedException{
        poJSON = new JSONObject();
        paDetail = new ArrayList<>();
        
        AddDetail();
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                     " a.sStockIDx = " + SQLUtil.toSQL(Master().getStockId())
                    );
        System.out.println("Executing SQL: " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        if (MiscUtil.RecordCount(loRS) > 0) {
            int lnRow = getDetailCount() - 1;
            while (loRS.next()) {
                poJSON = Detail(lnRow).openRecord(loRS.getString("sStockIDx"),loRS.getString("sCnvrsnID"), loRS.getInt("nEntryNox"));
                if (!"success".equals((String) poJSON.get("result"))){
                    return poJSON;
                }

                if(pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE){
                    poJSON = Detail(lnRow).updateRecord();
                    if (!"success".equals((String) poJSON.get("result"))){
                        return poJSON;
                    }
                }
                AddDetail();
                lnRow = getDetailCount() - 1;
            }
        }
        MiscUtil.close(loRS);
        
        if(poModel.getEditMode() == EditMode.READY){
            Detail().remove(getDetailCount() - 1);
        }
        
        poJSON.put("result", "success");
        poJSON.put("message", "success");
        return poJSON;
    }

    public JSONObject SearchMeasure(String value, boolean byCode, int row)
            throws SQLException,
            GuanzonException {
        poJSON = new JSONObject();
        if(Master().getStockId() == null || "".equals(Master().getStockId())){
            poJSON.put("result", "error");
            poJSON.put("message", "Stock Id cannot be empty");
            return poJSON;
        }
        Measure object = new ParamControllers(poGRider, logwrapr).Measurement();
        object.setRecordStatus(RecordStatus.ACTIVE);
        poJSON = object.searchRecord(value, byCode);
        if ("success".equals((String) poJSON.get("result"))) {
            Detail(row).setMeasureId(object.getModel().getMeasureId());
        }
        return poJSON;
    }
    
    public JSONObject SearchConversion(String value, boolean byCode, int row)
            throws SQLException,
            GuanzonException {
        poJSON = new JSONObject();
        
        if(Master().getStockId() == null || "".equals(Master().getStockId())){
            poJSON.put("result", "error");
            poJSON.put("message", "Stock Id cannot be empty");
            return poJSON;
        }
        
        String lsSQL = " SELECT " +
                    "  a.sCnvrsnID, " +
                    "  a.sMeasurID, " +
                    "  a.sConvrtID, " +
                    "  a.nQtyCnvrt, " +
                    "  a.cRecdStat, " +
                    "  b.sDescript AS Measure, " +
                    "  c.sDescript AS Conversion " +
                    "FROM Unit_Conversion a " +
                    "LEFT JOIN Measure b ON b.sMeasurID = a.sMeasurID " +
                    "LEFT JOIN Measure c ON c.sMeasurID = a.sConvrtID " ;
        lsSQL = MiscUtil.addCondition(lsSQL, "a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE));

        if(Detail(row).getMeasureId() != null && !"".equals(Detail(row).getMeasureId())){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sMeasurID = " + SQLUtil.toSQL(Detail().get(row).getMeasureId()));
        }
        System.out.println("Executing SQL: " + lsSQL);
        JSONObject loJSON = ShowDialogFX.Browse(poGRider,
                lsSQL,
                value,
                "Conversion ID»From UOM»To UOM»Quantity",
                "sCnvrsnID»Measure»Conversion»nQtyCnvrt",
                "a.sCnvrsnID»b.sDescript»c.sDescript»a.nQtyCnvrt",
                byCode ? 0 : 1);
        if (loJSON != null) {
            System.out.println("Conversion ID " + (String) loJSON.get("sCnvrsnID"));
            System.out.println("Measure " + (String) loJSON.get("Measure"));
            System.out.println("Conversion " + (String) loJSON.get("Conversion"));

            //Check if conversion is already exist and active
            for (int lnCtr = 0; lnCtr < getDetailCount(); lnCtr++) {
                if (lnCtr != row && Detail(lnCtr).getConversionId() != null){
                    if(Detail(lnCtr).getConversionId().equals((String) loJSON.get("sCnvrsnID"))
                        && !RecordStatus.DISAPPROVE.equals(Detail(lnCtr).getRecordStatus())) {
                        poJSON = new JSONObject();
                        poJSON.put("result", "error");
                        poJSON.put("message", "Conversion already exist at row "+(lnCtr+1));
                        return poJSON;
                    }
                }
            }

            Detail(row).setConversionId((String) loJSON.get("sCnvrsnID"));
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
    
        poJSON.put("result", "success");
        poJSON.put("message", "success");
        return poJSON;
    }

    /*End - Search Master References*/
    
    public Model_Inventory Master() {
        return (Model_Inventory) poModel;
    }

    public List<Model_Inventory_Child_Unit> Detail() {
      return paDetail;
    }  
    
    public Model_Inventory_Child_Unit Detail(int row) {
        return (Model_Inventory_Child_Unit) paDetail.get(row);
    }  
    
    public int getDetailCount() {
        return paDetail.size();
    }
    
    public JSONObject AddDetail() throws CloneNotSupportedException, SQLException{
        if(Master().getStockId() == null || "".equals(Master().getStockId())){
            return poJSON;
        }
        
        if(getDetailCount() > 0){
            if (Detail(getDetailCount() - 1).getConversionId().isEmpty()) {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "Last row has insufficient detail.");
                return poJSON;
            }
        }
        
        Model_Inventory_Child_Unit loDetail = new ParamModels(poGRider).Inventory_Child_Unit();
        loDetail.newRecord();
        paDetail.add(loDetail);
        Detail(getDetailCount() - 1).setStockId(Master().getStockId());
        return poJSON;
    }
    
    /**
     * Reload Detail for adding or deleting detail
     * @throws CloneNotSupportedException 
     */
    public void ReloadDetail() throws CloneNotSupportedException, SQLException{
        int lnCtr = getDetailCount() - 1;
        while (lnCtr >= 0) {
            if ((Detail(lnCtr).getConversionId() == null || "".equals(Detail(lnCtr).getConversionId()))
                && (Detail(lnCtr).getMeasureId() == null || "".equals(Detail(lnCtr).getMeasureId()))) {
                Detail().remove(lnCtr);
            }
            lnCtr--;
        }

        if ((getDetailCount() - 1) >= 0) {
            if (Detail(getDetailCount() - 1).getConversionId() != null && !"".equals(Detail(getDetailCount() - 1).getConversionId())) {
                AddDetail();
            }
        }

        if ((getDetailCount() - 1) < 0) {
            AddDetail();
        }
    }

    @Override
    public JSONObject willSave() throws SQLException, GuanzonException{
        /*Put system validations and other assignments here*/
        poJSON = new JSONObject();
        
        //remove items with no stockid or quantity order  
        paOrigDetail = new ArrayList<>();
        Iterator<Model_Inventory_Child_Unit> detail = Detail().iterator();
        while (detail.hasNext()) {
            Model_Inventory_Child_Unit item = detail.next(); // Store the item before checking conditions
            paOrigDetail.add(item); //Store original value;
            if (( "".equals((String) item.getValue("sStockIDx")) || (String) item.getValue("sStockIDx") == null)
                    || ( "".equals((String) item.getValue("sCnvrsnID")) || (String) item.getValue("sCnvrsnID") == null)
                    ) {
                detail.remove(); // Correctly remove the item
            }
        }
        
        if (getDetailCount() <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No inventory unit conversion to be saved.");
            paDetail = paOrigDetail; //Store the original values when error occur.
            return poJSON;
        }
        
        if (Detail(0).getConversionId() == null || "".equals(Detail(0).getConversionId())) {
            poJSON.put("result", "error");
            poJSON.put("message", "No inventory unit conversion to be saved.");
            paDetail = paOrigDetail; //Store the original values when error occur.
            return poJSON;
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    /**
     * Save Transaction : For Multiple record to save at once.
     * @return
     * @throws SQLException
     * @throws GuanzonException
     * @throws CloneNotSupportedException 
     */
    public JSONObject SaveTransaction() throws SQLException, GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        if (!pbInitRec) {
            poJSON.put("result", "error");
            poJSON.put("message", "Object is not initialized.");
            return poJSON;
        } 
        
        poJSON = willSave();
        if ("error".equals(poJSON.get("result"))){
            return poJSON; 
        }

        //assign other info on detail
        for (int lnCtr = 0; lnCtr < getDetailCount() ; lnCtr++){
            String lsEvent = ""; //Default Event
            switch(Detail(lnCtr).getEditMode()){
                case EditMode.UPDATE:
                    lsEvent = "UPDATE";
                break;
                case EditMode.ADDNEW:
                    Detail(lnCtr).setRecordStatus(RecordStatus.OPEN);
                    Detail(lnCtr).setStockId(Master().getStockId());
                    Detail(lnCtr).setEntryNo(lnCtr+1);
                    lsEvent = "ADD NEW";
                break;
                default:
                    continue; //Scape
            }
            
            poJSON = isEntryOkay(lnCtr);
            if (!"success".equals(poJSON.get("result"))){
                paDetail = paOrigDetail; //Store the original values when error occur.
                return poJSON; 
            }
            
            Detail(lnCtr).setModifiedDate(poGRider.getServerDate());
            Detail(lnCtr).setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
            
            poGRider.beginTrans(lsEvent,Detail(lnCtr).getTable(), "PARM",String.valueOf(Detail(lnCtr).getValue(1))); 
            poJSON = Detail(lnCtr).saveRecord();
            if ("success".equals(poJSON.get("result"))) {
                poGRider.commitTrans(); //Commit
                
                poJSON = Detail(lnCtr).openRecord(Detail(lnCtr).getStockId(), Detail(lnCtr).getConversionId(), Detail(lnCtr).getEntryNo());
                if (!"success".equals(poJSON.get("result"))) {
                    return poJSON;
                }
                poJSON = Detail(lnCtr).updateRecord();
                if (!"success".equals(poJSON.get("result"))) {
                    return poJSON;
                }
            } else {
                poGRider.rollbackTrans();
                paDetail = paOrigDetail; //Store the original values when error occur.
                return poJSON;
            } 
        }
        poJSON.put("result", "success");
        poJSON.put("message", "Record saved successfully.");
        return poJSON;
    }
    
    @Override
    public String getSQ_Browse(){
        return  " SELECT " +
                "  a.sStockIDx " +
                " , a.nEntryNox " +
                " , a.sCnvrsnID " +
                " , a.cRecdStat " +
                " , c.sDescript AS Measure " +
                " , d.sDescript AS Conversion " +
                " , e.sBarCodex AS Barcode" +
                " , e.sDescript AS Inventory " +
                "FROM Inventory_Child_Unit a " +
                "LEFT JOIN Unit_Conversion b ON b.sCnvrsnID = a.sCnvrsnID " +
                "LEFT JOIN Measure c ON c.sMeasurID = b.sMeasurID " +
                "LEFT JOIN Measure d ON d.sMeasurID = b.sConvrtID " +
                "LEFT JOIN Inventory e ON e.sStockIDx = a.sStockIDx";
    }
    
    protected JSONObject isEntryOkay(int fnRow){
        poJSON = new JSONObject();
        int lnRow = paOrigDetail.indexOf(Detail(fnRow));
        
        if (Detail(fnRow).getStockId() == null || "".equals(Detail(fnRow).getStockId())){
            poJSON.put("result", "error");
            poJSON.put("message", "Stock must not be empty at row "+(lnRow+1)+".");
            return poJSON;
        }
        
        if (Detail(fnRow).getConversionId() == null || "".equals(Detail(fnRow).getConversionId())){
            poJSON.put("result", "error");
            poJSON.put("message", "Conversion must not be empty at row "+(lnRow+1)+".");
            return poJSON;
        }
        
        poJSON.put("result", "success");
                
        return poJSON;
    }

    protected CachedRowSet getStatusHistory(int fnRow) throws SQLException {
        String lsSourceNo = Detail(fnRow).getStockId() + Detail(fnRow).getEntryNo() + Detail(fnRow).getConversionId();
        String lsSQL = "SELECT  a.sTableNme, a.sSourceNo, a.sRemarksx, a.cRefrStat cTranStat, IFNULL(c.sCompnyNm, '-') xModified, IFNULL(e.sCompnyNm, '-') xApproved, a.dModified, a.dApproved, a.sModified, a.sApproved " +
                " FROM GCASys_DBF.Parameter_Status_History a " +
                    "LEFT JOIN GCASys_DBF.xxxSysUser b ON b.sUserIDxx = AES_DECRYPT(UNHEX(a.sModified), '08220326') " +
                    "LEFT JOIN GGC_ISysDBF.Client_Master c ON b.sEmployNo = c.sClientID " +
                    "LEFT JOIN GCASys_DBF.xxxSysUser d ON d.sUserIDxx = AES_DECRYPT(UNHEX(a.sApproved), '08220326') " +
                    "LEFT JOIN GGC_ISysDBF.Client_Master e ON d.sEmployNo = e.sClientID " +
                " WHERE a.sSourceNo = " + SQLUtil.toSQL(lsSourceNo) +
                " AND a.sTableNme = " + SQLUtil.toSQL(Detail(fnRow).getTable()) + " ORDER BY a.dModified";
        System.out.println("STATUS HISTORY : " + lsSQL);
        ResultSet loRS = this.poGRider.executeQuery(lsSQL);
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowset = factory.createCachedRowSet();
        rowset.populate(loRS);
        MiscUtil.close(loRS);
        return rowset;
    }


    /**
     * Loads status history, maps status codes to captions, and displays the status-history dialog.
     *
     * @throws SQLException If a database access error occurs.
     * @throws GuanzonException If model operations fail.
     * @throws Exception If UI rendering fails.
     */
    public void ShowStatusHistory(int fnRow) throws SQLException, GuanzonException, Exception{
        CachedRowSet crs = getStatusHistory(fnRow);

        crs.beforeFirst();

        while(crs.next()){
            switch (crs.getString("cRefrStat")){
                case "":
                    crs.updateString("cRefrStat", "-");
                    break;
                case RecordStatus.OPEN:
                    crs.updateString("cRefrStat", "OPEN");
                    break;
                case RecordStatus.DISAPPROVE:
                    crs.updateString("cRefrStat", "DISAPPROVED");
                    break;
                case RecordStatus.ACTIVE:
                    crs.updateString("cRefrStat", "ACTIVE");
                    break;
                case RecordStatus.DEACTIVATE:
                    crs.updateString("cRefrStat", "INACTIVE");
                    break;
                default:
                    char ch = crs.getString("cRefrStat").charAt(0);
                    String stat = String.valueOf((int) ch - 64);

                    switch (stat){
                        case RecordStatus.OPEN:
                            crs.updateString("cRefrStat", "OPEN");
                            break;
                        case RecordStatus.DISAPPROVE:
                            crs.updateString("cRefrStat", "DISAPPROVED");
                            break;
                        case RecordStatus.ACTIVE:
                            crs.updateString("cRefrStat", "ACTIVE");
                            break;
                        case RecordStatus.DEACTIVATE:
                            crs.updateString("cRefrStat", "INACTIVE");
                            break;
                    }
            }
            crs.updateRow();
        }

        JSONObject loJSON  = getEntryBy(fnRow);
        String entryBy = "";
        String entryDate = "";

        if ("success".equals((String) loJSON.get("result"))){
            entryBy = (String) loJSON.get("sCompnyNm");
            entryDate = (String) loJSON.get("sEntryDte");
        }
        String lsSourceNo = Detail(fnRow).getStockId() + Detail(fnRow).getEntryNo() + Detail(fnRow).getConversionId();
        showStatusHistoryUI("Inventory Child Unit", lsSourceNo, entryBy, entryDate, crs);
    }

    /**
     * Resolves encoder name and entry timestamp from audit logs for the current transaction.
     *
     * @return JSON result containing entry metadata.
     * @throws SQLException If a database access error occurs.
     * @throws GuanzonException If user lookup operations fail.
     */
    public JSONObject getEntryBy(int fnRow) throws SQLException, GuanzonException {
        poJSON = new JSONObject();
        String lsEntry = "";
        String lsEntryDate = "";
        String lsSQL =  " SELECT b.sModified, b.dModified "
                + " FROM Inventory_Child_Unit a " //CONCAT(a.sStockIDx,a.nEntryNox,a.sCnvrsnID)
                + " LEFT JOIN xxxAuditLogMaster b ON b.sSourceNo = a.sStockIDx AND b.sEventNme LIKE 'ADD%NEW' AND b.sRemarksx = " + SQLUtil.toSQL(Detail(fnRow).getTable());
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sStockIDx =  " + SQLUtil.toSQL(Detail(fnRow).getStockId())
                                                + " AND a.nEntryNox =  " + SQLUtil.toSQL(Detail(fnRow).getEntryNo())
                                                + " AND a.sCnvrsnID =  " + SQLUtil.toSQL(Detail(fnRow).getConversionId()));
        lsSQL = lsSQL + " ORDER BY b.dModified DESC ";
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

}