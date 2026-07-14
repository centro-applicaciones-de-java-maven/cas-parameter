/*
SQLyog Ultimate v8.55 
MySQL - 5.7.44-log : Database - gcasys_dbf
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gcasys_dbf` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `gcasys_dbf`;

/*Table structure for table `unit_conversion` */

DROP TABLE IF EXISTS `unit_conversion`;

CREATE TABLE `unit_conversion` (
  `sCnvrsnID` varchar(12) NOT NULL,
  `sMeasurID` char(7) DEFAULT NULL,
  `sConvrtID` char(7) DEFAULT NULL,
  `nQtyCnvrt` decimal(8,2) DEFAULT NULL,
  `cRecdStat` char(1) DEFAULT NULL,
  `sModified` varchar(32) DEFAULT NULL,
  `dModified` datetime DEFAULT NULL,
  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sCnvrsnID`),
  KEY `MeasureID` (`sMeasurID`),
  KEY `ConvertID` (`sConvrtID`),
  KEY `RecordState` (`cRecdStat`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
