INSERT INTO Measure (sMeasurID, sDescript, cRecdStat) VALUES
('M0W2004', 'LITER', '1'),
('M0W2011', 'KILOMETER', '1'),
('M0W2013', 'METER', '1'),
('M0W2014', 'OUNCE', '1'),
('M0W2026', 'MILLIGRAM', '1');

INSERT INTO Unit_Conversion (sCnvrsnID, sMeasurID, sConvrtID, nQtyCnvrt, cRecdStat) VALUES
('GCO100000001', 'M0W2011', 'M0W2013', 1000.00, '1'),
('GCO100000002', 'M0W2011', 'M0W2014', 350.00, '1'),
('GCO100000003', 'M0W2004', 'M0W2026', 15.00, '1'),
('GCO100000004', 'M0W2004', 'M0W2011', 1000.00, '1');

INSERT INTO Inventory (
  sStockIDx, sBarCodex, sDescript, sBriefDsc, sMeasurID, sIndstCdx,
  cComboInv, cWthPromo, cSerialze, cUnitType, cInvStatx, cRecdStat
) VALUES
('GK0123000060', 'TEST-BARCODE-001', 'Test Inventory Item', 'Test Item', 'M0W2004', '08', '0', '0', '0', '0', '0', '1');

INSERT INTO Inventory_Child_Unit (sStockIDx, nEntryNox, sCnvrsnID, cRecdStat) VALUES
('GK0123000060', 1, 'GCO100000001', '0'),
('GK0123000060', 2, 'GCO100000002', '0');

