INSERT INTO Inventory (
  sStockIDx, sBarCodex, sDescript, sBriefDsc, sMeasurID, sIndstCdx,
  cComboInv, cWthPromo, cSerialze, cUnitType, cInvStatx, cRecdStat
) VALUES
('GK0123000060', 'TEST-BARCODE-001', 'Test Inventory Item', 'Test Item', 'M0W2004', '08', '0', '0', '0', '0', '0', '1');

INSERT INTO Inventory_Child_Unit (sStockIDx, nEntryNox, sCnvrsnID, cRecdStat) VALUES
('GK0123000060', 1, 'GCO100000001', '0'),
('GK0123000060', 2, 'GCO100000002', '0');

