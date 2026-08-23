package org.guanzon.cas.parameter.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.ReferenceCache;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.guanzon.cas.inv.services.InvModels;
import org.json.simple.JSONObject;

public class Model_Inventory_Child_Unit extends Model {

    private Model_Inventory poInventory;
    String psMeasure = "";
    private Model_Unit_Conversion poConversion;
    private Model_Measure poMeasure;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateObject("nEntryNox", 0);
            poEntity.updateString("cRecdStat", "0"); //OPEN
            poEntity.updateObject("dModified", poGRider.getServerDate());
            //end - assign default values

            poInventory = new InvModels(poGRider).Inventory();
            //poConversion/poMeasure are intentionally NOT constructed here - see UnitConversion()/
            //Measure() below, which build them lazily on first access so opening this record
            //never touches the Unit_Conversion/Measure tables.

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            ID2 = poEntity.getMetaData().getColumnLabel(3);
            ID3 = poEntity.getMetaData().getColumnLabel(2);
            
            pnEditMode = EditMode.UNKNOWN;
            psMeasure = "";
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setStockId(String stockId) {
        return setValue("sStockIDx", stockId);
    }

    public String getStockId() {
        return (String) getValue("sStockIDx");
    }
    
    public JSONObject setEntryNo(int entryNumber){
        return setValue("nEntryNox", entryNumber);
    }

    public int getEntryNo() {
        return (int) getValue("nEntryNox");
    }

    public JSONObject setConversionId(String conversionId) {
        return setValue("sCnvrsnID", conversionId);
    }

    public String getConversionId() {
        return (String) getValue("sCnvrsnID");
    }

    public JSONObject setRecordStatus(String recordStatus) {
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }

    public JSONObject setModifyingId(String modifyingId) {
        return setValue("sModified", modifyingId);
    }

    public String getModifyingId() {
        return (String) getValue("sModified");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }

    public void setMeasureId(String measure) {
        psMeasure = measure;
    }

    public String getMeasureId() {
        return psMeasure;
    }

    @Override
    public String getNextCode() {
        return "";
    }

    public Model_Inventory Inventory() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sStockIDx"))) {
            if (poInventory.getEditMode() == EditMode.READY
                    && poInventory.getStockId().equals((String) getValue("sStockIDx"))) {
                return poInventory;
            } else {
                poJSON = poInventory.openRecord((String) getValue("sStockIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInventory;
                } else {
                    poInventory.initialize();
                    return poInventory;
                }
            }
        } else {
            poInventory.initialize();
            return poInventory;
        }
    }

    public Model_Unit_Conversion UnitConversion() throws SQLException, GuanzonException {
        if (poConversion == null) {
            poConversion = new Model_Unit_Conversion();
            poConversion.setApplicationDriver(poGRider);
            poConversion.setXML("Model_Unit_Conversion");
            poConversion.setTableName("Unit_Conversion");
            poConversion.initialize();
        }

        String conversionId = (String) getValue("sCnvrsnID");

        if (!"".equals(conversionId)) {
            if (poConversion.getEditMode() == EditMode.READY
                    && poConversion.getConversionID().equals(conversionId)) {
                return poConversion;
            } else {
                if (ReferenceCache.tryLoad("Unit_Conversion", conversionId, poConversion)) {
                    return poConversion;
                }

                poJSON = poConversion.openRecord(conversionId);

                if ("success".equals((String) poJSON.get("result"))) {
                    ReferenceCache.store("Unit_Conversion", conversionId, poConversion);
                    return poConversion;
                } else {
                    poConversion.initialize();
                    return poConversion;
                }
            }
        } else {
            poConversion.initialize();
            return poConversion;
        }
    }

    public Model_Measure Measure() throws SQLException, GuanzonException {
        if (poMeasure == null) {
            poMeasure = new Model_Measure();
            poMeasure.setApplicationDriver(poGRider);
            poMeasure.setXML("Model_Measure");
            poMeasure.setTableName("Measure");
            poMeasure.initialize();
        }

        if (!"".equals(UnitConversion().getMeasureID())) {
            psMeasure = UnitConversion().getMeasureID();
            if (poMeasure.getEditMode() == EditMode.READY && poMeasure.getMeasureId().equals(psMeasure)) {
                return poMeasure;
            } else {
                if (ReferenceCache.tryLoad("Measure", psMeasure, poMeasure)) {
                    return poMeasure;
                }

                poJSON = poMeasure.openRecord(psMeasure);
                if ("success".equals((String) poJSON.get("result"))) {
                    ReferenceCache.store("Measure", psMeasure, poMeasure);
                    return poMeasure;
                } else {
                    poMeasure.initialize();
                    return poMeasure;
                }
            }
        } else if (!"".equals(psMeasure)) {
            if (poMeasure.getEditMode() == EditMode.READY && poMeasure.getMeasureId().equals(psMeasure)) {
                return poMeasure;
            } else {
                if (ReferenceCache.tryLoad("Measure", psMeasure, poMeasure)) {
                    return poMeasure;
                }

                poJSON = poMeasure.openRecord(psMeasure);
                if ("success".equals((String) poJSON.get("result"))) {
                    ReferenceCache.store("Measure", psMeasure, poMeasure);
                    return poMeasure;
                } else {
                    poMeasure.initialize();
                    return poMeasure;
                }
            }
        } else {
            poMeasure.initialize();
            return poMeasure;
        }
    }



}
