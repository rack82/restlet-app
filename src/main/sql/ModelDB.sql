CREATE DATABASE  IF NOT EXISTS `ModelDB` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ModelDB`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.24-log

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

-- -----------------------------------------------------
-- Table `ModelDB`.`user`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `surname` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `Owner` INT(11) NOT NULL,
  `AccessToken` VARCHAR(255) NULL,
  `Status` INT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'a@b.com','Κώστας','Παπαδόπουλος','pass123',1, NULL,1),(2,'b@a.com','Πέτρος','Παπαδημητρίου','pass234',0, NULL,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
-- -----------------------------------------------------
-- Table `ModelDB`.`Items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Items` (
  `ItemId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Price` INT(11) NOT NULL,
  `Details` VARCHAR(255) NULL,
  `Photo_target` VARCHAR(255) NULL,
  PRIMARY KEY (`ItemId`))
ENGINE = InnoDB;

LOCK TABLES `Items` WRITE;

INSERT INTO `Items` VALUES (1,'ο αρχοντας των δαχτυλιδιών','23','some details','target');

UNLOCK TABLES;
-- -----------------------------------------------------
-- Table `ModelDB`.`Transactions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Transactions` (
  `Transaction_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NULL,
  `Item_id` INT(11) NULL,
  `Buy_Sell` INT NULL,
  PRIMARY KEY (`Transaction_id`),
  INDEX `fk_user_has_Items_Items1_idx` (`Item_id` ASC),
  INDEX `fk_user_has_Items_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_has_Items_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ModelDB`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_Items_Items1`
    FOREIGN KEY (`Item_id`)
    REFERENCES `ModelDB`.`Items` (`ItemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;

LOCK TABLES `Transactions` WRITE;

INSERT INTO `Transactions` VALUES (1, 1, 1, 0);

UNLOCK TABLES;
-- -----------------------------------------------------
-- Table `ModelDB`.`Categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Categories` (
  `Index` INT(11) NOT NULL AUTO_INCREMENT,
  `Item_id` INT(11) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Index`),
  INDEX `fk_Categories_Items1_idx` (`Item_id` ASC),
  CONSTRAINT `fk_Categories_Items1`
    FOREIGN KEY (`Item_id`)
    REFERENCES `ModelDB`.`Items` (`ItemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

LOCK TABLES `Categories` WRITE;

INSERT INTO `Categories` VALUES (1, 1, 'βιβλια');

UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
