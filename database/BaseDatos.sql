-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hotel
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `habitacion`
--

DROP TABLE IF EXISTS `habitacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitacion` (
  `idHabitacion` int(11) NOT NULL AUTO_INCREMENT,
  `numeroHabitacion` int(11) NOT NULL,
  PRIMARY KEY (`idHabitacion`),
  UNIQUE KEY `numeroHabitacion` (`numeroHabitacion`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitacion`
--

LOCK TABLES `habitacion` WRITE;
/*!40000 ALTER TABLE `habitacion` DISABLE KEYS */;
INSERT INTO `habitacion` VALUES (1,101),(2,102),(3,103),(4,104),(5,105),(6,106),(7,107),(8,108),(9,109),(10,110),(11,201),(12,202),(13,203),(14,204),(15,205),(16,206),(17,207),(18,208),(19,209),(20,210),(21,301),(22,302),(23,303),(24,304),(25,305);
/*!40000 ALTER TABLE `habitacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hamaca`
--

DROP TABLE IF EXISTS `hamaca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hamaca` (
  `idHamaca` int(11) NOT NULL AUTO_INCREMENT,
  `numeroHamaca` int(11) NOT NULL,
  `estado` enum('libre','reservada') DEFAULT 'libre',
  `idHabitacion` int(11) DEFAULT NULL,
  PRIMARY KEY (`idHamaca`),
  UNIQUE KEY `numeroHamaca` (`numeroHamaca`),
  KEY `idHabitacion` (`idHabitacion`),
  CONSTRAINT `hamaca_ibfk_1` FOREIGN KEY (`idHabitacion`) REFERENCES `habitacion` (`idHabitacion`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hamaca`
--

LOCK TABLES `hamaca` WRITE;
/*!40000 ALTER TABLE `hamaca` DISABLE KEYS */;
INSERT INTO `hamaca` VALUES (1,1,'libre',1),(2,2,'libre',NULL),(3,3,'libre',NULL),(4,4,'libre',NULL),(5,5,'libre',NULL),(6,6,'libre',NULL),(7,7,'libre',NULL),(8,8,'libre',NULL),(9,9,'libre',NULL),(10,10,'libre',NULL),(11,11,'libre',NULL),(12,12,'libre',NULL),(13,13,'libre',NULL),(14,14,'libre',NULL),(15,15,'libre',NULL),(16,16,'libre',NULL),(17,17,'libre',NULL),(18,18,'libre',NULL),(19,19,'libre',NULL),(20,20,'libre',NULL),(21,21,'libre',NULL),(22,22,'libre',NULL),(23,23,'libre',NULL),(24,24,'libre',NULL),(25,25,'libre',NULL);
/*!40000 ALTER TABLE `hamaca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `idReserva` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idHabitacion` int(11) DEFAULT NULL,
  `fechaInicio` date DEFAULT NULL,
  `fechaFin` date DEFAULT NULL,
  `idHamaca` int(11) DEFAULT NULL,
  PRIMARY KEY (`idReserva`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idHabitacion` (`idHabitacion`),
  CONSTRAINT `reserva_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`),
  CONSTRAINT `reserva_ibfk_2` FOREIGN KEY (`idHabitacion`) REFERENCES `habitacion` (`idHabitacion`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `esAdministrador` tinyint(1) DEFAULT 0,
  `role` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin','adminpassword',1,'ADMIN'),(2,'','',1,'ADMIN'),(4,'Jose','Jose',0,'USER'),(5,'Juan','Juan',1,'ADMIN'),(6,'ben','ben',1,'ADMIN');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-04 22:50:44
