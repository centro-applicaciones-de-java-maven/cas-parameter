package org.guanzon.cas.parameter.services;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.cas.parameter.model.Model_Banks;
import org.guanzon.cas.parameter.model.Model_Banks_Branch;
import org.guanzon.cas.parameter.model.Model_Barangay;
import org.guanzon.cas.parameter.model.Model_Bin;
import org.guanzon.cas.parameter.model.Model_Branch;
import org.guanzon.cas.parameter.model.Model_Brand;
import org.guanzon.cas.parameter.model.Model_Category;
import org.guanzon.cas.parameter.model.Model_Category_Level2;
import org.guanzon.cas.parameter.model.Model_Category_Level3;
import org.guanzon.cas.parameter.model.Model_Category_Level4;
import org.guanzon.cas.parameter.model.Model_Color;
import org.guanzon.cas.parameter.model.Model_Color_Detail;
import org.guanzon.cas.parameter.model.Model_Country;
import org.guanzon.cas.parameter.model.Model_Inv_Location;
import org.guanzon.cas.parameter.model.Model_Inv_Type;
import org.guanzon.cas.parameter.model.Model_Made;
import org.guanzon.cas.parameter.model.Model_Measure;
import org.guanzon.cas.parameter.model.Model_Model;
import org.guanzon.cas.parameter.model.Model_Model_Series;
import org.guanzon.cas.parameter.model.Model_Province;
import org.guanzon.cas.parameter.model.Model_Region;
import org.guanzon.cas.parameter.model.Model_Relationship;
import org.guanzon.cas.parameter.model.Model_Section;
import org.guanzon.cas.parameter.model.Model_Term;
import org.guanzon.cas.parameter.model.Model_TownCity;
import org.guanzon.cas.parameter.model.Model_Warehouse;

public class ParamModels {
    public ParamModels(GRider applicationDriver){
        poGRider = applicationDriver;
    }
    
    public Model_Barangay Barangay(){
        if (poGRider == null){
            System.err.println("ParamModels.Barangay: Application driver is not set.");
            return null;
        }
        
        if (poBarangay == null){
            poBarangay = new Model_Barangay();
            poBarangay.setApplicationDriver(poGRider);
            poBarangay.setXML("Model_Barangay");
            poBarangay.setTableName("Barangay");
            poBarangay.initialize();
        }

        return poBarangay;
    }
    
    public Model_Bin Bin(){
        if (poGRider == null){
            System.err.println("ParamModels.Bin: Application driver is not set.");
            return null;
        }
        
        if (poBin == null){
            poBin = new Model_Bin();
            poBin.setApplicationDriver(poGRider);
            poBin.setXML("Model_Bin");
            poBin.setTableName("Bin");
            poBin.initialize();
        }

        return poBin;
    }
    
    public Model_Branch Branch(){
        if (poGRider == null){
            System.err.println("ParamModels.Branch: Application driver is not set.");
            return null;
        }
        
        if (poBranch == null){
            poBranch = new Model_Branch();
            poBranch.setApplicationDriver(poGRider);
            poBranch.setXML("Model_Branch");
            poBranch.setTableName("Branch");
            poBranch.initialize();
        }

        return poBranch;
    }
    
    public Model_Brand Brand(){
        if (poGRider == null){
            System.err.println("ParamModels.Brand: Application driver is not set.");
            return null;
        }
        
        if (poBrand == null){
            poBrand = new Model_Brand();
            poBrand.setApplicationDriver(poGRider);
            poBrand.setXML("Model_Brand");
            poBrand.setTableName("Brand");
            poBrand.initialize();
        }

        return poBrand;
    }
    
    public Model_Category Category(){
        if (poGRider == null){
            System.err.println("ParamModels.Category: Application driver is not set.");
            return null;
        }
        
        if (poCategory == null){
            poCategory = new Model_Category();
            poCategory.setApplicationDriver(poGRider);
            poCategory.setXML("Model_Category");
            poCategory.setTableName("Category");
            poCategory.initialize();
        }

        return poCategory;
    }
    
    public Model_Category_Level2 Category2(){
        if (poGRider == null){
            System.err.println("ParamModels.Category2: Application driver is not set.");
            return null;
        }
        
        if (poCategory2 == null){
            poCategory2 = new Model_Category_Level2();
            poCategory2.setApplicationDriver(poGRider);
            poCategory2.setXML("Model_Category_Level2");
            poCategory2.setTableName("Category_Level2");
            poCategory2.initialize();
        }

        return poCategory2;
    }
    
    public Model_Category_Level3 Category3(){
        if (poGRider == null){
            System.err.println("ParamModels.Category3: Application driver is not set.");
            return null;
        }
        
        if (poCategory3 == null){
            poCategory3 = new Model_Category_Level3();
            poCategory3.setApplicationDriver(poGRider);
            poCategory3.setXML("Model_Category_Level3");
            poCategory3.setTableName("Category_Level3");
            poCategory3.initialize();
        }

        return poCategory3;
    }
    
    public Model_Category_Level4 Category4(){
        if (poGRider == null){
            System.err.println("ParamModels.Category4: Application driver is not set.");
            return null;
        }
        
        if (poCategory4 == null){
            poCategory4 = new Model_Category_Level4();
            poCategory4.setApplicationDriver(poGRider);
            poCategory4.setXML("Model_Category_Level4");
            poCategory4.setTableName("Category_Level4");
            poCategory4.initialize();
        }

        return poCategory4;
    }
    
    public Model_Color Color(){
        if (poGRider == null){
            System.err.println("ParamModels.Color: Application driver is not set.");
            return null;
        }
        
        if (poColor == null){
            poColor = new Model_Color();
            poColor.setApplicationDriver(poGRider);
            poColor.setXML("Model_Color");
            poColor.setTableName("Color");
            poColor.initialize();
        }

        return poColor;
    }
    
    public Model_Color_Detail ColorDetail(){
        if (poGRider == null){
            System.err.println("ParamModels.Color: Application driver is not set.");
            return null;
        }
        
        if (poColorDetail == null){
            poColorDetail = new Model_Color_Detail();
            poColorDetail.setApplicationDriver(poGRider);
            poColorDetail.setXML("Model_Color_Detail");
            poColorDetail.setTableName("Color_Detail");
            poColorDetail.initialize();
        }

        return poColorDetail;
    }
    
    public Model_Country Country(){
        if (poGRider == null){
            System.err.println("ParamModels.Country: Application driver is not set.");
            return null;
        }
        
        if (poCountry == null){
            poCountry = new Model_Country();
            poCountry.setApplicationDriver(poGRider);
            poCountry.setXML("Model_Country");
            poCountry.setTableName("Country");
            poCountry.initialize();
        }

        return poCountry;
    }
    
    public Model_Inv_Location InventoryLocation(){
        if (poGRider == null){
            System.err.println("ParamModels.InventoryLocation: Application driver is not set.");
            return null;
        }
        
        if (poInvLocation == null){
            poInvLocation = new Model_Inv_Location();
            poInvLocation.setApplicationDriver(poGRider);
            poInvLocation.setXML("Model_Inv_Location");
            poInvLocation.setTableName("Inv_Location");
            poInvLocation.initialize();
        }

        return poInvLocation;
    }
    
    public Model_Inv_Type InventoryType(){
        if (poGRider == null){
            System.err.println("ParamModels.InventoryType: Application driver is not set.");
            return null;
        }
        
        if (poInvType == null){
            poInvType = new Model_Inv_Type();
            poInvType.setApplicationDriver(poGRider);
            poInvType.setXML("Model_Inv_Type");
            poInvType.setTableName("Inv_Type");
            poInvType.initialize();
        }

        return poInvType;
    }
    
    public Model_Measure Measurement(){
        if (poGRider == null){
            System.err.println("ParamModels.Measurement: Application driver is not set.");
            return null;
        }
        
        if (poMeasure == null){
            poMeasure = new Model_Measure();
            poMeasure.setApplicationDriver(poGRider);
            poMeasure.setXML("Model_Measure");
            poMeasure.setTableName("Measure");
            poMeasure.initialize();
        }

        return poMeasure;
    }
    
    public Model_Model Model(){
        if (poGRider == null){
            System.err.println("ParamModels.Model: Application driver is not set.");
            return null;
        }
        
        if (poModel == null){
            poModel = new Model_Model();
            poModel.setApplicationDriver(poGRider);
            poModel.setXML("Model_Model");
            poModel.setTableName("Model");
            poModel.initialize();
        }

        return poModel;
    }
    
    public Model_Model_Series ModelSeries(){
        if (poGRider == null){
            System.err.println("ParamModels.ModelSeries: Application driver is not set.");
            return null;
        }
        
        if (poModelSeries == null){
            poModelSeries = new Model_Model_Series();
            poModelSeries.setApplicationDriver(poGRider);
            poModelSeries.setXML("Model_Model_Series");
            poModelSeries.setTableName("Model_Series");
            poModelSeries.initialize();
        }

        return poModelSeries;
    }
    
    public Model_Province Province(){
        if (poGRider == null){
            System.err.println("ParamModels.Province: Application driver is not set.");
            return null;
        }
        
        if (poProvince == null){
            poProvince = new Model_Province();
            poProvince.setApplicationDriver(poGRider);
            poProvince.setXML("Model_Province");
            poProvince.setTableName("Province");
            poProvince.initialize();
        }

        return poProvince;
    }
    
    public Model_Region Region(){
        if (poGRider == null){
            System.err.println("ParamModels.Region: Application driver is not set.");
            return null;
        }
        
        if (poRegion == null){
            poRegion = new Model_Region();
            poRegion.setApplicationDriver(poGRider);
            poRegion.setXML("Model_Region");
            poRegion.setTableName("Region");
            poRegion.initialize();
        }

        return poRegion;
    }
    
    public Model_Section Section(){
        if (poGRider == null){
            System.err.println("ParamModels.Section: Application driver is not set.");
            return null;
        }
        
        if (poSection == null){
            poSection = new Model_Section();
            poSection.setApplicationDriver(poGRider);
            poSection.setXML("Model_Section");
            poSection.setTableName("Section");
            poSection.initialize();
        }

        return poSection;
    }
    
    public Model_TownCity TownCity(){
        if (poGRider == null){
            System.err.println("ParamModels.TownCity: Application driver is not set.");
            return null;
        }
        
        if (poTownCity == null){
            poTownCity = new Model_TownCity();
            poTownCity.setApplicationDriver(poGRider);
            poTownCity.setXML("Model_TownCity");
            poTownCity.setTableName("TownCity");
            poTownCity.initialize();
        }

        return poTownCity;
    }
    public Model_Term Term(){
        if (poGRider == null){
            System.err.println("ParamModels.Term: Application driver is not set.");
            return null;
        }
        
        if (poTerm == null){
            poTerm = new Model_Term();
            poTerm.setApplicationDriver(poGRider);
            poTerm.setXML("Model_Term");
            poTerm.setTableName("Term");
            poTerm.initialize();
        }

        return poTerm;
    }
    
    public Model_Warehouse Warehouse(){
        if (poGRider == null){
            System.err.println("ParamModels.Warehouse: Application driver is not set.");
            return null;
        }
        
        if (poWarehouse == null){
            poWarehouse = new Model_Warehouse();
            poWarehouse.setApplicationDriver(poGRider);
            poWarehouse.setXML("Model_Warehouse");
            poWarehouse.setTableName("Warehouse");
            poWarehouse.initialize();
        }

        return poWarehouse;
    }
    
    public Model_Banks Banks(){
        if (poGRider == null){
            System.err.println("ParamModels.Banks: Application driver is not set.");
            return null;
        }
        
        if (poBanks == null){
            poBanks = new Model_Banks();
            poBanks.setApplicationDriver(poGRider);
            poBanks.setXML("Model_Banks");
            poBanks.setTableName("Banks");
            poBanks.initialize();
        }

        return poBanks;
    }
    public Model_Banks_Branch BanksBranch(){
        if (poGRider == null){
            System.err.println("ParamModels.Banks: Application driver is not set.");
            return null;
        }
        
        if (poBanksBranch == null){
            poBanksBranch = new Model_Banks_Branch();
            poBanksBranch.setApplicationDriver(poGRider);
            poBanksBranch.setXML("Model_Banks_Branch");
            poBanksBranch.setTableName("Banks_Branches");
            poBanksBranch.initialize();
        }

        return poBanksBranch;
    }
    
    public Model_Made Made(){
        if (poGRider == null){
            System.err.println("ParamModels.Banks: Application driver is not set.");
            return null;
        }
        
        if (poMade == null){
            poMade = new Model_Made();
            poMade.setApplicationDriver(poGRider);
            poMade.setXML("Model_Made");
            poMade.setTableName("made");
            poMade.initialize();
        }

        return poMade;
    }
    public Model_Relationship Relationship(){
        if (poGRider == null){
            System.err.println("ParamModels.Relationship: Application driver is not set.");
            return null;
        }
        
        if (poRelationship == null){
            poRelationship = new Model_Relationship();
            poRelationship.setApplicationDriver(poGRider);
            poRelationship.setXML("Model_Relationship");
            poRelationship.setTableName("Relationship");
            poRelationship.initialize();
        }

        return poRelationship;
    }
    
    private final GRider poGRider;
    
    private Model_Barangay poBarangay;
    private Model_Bin poBin;
    private Model_Branch poBranch;
    private Model_Brand poBrand;
    private Model_Category poCategory;
    private Model_Category_Level2 poCategory2;
    private Model_Category_Level3 poCategory3;
    private Model_Category_Level4 poCategory4;
    private Model_Color poColor;    
    private Model_Color_Detail poColorDetail;
    private Model_Country poCountry;
    private Model_Inv_Location poInvLocation;
    private Model_Inv_Type poInvType;
    private Model_Measure poMeasure;
    private Model_Model poModel;
    private Model_Model_Series poModelSeries;
    private Model_Province poProvince;
    private Model_Region poRegion;
    private Model_Section poSection;
    private Model_TownCity poTownCity;
    private Model_Term poTerm;
    private Model_Warehouse poWarehouse;    
    private Model_Banks poBanks;
    private Model_Banks_Branch poBanksBranch;       
    private Model_Made poMade;    
    private Model_Relationship poRelationship;    
}
