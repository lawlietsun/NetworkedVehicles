CREATE DATABASE  IF NOT EXISTS `networkedv` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `networkedv`;
-- MySQL dump 10.13  Distrib 5.5.24, for osx10.5 (i386)
--
-- Host: eu-cdbr-azure-west-a.cloudapp.net    Database: networkedv
-- ------------------------------------------------------
-- Server version	5.5.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friendship` (
  `idfriendship` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender` bigint(20) NOT NULL,
  `receiver` bigint(20) NOT NULL,
  `confirmed` int(11) DEFAULT NULL,
  PRIMARY KEY (`idfriendship`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `idLocation` bigint(20) NOT NULL AUTO_INCREMENT,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `idVehicle` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idLocation`),
  KEY `idVehicle_idx` (`idVehicle`),
  CONSTRAINT `idVehicle` FOREIGN KEY (`idVehicle`) REFERENCES `vehicle` (`idVehicle`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=63372 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicle` (
  `idVehicle` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(45) NOT NULL,
  `registrationNumber` varchar(45) NOT NULL,
  `owner` varchar(45) NOT NULL,
  `lastLocation` bigint(20) DEFAULT NULL,
  `field1` varchar(45) DEFAULT NULL,
  `field2` varchar(45) DEFAULT NULL,
  `field3` varchar(45) DEFAULT NULL,
  `field4` varchar(45) DEFAULT NULL,
  `field5` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idVehicle`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'networkedv'
--
/*!50003 DROP PROCEDURE IF EXISTS `addLocation` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `addLocation`(IN VEHICLEID BIGINT,IN LAT DOUBLE,IN LON DOUBLE)
BEGIN
INSERT INTO `location`(`latitude`,`longitude`,`idVehicle`,`time`) VALUES (LAT,LON,VEHICLEID,now());
UPDATE `vehicle` SET `lastLocation`=(SELECT LAST_INSERT_ID()) WHERE `idvehicle`=VEHICLEID;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `addVehicle` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `addVehicle`(IN REGNR VARCHAR(45),IN PASS VARCHAR(45),IN OWN VARCHAR(45))
BEGIN
INSERT INTO vehicle(registrationNumber,owner,password) VALUES(REGNR,OWN,PASS);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `findIdVehicle` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `findIdVehicle`(IN regnr VARCHAR(45))
BEGIN
SELECT idVehicle FROM Vehicle WHERE registrationNumber=regnr;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getField` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `getField`(IN VEHICLEID BIGINT,IN MYFIELD VARCHAR(45))
BEGIN
DECLARE res VARCHAR(45);
IF MYFIELD='field1' THEN
SET res=(SELECT field1 AS result FROM vehicle WHERE idVehicle=VEHICLEID);
ELSEIF MYFIELD='field2' THEN
SET res=(SELECT field2 AS result FROM vehicle WHERE idVehicle=VEHICLEID);
ELSEIF MYFIELD='field3' THEN
SET res=(SELECT field3 AS result FROM vehicle WHERE idVehicle=VEHICLEID);
ELSEIF MYFIELD='field4' THEN
SET res=(SELECT field4 AS result FROM vehicle WHERE idVehicle=VEHICLEID);
ELSEIF MYFIELD='field5' THEN
SET res=(SELECT field5 AS result FROM vehicle WHERE idVehicle=VEHICLEID);
END IF;
SELECT res as result;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getLastLocation` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `getLastLocation`(IN VEHICLEID BIGINT)
BEGIN
SELECT * FROM `location` WHERE idLocation = (SELECT `lastLocation` FROM `vehicle` WHERE idVehicle = VEHICLEID); 

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getMyFriends` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `getMyFriends`(IN VEHID BIGINT)
BEGIN
SELECT owner,latitude,longitude,time,idVehicle 
FROM (SELECT owner,lastlocation FROM vehicle
RIGHT JOIN
((SELECT receiver as friend FROM friendship WHERE sender=VEHID) 
UNION
(SELECT sender as friend FROM friendship WHERE receiver=VEHID)) as friends
ON vehicle.idVehicle=friends.friend) as b,location
WHERE location.idLocation=b.lastlocation;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newFriendship` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `newFriendship`(IN SENDERIN BIGINT,IN RECEIVERIN BIGINT)
BEGIN
INSERT INTO friendship(sender,receiver,confirmed) VALUES(SENDERIN,RECEIVERIN,0);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `setField` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`b471ba38db07c5`@`%`*/ /*!50003 PROCEDURE `setField`(IN VEHICLEID BIGINT,IN MYFIELD VARCHAR(45),IN MYVALUE VARCHAR(45))
BEGIN

IF MYFIELD='field1' THEN
UPDATE `vehicle` 
SET field1=MYVALUE WHERE `idvehicle`=VEHICLEID;
ELSEIF MYFIELD='field2' THEN
UPDATE `vehicle` 
SET vehicle.`field2`=MYVALUE WHERE `idvehicle`=VEHICLEID;
ELSEIF MYFIELD='field3' THEN
UPDATE `vehicle` 
SET vehicle.`field3`=MYVALUE WHERE `idvehicle`=VEHICLEID;
ELSEIF MYFIELD='field4' THEN
UPDATE `vehicle` 
SET vehicle.`field4`=MYVALUE WHERE `idvehicle`=VEHICLEID;
ELSEIF MYFIELD='field5' THEN
UPDATE `vehicle` 
SET vehicle.`field5`=MYVALUE WHERE `idvehicle`=VEHICLEID;

END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-10  2:25:58
