package org.guanzon.cas.parameter.model;

import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.cas.cashflow.model.Model_Account_ChartX;

import java.sql.SQLException;
import java.util.Date;

public class Model_Unit_Conversion extends Model {
    Model_Measure poMeasure;
    Model_Measure poConvert;
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            
            poEntity.updateObject("nQtyCnvrt", 0.00);
            poEntity.updateString("cRecdStat", "0");
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            poMeasure = new ParamModels(poGRider).Measurement();
            poConvert = new ParamModels(poGRider).Measurement();
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }


    
    public JSONObject setConversionID(String conversionID) {
        return setValue("sCnvrsnID", conversionID);
    }

    public String getConversionID() {
        return (String) getValue("sCnvrsnID");
    }

    public JSONObject setMeasureID(String measureID) {
        return setValue("sMeasurID", measureID);
    }

    public String getMeasureID() {
        return (String) getValue("sMeasurID");
    }
    
    public JSONObject setConvertedID(String convertedID) {
        return setValue("sConvrtID", convertedID);
    }

    public String getConvertedID() {
        return (String) getValue("sConvrtID");
    }
    
    public JSONObject setQuantityConverted(double quantityConverted) {
        return setValue("nQtyCnvrt", quantityConverted);
    }

    public double getQuantityConverted() {
        return Double.parseDouble(String.valueOf(getValue("nQtyCnvrt")));

    }
    
    public JSONObject setRecordStatus(String recordStatus){
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
    
    @Override
    public String getNextCode(){
        return MiscUtil.getNextCode(getTable(), ID, false, poGRider.getGConnection().getConnection(), poGRider.getBranchCode()); 
    }

    public Model_Measure Measurement() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sMeasurID"))) {
            if (poMeasure.getEditMode() == EditMode.READY
                    && poMeasure.getMeasureId().equals((String) getValue("sMeasurID"))) {
                return poMeasure;
            } else {
                poJSON = poMeasure.openRecord((String) getValue("sMeasurID"));

                if ("success".equals((String) poJSON.get("result"))) {
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
    public Model_Measure ConvertTo() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sConvrtID"))) {
            if (poConvert.getEditMode() == EditMode.READY
                    && poConvert.getMeasureId().equals((String) getValue("sConvrtID"))) {
                return poConvert;
            } else {
                poJSON = poConvert.openRecord((String) getValue("sConvrtID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poConvert;
                } else {
                    poConvert.initialize();
                    return poConvert;
                }
            }
        } else {
            poConvert.initialize();
            return poConvert;
        }
    }

}
