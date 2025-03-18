package org.guanzon.cas.parameter.services;

import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.cas.parameter.AffiliatedCompany;
import org.guanzon.cas.parameter.Banks;
import org.guanzon.cas.parameter.BanksBranch;
import org.guanzon.cas.parameter.Barangay;
import org.guanzon.cas.parameter.Bin;
import org.guanzon.cas.parameter.Branch;
import org.guanzon.cas.parameter.Brand;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.CategoryLevel2;
import org.guanzon.cas.parameter.CategoryLevel3;
import org.guanzon.cas.parameter.CategoryLevel4;
import org.guanzon.cas.parameter.Color;
import org.guanzon.cas.parameter.ColorDetail;
import org.guanzon.cas.parameter.Company;
import org.guanzon.cas.parameter.Country;
import org.guanzon.cas.parameter.Industry;
import org.guanzon.cas.parameter.InvLocation;
import org.guanzon.cas.parameter.InvType;
import org.guanzon.cas.parameter.Made;
import org.guanzon.cas.parameter.Measure;
import org.guanzon.cas.parameter.Model;
import org.guanzon.cas.parameter.ModelVariant;
import org.guanzon.cas.parameter.Province;
import org.guanzon.cas.parameter.Region;
import org.guanzon.cas.parameter.Relationship;
import org.guanzon.cas.parameter.Section;
import org.guanzon.cas.parameter.Size;
import org.guanzon.cas.parameter.Term;
import org.guanzon.cas.parameter.TownCity;
import org.guanzon.cas.parameter.Warehouse;

public class ParamControllers {
    public ParamControllers(GRiderCAS applicationDriver, LogWrapper logWrapper){
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }
    
    public Barangay Barangay() throws SQLException, GuanzonException{
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
    
    public Bin Bin() throws SQLException, GuanzonException{
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
    
    public Branch Branch() throws SQLException, GuanzonException{
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
    
    public Brand Brand() throws SQLException, GuanzonException{
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
    
    public Category Category() throws SQLException, GuanzonException{
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
    
    public CategoryLevel2 CategoryLevel2() throws SQLException, GuanzonException{
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
    
    public CategoryLevel3 CategoryLevel3() throws SQLException, GuanzonException{
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
    
    public CategoryLevel4 CategoryLevel4() throws SQLException, GuanzonException{
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
    
    public Color Color() throws SQLException, GuanzonException{
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
    public ColorDetail ColorDetail() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Color: Application driver is not set.");
            return null;
        }
        
        if (poColorDetail != null) return poColorDetail;
        
        poColorDetail = new ColorDetail();
        poColorDetail.setApplicationDriver(poGRider);
        poColorDetail.setWithParentClass(true);
        poColorDetail.setLogWrapper(poLogWrapper);
        poColorDetail.initialize();
        poColorDetail.newRecord();
        return poColorDetail;        
    }
    
    public Country Country() throws SQLException, GuanzonException{
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
    
    public Industry Industry() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.InventoryLocation: Application driver is not set.");
            return null;
        }
        
        if (poIndustry != null) return poIndustry;
        
        poIndustry = new Industry();
        poIndustry.setApplicationDriver(poGRider);
        poIndustry.setWithParentClass(true);
        poIndustry.setLogWrapper(poLogWrapper);
        poIndustry.initialize();
        poIndustry.newRecord();
        return poIndustry;        
    }
    
    public InvLocation InventoryLocation() throws SQLException, GuanzonException{
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
    
    public InvType InventoryType() throws SQLException, GuanzonException{
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
    
    public Measure Measurement() throws SQLException, GuanzonException{
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
    
    public Model Model() throws SQLException, GuanzonException{
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
    
    public ModelVariant ModelVariant() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.ModelVariant: Application driver is not set.");
            return null;
        }
        
        if (poModelVariant != null) return poModelVariant;
        
        poModelVariant = new ModelVariant();
        poModelVariant.setApplicationDriver(poGRider);
        poModelVariant.setWithParentClass(true);
        poModelVariant.setLogWrapper(poLogWrapper);
        poModelVariant.initialize();
        poModelVariant.newRecord();
        return poModelVariant;        
    }
    
    public Province Province() throws SQLException, GuanzonException{
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
    
    public Region Region() throws SQLException, GuanzonException{
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
    
    public Section Section() throws SQLException, GuanzonException{
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
    
    public TownCity TownCity() throws SQLException, GuanzonException{
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
    
    public Term Term() throws SQLException, GuanzonException{
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
    
    public Warehouse Warehouse() throws SQLException, GuanzonException{
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
    
    public Banks Banks() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Banks: Application driver is not set.");
            return null;
        }
        
        if (poBanks != null) return poBanks;
        
        poBanks = new Banks();
        poBanks.setApplicationDriver(poGRider);
        poBanks.setWithParentClass(true);
        poBanks.setLogWrapper(poLogWrapper);
        poBanks.initialize();
        poBanks.newRecord();
        return poBanks;        
    }
    
    public BanksBranch BanksBranch() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Banks: Application driver is not set.");
            return null;
        }
        
        if (poBanksBranches != null) return poBanksBranches;
        
        poBanksBranches = new BanksBranch();
        poBanksBranches.setApplicationDriver(poGRider);
        poBanksBranches.setWithParentClass(true);
        poBanksBranches.setLogWrapper(poLogWrapper);
        poBanksBranches.initialize();
        poBanksBranches.newRecord();
        return poBanksBranches;        
    }
    
    public Made Made() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Made: Application driver is not set.");
            return null;
        }
        
        if (poMade != null) return poMade;
        
        poMade = new Made();
        poMade.setApplicationDriver(poGRider);
        poMade.setWithParentClass(true);
        poMade.setLogWrapper(poLogWrapper);
        poMade.initialize();
        poMade.newRecord();
        return poMade;        
    }
    
    public Relationship Relationship() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Made: Application driver is not set.");
            return null;
        }
        
        if (poRelationship != null) return poRelationship;
        
        poRelationship = new Relationship();
        poRelationship.setApplicationDriver(poGRider);
        poRelationship.setWithParentClass(true);
        poRelationship.setLogWrapper(poLogWrapper);
        poRelationship.initialize();
        poRelationship.newRecord();
        return poRelationship;        
    }
    
    public Size Size() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.Size: Application driver is not set.");
            return null;
        }
        
        if (poSize != null) return poSize;
        
        poSize = new Size();
        poSize.setApplicationDriver(poGRider);
        poSize.setWithParentClass(true);
        poSize.setLogWrapper(poLogWrapper);
        poSize.initialize();
        poSize.newRecord();
        return poSize;        
    }
    
    public Company Company() throws SQLException, GuanzonException{
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
    
    public AffiliatedCompany AffiliatedCompany() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("ParamControllers.AffiliatedCompany: Application driver is not set.");
            return null;
        }
        
        if (poAffiliatedCompany != null) return poAffiliatedCompany;
        
        poAffiliatedCompany = new AffiliatedCompany();
        poAffiliatedCompany.setApplicationDriver(poGRider);
        poAffiliatedCompany.setWithParentClass(true);
        poAffiliatedCompany.setLogWrapper(poLogWrapper);
        poAffiliatedCompany.initialize();
        poAffiliatedCompany.newRecord();
        return poAffiliatedCompany;        
    }
    
//    public Labor Labor(){
//        if (poGRider == null){
//            poLogWrapper.severe("ParamControllers.Labor: Application driver is not set.");
//            return null;
//        }
//        
//        if (poLabor != null) return poLabor;
//        
//        poLabor = new Labor();
//        poLabor.setApplicationDriver(poGRider);
//        poLabor.setWithParentClass(true);
//        poLabor.setLogWrapper(poLogWrapper);
//        poLabor.initialize();
//        poLabor.newRecord();
//        return poLabor;        
//    }
//    
//    public LaborModel LaborModel(){
//        if (poGRider == null){
//            poLogWrapper.severe("ParamControllers.LaborModel: Application driver is not set.");
//            return null;
//        }
//        
//        if (poLaborModel != null) return poLaborModel;
//        
//        poLaborModel = new LaborModel();
//        poLaborModel.setApplicationDriver(poGRider);
//        poLaborModel.setWithParentClass(true);
//        poLaborModel.setLogWrapper(poLogWrapper);
//        poLaborModel.initialize();
//        poLaborModel.newRecord();
//        return poLaborModel;        
//    }  
//    
//    public LaborCategory LaborCategory(){
//        if (poGRider == null){
//            poLogWrapper.severe("ParamControllers.LaborCategory: Application driver is not set.");
//            return null;
//        }
//        
//        if (poLaborCategory != null) return poLaborCategory;
//        
//        poLaborCategory = new LaborCategory();
//        poLaborCategory.setApplicationDriver(poGRider);
//        poLaborCategory.setWithParentClass(true);
//        poLaborCategory.setLogWrapper(poLogWrapper);
//        poLaborCategory.initialize();
//        poLaborCategory.newRecord();
//        return poLaborCategory;        
//    }
    
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
            poColorDetail = null;
            poCountry = null;
            poInvLocation = null;
            poInvType = null;
            poMeasure = null;
            poModel = null;
            poModelVariant = null;
            poProvince = null;
            poSection = null;
            poTownCity = null;
            poWarehouse = null;
            poBanks = null;
            poBanksBranches = null;
            poMade = null;
            poRelationship = null;
            poSize = null;
            poCompany = null;
            poAffiliatedCompany= null;
//            poLabor = null;
//            poLaborModel = null;
//            poLaborCategory = null;
                    
            poLogWrapper = null;
            poGRider = null;
        } finally {
            super.finalize();
        }
    }
    
    private GRiderCAS poGRider;
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
    private ColorDetail poColorDetail;
    private Country poCountry;
    private Industry poIndustry;
    private InvLocation poInvLocation;
    private InvType poInvType;
    private Measure poMeasure;
    private Model poModel;
    private ModelVariant poModelVariant;
    private Province poProvince;
    private Region poRegion;
    private Section poSection;
    private TownCity poTownCity;
    private Term poTerm;
    private Warehouse poWarehouse;    
    private Banks poBanks;        
    private BanksBranch poBanksBranches;  
    private Made poMade;        
    private Relationship poRelationship;  
    private Size poSize;  
    private Company poCompany;      
    private AffiliatedCompany poAffiliatedCompany; 
//    private Labor poLabor; 
//    private LaborModel poLaborModel; 
//    private LaborCategory poLaborCategory; 
}
