DROP TABLE IF EXISTS Inventory_Child_Unit;
DROP TABLE IF EXISTS Unit_Conversion;
DROP TABLE IF EXISTS Inventory;
DROP TABLE IF EXISTS Measure;

CREATE TABLE Measure (
  sMeasurID CHAR(7) NOT NULL,
  sDescript VARCHAR(32) NOT NULL,
  cRecdStat CHAR(1) DEFAULT '1',
  sModified VARCHAR(32),
  dModified TIMESTAMP,
  dTimeStmp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (sMeasurID)
);

CREATE TABLE Unit_Conversion (
  sCnvrsnID VARCHAR(12) NOT NULL,
  sMeasurID CHAR(7),
  sConvrtID CHAR(7),
  nQtyCnvrt DECIMAL(8,2),
  cRecdStat CHAR(1),
  sModified VARCHAR(32),
  dModified TIMESTAMP,
  dTimeStmp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (sCnvrsnID)
);

CREATE TABLE Inventory (
  sStockIDx VARCHAR(12) NOT NULL,
  sBarCodex VARCHAR(25),
  sDescript VARCHAR(256),
  sBriefDsc VARCHAR(25),
  sAltBarCd VARCHAR(25),
  sCategCd1 VARCHAR(7),
  sCategCd2 VARCHAR(7),
  sCategCd3 VARCHAR(7),
  sCategCd4 VARCHAR(7),
  sBrandIDx VARCHAR(8),
  sModelIDx VARCHAR(9),
  sColorIDx VARCHAR(7),
  sVrntIDxx VARCHAR(5),
  sMeasurID VARCHAR(7),
  sInvTypCd VARCHAR(4),
  sIndstCdx CHAR(2),
  nUnitPrce DECIMAL(13,4),
  nSelPrice DECIMAL(11,2),
  nDiscLev1 DECIMAL(8,2),
  nDiscLev2 DECIMAL(8,2),
  nDiscLev3 DECIMAL(8,2),
  nDealrDsc DECIMAL(8,2),
  nMinLevel SMALLINT,
  nMaxLevel SMALLINT,
  cComboInv CHAR(1) DEFAULT '0',
  cWthPromo CHAR(1) DEFAULT '0',
  cSerialze CHAR(1) DEFAULT '0',
  cUnitType CHAR(1) DEFAULT '0',
  cInvStatx CHAR(1) DEFAULT '0',
  nShlfLife SMALLINT,
  sSupersed VARCHAR(16),
  cRecdStat CHAR(1) DEFAULT '1',
  sModified VARCHAR(32),
  dModified TIMESTAMP,
  dTimeStmp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (sStockIDx)
);

CREATE TABLE Inventory_Child_Unit (
  sStockIDx VARCHAR(12) NOT NULL,
  nEntryNox TINYINT NOT NULL,
  sCnvrsnID VARCHAR(12) NOT NULL,
  cRecdStat CHAR(1),
  sModified VARCHAR(32),
  dModified TIMESTAMP,
  dTimeStmp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (sStockIDx, nEntryNox, sCnvrsnID)
);

