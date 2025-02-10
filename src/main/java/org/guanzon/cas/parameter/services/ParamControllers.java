package org.guanzon.cas.parameter.services;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.cas.parameter.Barangay;
import org.guanzon.cas.parameter.Bin;
import org.guanzon.cas.parameter.Branch;
import org.guanzon.cas.parameter.Brand;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.CategoryLevel2;
import org.guanzon.cas.parameter.CategoryLevel3;
import org.guanzon.cas.parameter.CategoryLevel4;
import org.guanzon.cas.parameter.Color;
import org.guanzon.cas.parameter.Company;
import org.guanzon.cas.parameter.Country;
import org.guanzon.cas.parameter.InvLocation;
import org.guanzon.cas.parameter.InvType;
import org.guanzon.cas.parameter.Measure;
import org.guanzon.cas.parameter.Model;
import org.guanzon.cas.parameter.ModelSeries;
import org.guanzon.cas.parameter.Province;
import org.guanzon.cas.parameter.Region;
import org.guanzon.cas.parameter.Section;
import org.guanzon.cas.parameter.Term;
import org.guanzon.cas.parameter.TownCity;
import org.guanzon.cas.parameter.Warehouse;

public class ParamControllers {
    public ParamControllers(GRider applicationDriver, LogWrapper logWrapper){
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }
    
    public Barangay Barangay(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Barangay: Application driver is not set.");
            return null;
        }
        
        if (poBarangay != null) return poBarangay;
        
        poBarangay = new Barangay();
        poBarangay.setApplicationDriver(poGRider);
        poBarangay.setWithParentClass(true);
        poBarangay.setLogWrapper(poLogWrapper);
        poBarangay.initialize();
        poBarangay.newRecord();
        return poBarangay;        
    }
    
    public Bin Bin(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Bin: Application driver is not set.");
            return null;
        }
        
        if (poBin != null) return poBin;
        
        poBin = new Bin();
        poBin.setApplicationDriver(poGRider);
        poBin.setWithParentClass(true);
        poBin.setLogWrapper(poLogWrapper);
        poBin.initialize();
        poBin.newRecord();
        return poBin;        
    }
    
    public Branch Branch(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Branch: Application driver is not set.");
            return null;
        }
        
        if (poBranch != null) return poBranch;
        
        poBranch = new Branch();
        poBranch.setApplicationDriver(poGRider);
        poBranch.setWithParentClass(true);
        poBranch.setLogWrapper(poLogWrapper);
        poBranch.initialize();
        poBranch.newRecord();
        return poBranch;        
    }
    
    public Brand Brand(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Brand: Application driver is not set.");
            return null;
        }
        
        if (poBrand != null) return poBrand;
        
        poBrand = new Brand();
        poBrand.setApplicationDriver(poGRider);
        poBrand.setWithParentClass(true);
        poBrand.setLogWrapper(poLogWrapper);
        poBrand.initialize();
        poBrand.newRecord();
        return poBrand;        
    }
    
    public Category Category(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Category: Application driver is not set.");
            return null;
        }
        
        if (poCategory != null) return poCategory;
        
        poCategory = new Category();
        poCategory.setApplicationDriver(poGRider);
        poCategory.setWithParentClass(true);
        poCategory.setLogWrapper(poLogWrapper);
        poCategory.initialize();
        poCategory.newRecord();
        return poCategory;        
    }
    
    public CategoryLevel2 CategoryLevel2(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.CategoryLevel2: Application driver is not set.");
            return null;
        }
        
        if (poCategory2 != null) return poCategory2;
        
        poCategory2 = new CategoryLevel2();
        poCategory2.setApplicationDriver(poGRider);
        poCategory2.setWithParentClass(true);
        poCategory2.setLogWrapper(poLogWrapper);
        poCategory2.initialize();
        poCategory2.newRecord();
        return poCategory2;        
    }
    
    public CategoryLevel3 CategoryLevel3(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.CategoryLevel3: Application driver is not set.");
            return null;
        }
        
        if (poCategory3 != null) return poCategory3;
        
        poCategory3 = new CategoryLevel3();
        poCategory3.setApplicationDriver(poGRider);
        poCategory3.setWithParentClass(true);
        poCategory3.setLogWrapper(poLogWrapper);
        poCategory3.initialize();
        poCategory3.newRecord();
        return poCategory3;        
    }
    
    public CategoryLevel4 CategoryLevel4(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.CategoryLevel4: Application driver is not set.");
            return null;
        }
        
        if (poCategory4 != null) return poCategory4;
        
        poCategory4 = new CategoryLevel4();
        poCategory4.setApplicationDriver(poGRider);
        poCategory4.setWithParentClass(true);
        poCategory4.setLogWrapper(poLogWrapper);
        poCategory4.initialize();
        poCategory4.newRecord();
        return poCategory4;        
    }
    
    public Color Color(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Color: Application driver is not set.");
            return null;
        }
        
        if (poColor != null) return poColor;
        
        poColor = new Color();
        poColor.setApplicationDriver(poGRider);
        poColor.setWithParentClass(true);
        poColor.setLogWrapper(poLogWrapper);
        poColor.initialize();
        poColor.newRecord();
        return poColor;        
    }
    
    public Company Company(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Company: Application driver is not set.");
            return null;
        }
        
        if (poCompany != null) return poCompany;
        
        poCompany = new Company();
        poCompany.setApplicationDriver(poGRider);
        poCompany.setWithParentClass(true);
        poCompany.setLogWrapper(poLogWrapper);
        poCompany.initialize();
        poCompany.newRecord();
        return poCompany;        
    }
    
    public Country Country(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Country: Application driver is not set.");
            return null;
        }
        
        if (poCountry != null) return poCountry;
        
        poCountry = new Country();
        poCountry.setApplicationDriver(poGRider);
        poCountry.setWithParentClass(true);
        poCountry.setLogWrapper(poLogWrapper);
        poCountry.initialize();
        poCountry.newRecord();
        return poCountry;        
    }
    
    public InvLocation InventoryLocation(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.InventoryLocation: Application driver is not set.");
            return null;
        }
        
        if (poInvLocation != null) return poInvLocation;
        
        poInvLocation = new InvLocation();
        poInvLocation.setApplicationDriver(poGRider);
        poInvLocation.setWithParentClass(true);
        poInvLocation.setLogWrapper(poLogWrapper);
        poInvLocation.initialize();
        poInvLocation.newRecord();
        return poInvLocation;        
    }
    
    public InvType InventoryType(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.InventoryType: Application driver is not set.");
            return null;
        }
        
        if (poInvType != null) return poInvType;
        
        poInvType = new InvType();
        poInvType.setApplicationDriver(poGRider);
        poInvType.setWithParentClass(true);
        poInvType.setLogWrapper(poLogWrapper);
        poInvType.initialize();
        poInvType.newRecord();
        return poInvType;        
    }
    
    public Measure Measurement(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Measurement: Application driver is not set.");
            return null;
        }
        
        if (poMeasure != null) return poMeasure;
        
        poMeasure = new Measure();
        poMeasure.setApplicationDriver(poGRider);
        poMeasure.setWithParentClass(true);
        poMeasure.setLogWrapper(poLogWrapper);
        poMeasure.initialize();
        poMeasure.newRecord();
        return poMeasure;        
    }
    
    public Model Model(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Model: Application driver is not set.");
            return null;
        }
        
        if (poModel != null) return poModel;
        
        poModel = new Model();
        poModel.setApplicationDriver(poGRider);
        poModel.setWithParentClass(true);
        poModel.setLogWrapper(poLogWrapper);
        poModel.initialize();
        poModel.newRecord();
        return poModel;        
    }
    
    public ModelSeries ModelSeries(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.ModelSeries: Application driver is not set.");
            return null;
        }
        
        if (poModelSeries != null) return poModelSeries;
        
        poModelSeries = new ModelSeries();
        poModelSeries.setApplicationDriver(poGRider);
        poModelSeries.setWithParentClass(true);
        poModelSeries.setLogWrapper(poLogWrapper);
        poModelSeries.initialize();
        poModelSeries.newRecord();
        return poModelSeries;        
    }
    
    public Province Province(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Province: Application driver is not set.");
            return null;
        }
        
        if (poProvince != null) return poProvince;
        
        poProvince = new Province();
        poProvince.setApplicationDriver(poGRider);
        poProvince.setWithParentClass(true);
        poProvince.setLogWrapper(poLogWrapper);
        poProvince.initialize();
        poProvince.newRecord();
        return poProvince;        
    }
    
    public Region Region(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Region: Application driver is not set.");
            return null;
        }
        
        if (poRegion != null) return poRegion;
        
        poRegion = new Region();
        poRegion.setApplicationDriver(poGRider);
        poRegion.setWithParentClass(true);
        poRegion.setLogWrapper(poLogWrapper);
        poRegion.initialize();
        poRegion.newRecord();
        return poRegion;        
    }
    
    public Section Section(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Section: Application driver is not set.");
            return null;
        }
        
        if (poSection != null) return poSection;
        
        poSection = new Section();
        poSection.setApplicationDriver(poGRider);
        poSection.setWithParentClass(true);
        poSection.setLogWrapper(poLogWrapper);
        poSection.initialize();
        poSection.newRecord();
        return poSection;        
    }
    
    public TownCity TownCity(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.TownCity: Application driver is not set.");
            return null;
        }
        
        if (poTownCity != null) return poTownCity;
        
        poTownCity = new TownCity();
        poTownCity.setApplicationDriver(poGRider);
        poTownCity.setWithParentClass(true);
        poTownCity.setLogWrapper(poLogWrapper);
        poTownCity.initialize();
        poTownCity.newRecord();
        return poTownCity;        
    }
    
    public Term Term(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.TownCity: Application driver is not set.");
            return null;
        }
        
        if (poTerm != null) return poTerm;
        
        poTerm = new Term();
        poTerm.setApplicationDriver(poGRider);
        poTerm.setWithParentClass(true);
        poTerm.setLogWrapper(poLogWrapper);
        poTerm.initialize();
        poTerm.newRecord();
        return poTerm;        
    }
    
    public Warehouse Warehouse(){
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Warehouse: Application driver is not set.");
            return null;
        }
        
        if (poWarehouse != null) return poWarehouse;
        
        poWarehouse = new Warehouse();
        poWarehouse.setApplicationDriver(poGRider);
        poWarehouse.setWithParentClass(true);
        poWarehouse.setLogWrapper(poLogWrapper);
        poWarehouse.initialize();
        poWarehouse.newRecord();
        return poWarehouse;        
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            poBarangay = null;
            poBin = null;
            poBrand = null;
            poCategory = null;
            poCategory2 = null;
            poCategory3 = null;
            poCategory4 = null;
            poColor = null;
            poCompany = null;
            poCountry = null;
            poInvLocation = null;
            poInvType = null;
            poMeasure = null;
            poModel = null;
            poModelSeries = null;
            poProvince = null;
            poSection = null;
            poTownCity = null;
            poWarehouse = null;
            
            poLogWrapper = null;
            poGRider = null;
        } finally {
            super.finalize();
        }
    }
    
    private GRider poGRider;
    private LogWrapper poLogWrapper;
    
    private Barangay poBarangay;
    private Bin poBin;
    private Branch poBranch;
    private Brand poBrand;
    private Category poCategory;
    private CategoryLevel2 poCategory2;
    private CategoryLevel3 poCategory3;
    private CategoryLevel4 poCategory4;
    private Color poColor;
    private Company poCompany;
    private Country poCountry;
    private InvLocation poInvLocation;
    private InvType poInvType;
    private Measure poMeasure;
    private Model poModel;
    private ModelSeries poModelSeries;
    private Province poProvince;
    private Region poRegion;
    private Section poSection;
    private TownCity poTownCity;
    private Term poTerm;
    private Warehouse poWarehouse;        
}
