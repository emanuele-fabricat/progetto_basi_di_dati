-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: casa_dei_giochi
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `accessori`
--

DROP TABLE IF EXISTS `accessori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessori` (
  `tematica` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessori`
--

LOCK TABLES `accessori` WRITE;
/*!40000 ALTER TABLE `accessori` DISABLE KEYS */;
INSERT INTO `accessori` VALUES ('giochi di ruolo','4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f'),('funko pop','5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e');
/*!40000 ALTER TABLE `accessori` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id_admin` char(36) NOT NULL,
  `mail` varchar(254) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`mail`),
  UNIQUE KEY `id_admin` (`id_admin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('3d23103f-a12b-4fb8-ad0c-aa7a771062b2','filippa.argenti@casa.dei.giochi.it','filippa'),('088a312c-1d05-44b6-ac28-f3d91988672e','gicomo.datteri@casa.dei.giochi.it','giacomo'),('9b1deb4d-3b7d-4f2a-a2f7-3d6f0f2f9b1a','mario.rossi@casa.dei.giochi.it','mario');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_crea`
--

DROP TABLE IF EXISTS `admin_crea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_crea` (
  `id_evento` char(36) NOT NULL,
  `mail` varchar(254) NOT NULL,
  PRIMARY KEY (`id_evento`,`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_crea`
--

LOCK TABLES `admin_crea` WRITE;
/*!40000 ALTER TABLE `admin_crea` DISABLE KEYS */;
INSERT INTO `admin_crea` VALUES ('cd5c407b-8b52-4627-856f-1994610a3462','filippa.argenti@casa.dei.giochi.it');
/*!40000 ALTER TABLE `admin_crea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_partecipa`
--

DROP TABLE IF EXISTS `admin_partecipa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_partecipa` (
  `id_evento` char(36) NOT NULL,
  `mail` varchar(254) NOT NULL,
  PRIMARY KEY (`id_evento`,`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_partecipa`
--

LOCK TABLES `admin_partecipa` WRITE;
/*!40000 ALTER TABLE `admin_partecipa` DISABLE KEYS */;
INSERT INTO `admin_partecipa` VALUES ('cd5c407b-8b52-4627-856f-1994610a3462','filippa.argenti@casa.dei.giochi.it');
/*!40000 ALTER TABLE `admin_partecipa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggiungere`
--

DROP TABLE IF EXISTS `aggiungere`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aggiungere` (
  `id_articolo` char(36) NOT NULL,
  `id_carrello` char(36) NOT NULL,
  `carico` int NOT NULL,
  PRIMARY KEY (`id_articolo`,`id_carrello`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggiungere`
--

LOCK TABLES `aggiungere` WRITE;
/*!40000 ALTER TABLE `aggiungere` DISABLE KEYS */;
INSERT INTO `aggiungere` VALUES ('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','1e69777e-26f4-4239-a774-3171ab39f1c0',1),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','1e69777e-26f4-4239-a774-3171ab39f1c0',10),('3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a','1e69777e-26f4-4239-a774-3171ab39f1c0',1),('3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a','1e69777e-26f4-4239-a774-3171ab39f1c0',1),('4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f','1e69777e-26f4-4239-a774-3171ab39f1c0',1);
/*!40000 ALTER TABLE `aggiungere` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articolo`
--

DROP TABLE IF EXISTS `articolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articolo` (
  `id_articolo` char(36) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `descrizione` text NOT NULL,
  `prezzo_vendita` int NOT NULL,
  `disponibilità` int NOT NULL,
  `tipologia` enum('CARTE_COLLEZIONABILI','GIOCHI_DI_RUOLO','GIOCHI_IN_SCATOLA','LIBRI_E_FUMETTI','MODELLISMO','GADGET_E_GIOCATTOLI','ACCESSORI') DEFAULT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articolo`
--

LOCK TABLES `articolo` WRITE;
/*!40000 ALTER TABLE `articolo` DISABLE KEYS */;
INSERT INTO `articolo` VALUES ('1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e','set driadi','il potere della natura',25,110,'MODELLISMO'),('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','one piece 1','volume 1 di one piece',11,210,'LIBRI_E_FUMETTI'),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','pacchetto lorcana','gioco di carte a tema disney',15,210,'CARTE_COLLEZIONABILI'),('3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a','monopoly','forse dopo avrete ancora amici',35,110,'GIOCHI_IN_SCATOLA'),('3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a','naruto 35','volume 35 di naruto',11,210,'ACCESSORI'),('4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f','set dadi bianchi','bianchi come scheletri',15,110,'ACCESSORI'),('4e2b6d1f-8c3a-9f7e-5b8d-1a3c2e4f9c7a','set guerrieri','pronti a combattere',25,110,'MODELLISMO'),('5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e','set dadi rossi','rossi come il fuoco, qualcuno ha detto palla di fuoco?',15,110,'ACCESSORI'),('6d1f2b4e-9f7e-8c3a-1a9f-3c7e5b8d4e2b','dragon bane','impugna un\'arma e buttati in un gioco di spasso e sconquasso',55,110,'GIOCHI_DI_RUOLO'),('7b5d3f2e-4c8a-2d4b-9e1a-8f6c1a9f3c7e','sdentato','pupazzo di sdentato',30,110,'GADGET_E_GIOCATTOLI'),('7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d','7th sea','tutti all\'avventura in una europa fansy del rinascimento',55,120,'GIOCHI_DI_RUOLO'),('8c3a9e7f-2c4b-1a9f-3c7e-5b8d4e2b6d1f','naruto','funko pop di naruto',30,110,'GADGET_E_GIOCATTOLI'),('9e1a7b5d-3f2e-4c8a-2d4b-8f6c1a9f3c7e','risiko','dopo non avrete più amici',35,110,'GIOCHI_IN_SCATOLA'),('9f3a1c2e-4b7d-4e8f-9a6c-2d1f3e7b9c4a','pacchetto magic','il gioco di carte più famoso al mondo',15,110,'CARTE_COLLEZIONABILI');
/*!40000 ALTER TABLE `articolo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrello`
--

DROP TABLE IF EXISTS `carrello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrello` (
  `id_carrello` char(36) NOT NULL,
  `data` date NOT NULL,
  `totale_carrello` int NOT NULL,
  `id_utente` char(36) NOT NULL,
  PRIMARY KEY (`id_carrello`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrello`
--

LOCK TABLES `carrello` WRITE;
/*!40000 ALTER TABLE `carrello` DISABLE KEYS */;
INSERT INTO `carrello` VALUES ('1e69777e-26f4-4239-a774-3171ab39f1c0','2025-09-05',222,'ca119548-e9c4-4e98-9428-2d18d9420c1d');
/*!40000 ALTER TABLE `carrello` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carte_collezionabili`
--

DROP TABLE IF EXISTS `carte_collezionabili`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carte_collezionabili` (
  `gioco` varchar(100) NOT NULL,
  `espansione` varchar(100) NOT NULL,
  `formato` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carte_collezionabili`
--

LOCK TABLES `carte_collezionabili` WRITE;
/*!40000 ALTER TABLE `carte_collezionabili` DISABLE KEYS */;
INSERT INTO `carte_collezionabili` VALUES ('lorcana','base','pacchetto','2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e'),('magic','base','pacchetto','9f3a1c2e-4b7d-4e8f-9a6c-2d1f3e7b9c4a');
/*!40000 ALTER TABLE `carte_collezionabili` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_utente` char(36) NOT NULL,
  `mail` varchar(254) NOT NULL,
  `password` varchar(36) NOT NULL,
  `nome` varchar(20) NOT NULL,
  `cognome` varchar(20) NOT NULL,
  `data_di_nascita` date NOT NULL,
  `indirizzo_1` varchar(100) NOT NULL,
  `indirizzo_2` varchar(100) DEFAULT NULL,
  `indirizzo_3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('ca119548-e9c4-4e98-9428-2d18d9420c1d','mauro.repetto@gmail.com','mauro','mauro','repetto','1999-05-06','via verdi 6','',''),('db2bc7b5-859f-4f6e-ae71-e58805886acd','daniele.fabbri@gmail.com','daniele','daniele','fabbri','1998-12-04','via colombo 72','',''),('f47ac10b-58cc-4372-a567-0e02b2c3d479','giacomo.giacomini@hotmail.com','giacomo','giacomo','giacomini','1998-12-09','via garibaldi 5',NULL,NULL);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_crea`
--

DROP TABLE IF EXISTS `cliente_crea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_crea` (
  `id_evento` char(36) NOT NULL,
  `id_utente` char(36) NOT NULL,
  PRIMARY KEY (`id_evento`,`id_utente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_crea`
--

LOCK TABLES `cliente_crea` WRITE;
/*!40000 ALTER TABLE `cliente_crea` DISABLE KEYS */;
INSERT INTO `cliente_crea` VALUES ('43978d9b-8f45-4ad7-b332-710ccc392874','ca119548-e9c4-4e98-9428-2d18d9420c1d');
/*!40000 ALTER TABLE `cliente_crea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_partecipa`
--

DROP TABLE IF EXISTS `cliente_partecipa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_partecipa` (
  `id_utente` char(36) NOT NULL,
  `id_evento` char(36) NOT NULL,
  PRIMARY KEY (`id_utente`,`id_evento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_partecipa`
--

LOCK TABLES `cliente_partecipa` WRITE;
/*!40000 ALTER TABLE `cliente_partecipa` DISABLE KEYS */;
INSERT INTO `cliente_partecipa` VALUES ('ca119548-e9c4-4e98-9428-2d18d9420c1d','cd5c407b-8b52-4627-856f-1994610a3462');
/*!40000 ALTER TABLE `cliente_partecipa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comporre`
--

DROP TABLE IF EXISTS `comporre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comporre` (
  `id_prodotto` char(36) NOT NULL,
  `id_ordine` char(36) NOT NULL,
  `qta` int NOT NULL,
  PRIMARY KEY (`id_prodotto`,`id_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comporre`
--

LOCK TABLES `comporre` WRITE;
/*!40000 ALTER TABLE `comporre` DISABLE KEYS */;
INSERT INTO `comporre` VALUES ('1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','c65e8642-a9ec-4017-acaf-45057c333b51',100),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','3c76a56d-49b0-42e9-8209-241a29cff467',100),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','a7a23f56-e318-436d-8b47-3efec6f31443',100),('3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a','8db94b2d-c970-49a8-af50-6dff00f6a22f',200),('4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f','8a6a99d0-396f-4526-8f36-d9158cfe94a2',100),('4e2b6d1f-8c3a-9f7e-5b8d-1a3c2e4f9c7a','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('4e2b6d1f-8c3a-9f7e-5b8d-1a3c2e4f9c7a','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('6d1f2b4e-9f7e-8c3a-1a9f-3c7e5b8d4e2b','75354367-60cb-49f2-85e0-a9b819d0188b',10),('6d1f2b4e-9f7e-8c3a-1a9f-3c7e5b8d4e2b','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('7b5d3f2e-4c8a-2d4b-9e1a-8f6c1a9f3c7e','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('7b5d3f2e-4c8a-2d4b-9e1a-8f6c1a9f3c7e','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d','75354367-60cb-49f2-85e0-a9b819d0188b',10),('7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('8c3a9e7f-2c4b-1a9f-3c7e-5b8d4e2b6d1f','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('8c3a9e7f-2c4b-1a9f-3c7e-5b8d4e2b6d1f','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('9e1a7b5d-3f2e-4c8a-2d4b-8f6c1a9f3c7e','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('9e1a7b5d-3f2e-4c8a-2d4b-8f6c1a9f3c7e','8db94b2d-c970-49a8-af50-6dff00f6a22f',100),('9f3a1c2e-4b7d-4e8f-9a6c-2d1f3e7b9c4a','11e7dfd2-c9c2-4416-8b06-0c870dd0f539',10),('9f3a1c2e-4b7d-4e8f-9a6c-2d1f3e7b9c4a','8db94b2d-c970-49a8-af50-6dff00f6a22f',100);
/*!40000 ALTER TABLE `comporre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento` (
  `id_evento` char(36) NOT NULL,
  `num_partecipanti` int NOT NULL,
  `data_ora_inizio` datetime NOT NULL,
  `data_ora_fine` datetime NOT NULL,
  `visibilità` enum('pubblico','privato') NOT NULL,
  `nome` varchar(30) DEFAULT NULL,
  `presentazione` text,
  `max_partecipanti` int DEFAULT NULL,
  PRIMARY KEY (`id_evento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evento`
--

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
INSERT INTO `evento` VALUES ('43978d9b-8f45-4ad7-b332-710ccc392874',4,'2025-10-07 15:00:00','2025-10-07 20:00:00','privato',NULL,NULL,NULL),('cd5c407b-8b52-4627-856f-1994610a3462',2,'2025-10-07 15:00:00','2025-10-07 20:00:00','pubblico','Multi tavolo 7th sea','Super iper figata in giro con una flotta ad affrontare i mostri marini più pericolosi',20);
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornitore`
--

DROP TABLE IF EXISTS `fornitore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornitore` (
  `p_iva` char(11) NOT NULL,
  `dominio` varchar(254) NOT NULL,
  `nominativo` varchar(100) NOT NULL,
  PRIMARY KEY (`p_iva`),
  UNIQUE KEY `dominio` (`dominio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornitore`
--

LOCK TABLES `fornitore` WRITE;
/*!40000 ALTER TABLE `fornitore` DISABLE KEYS */;
INSERT INTO `fornitore` VALUES ('12345670123','panini@info.it','panini'),('23456780987','chessex@info.it','chessex'),('32109870456','planet.manga@info.it','planet manga'),('45678910111','hasbro@info.it','hasbro'),('65432190876','citadel@info.it','citadel'),('78901230543','funko@info.it','funko'),('98765430219','need.games@info.it','need games');
/*!40000 ALTER TABLE `fornitore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gadget_e_giocattoli`
--

DROP TABLE IF EXISTS `gadget_e_giocattoli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gadget_e_giocattoli` (
  `franchise` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gadget_e_giocattoli`
--

LOCK TABLES `gadget_e_giocattoli` WRITE;
/*!40000 ALTER TABLE `gadget_e_giocattoli` DISABLE KEYS */;
INSERT INTO `gadget_e_giocattoli` VALUES ('funko pop','7b5d3f2e-4c8a-2d4b-9e1a-8f6c1a9f3c7e'),('funko pop','8c3a9e7f-2c4b-1a9f-3c7e-5b8d4e2b6d1f');
/*!40000 ALTER TABLE `gadget_e_giocattoli` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `giochi_di_ruolo`
--

DROP TABLE IF EXISTS `giochi_di_ruolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giochi_di_ruolo` (
  `tipo` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giochi_di_ruolo`
--

LOCK TABLES `giochi_di_ruolo` WRITE;
/*!40000 ALTER TABLE `giochi_di_ruolo` DISABLE KEYS */;
INSERT INTO `giochi_di_ruolo` VALUES ('base','6d1f2b4e-9f7e-8c3a-1a9f-3c7e5b8d4e2b'),('manuale base','7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d');
/*!40000 ALTER TABLE `giochi_di_ruolo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `giochi_in_scatola`
--

DROP TABLE IF EXISTS `giochi_in_scatola`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giochi_in_scatola` (
  `linea` varchar(100) NOT NULL,
  `meccanica` varchar(100) NOT NULL,
  `min_giocatori` int NOT NULL,
  `max_giocatori` int NOT NULL,
  `tempo_medio` int NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giochi_in_scatola`
--

LOCK TABLES `giochi_in_scatola` WRITE;
/*!40000 ALTER TABLE `giochi_in_scatola` DISABLE KEYS */;
INSERT INTO `giochi_in_scatola` VALUES ('hasbro','dadi e soldi',2,6,120,'3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a'),('hasbro','dadi e miniature',2,6,120,'9e1a7b5d-3f2e-4c8a-2d4b-8f6c1a9f3c7e');
/*!40000 ALTER TABLE `giochi_in_scatola` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libri_e_fumetti`
--

DROP TABLE IF EXISTS `libri_e_fumetti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libri_e_fumetti` (
  `serie` varchar(100) NOT NULL,
  `categoria` varchar(100) NOT NULL,
  `autore` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libri_e_fumetti`
--

LOCK TABLES `libri_e_fumetti` WRITE;
/*!40000 ALTER TABLE `libri_e_fumetti` DISABLE KEYS */;
INSERT INTO `libri_e_fumetti` VALUES ('blu','manga','echiro oda','1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b'),('base','manga','mangaka','3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a');
/*!40000 ALTER TABLE `libri_e_fumetti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modellismo`
--

DROP TABLE IF EXISTS `modellismo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modellismo` (
  `marca` varchar(100) NOT NULL,
  `id_articolo` char(36) NOT NULL,
  PRIMARY KEY (`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modellismo`
--

LOCK TABLES `modellismo` WRITE;
/*!40000 ALTER TABLE `modellismo` DISABLE KEYS */;
INSERT INTO `modellismo` VALUES ('citadel','1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e'),('citadel','4e2b6d1f-8c3a-9f7e-5b8d-1a3c2e4f9c7a');
/*!40000 ALTER TABLE `modellismo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `id_ordine` char(36) NOT NULL,
  `somma` int NOT NULL,
  `data` date NOT NULL,
  `mail` varchar(254) NOT NULL,
  PRIMARY KEY (`id_ordine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine` VALUES ('11e7dfd2-c9c2-4416-8b06-0c870dd0f539',2520,'2025-09-06','filippa.argenti@casa.dei.giochi.it'),('3c76a56d-49b0-42e9-8209-241a29cff467',1000,'2025-09-06','filippa.argenti@casa.dei.giochi.it'),('75354367-60cb-49f2-85e0-a9b819d0188b',1000,'2025-09-06','filippa.argenti@casa.dei.giochi.it'),('8a6a99d0-396f-4526-8f36-d9158cfe94a2',1000,'2025-09-06','filippa.argenti@casa.dei.giochi.it'),('8db94b2d-c970-49a8-af50-6dff00f6a22f',28800,'2025-09-06','filippa.argenti@casa.dei.giochi.it');
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto`
--

DROP TABLE IF EXISTS `prodotto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotto` (
  `id_prodotto` char(36) NOT NULL,
  `nome_prodotto` varchar(100) NOT NULL,
  `costo` int NOT NULL,
  `p_iva` char(11) NOT NULL,
  PRIMARY KEY (`id_prodotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES ('1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e','set driadi',20,'65432190876'),('1a9f3c7e-5b8d-4e2b-6d1f-8c3a9e7f2c4b','one piece 1',6,'32109870456'),('2d4b8f6c-9e1a-7b5d-3f2e-4c8a1a9f3c7e','pacchetto lorcana',10,'12345670123'),('3c7e9a1f-2d4b-8f6c-9e1a-7b5d3f2e4c8a','monopoly',30,'45678910111'),('3f2e4c8a-1a9f-5b8d-6d1f-2b4e9f7e8c3a','naruto 35',6,'32109870456'),('4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f','set dadi bianchi',10,'23456780987'),('4e2b6d1f-8c3a-9f7e-5b8d-1a3c2e4f9c7a','set guerrieri',20,'65432190876'),('5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e','set dadi rossi',10,'23456780987'),('6d1f2b4e-9f7e-8c3a-1a9f-3c7e5b8d4e2b','dragon bane',50,'98765430219'),('7b5d3f2e-4c8a-2d4b-9e1a-8f6c1a9f3c7e','sdentato',25,'78901230543'),('7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d','7th sea',50,'98765430219'),('8c3a9e7f-2c4b-1a9f-3c7e-5b8d4e2b6d1f','naruto',25,'78901230543'),('9e1a7b5d-3f2e-4c8a-2d4b-8f6c1a9f3c7e','risiko',30,'45678910111'),('9f3a1c2e-4b7d-4e8f-9a6c-2d1f3e7b9c4a','pacchetto magic',10,'12345670123');
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servire`
--

DROP TABLE IF EXISTS `servire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servire` (
  `id_evento` char(36) NOT NULL,
  `numero` int NOT NULL,
  `id_articolo` char(36) NOT NULL,
  `quantità` int NOT NULL,
  PRIMARY KEY (`id_evento`,`numero`,`id_articolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servire`
--

LOCK TABLES `servire` WRITE;
/*!40000 ALTER TABLE `servire` DISABLE KEYS */;
INSERT INTO `servire` VALUES ('43978d9b-8f45-4ad7-b332-710ccc392874',4,'1a3c2e4f-9c7a-5b8d-8c3a-6d1f2b4e9f7e',1),('cd5c407b-8b52-4627-856f-1994610a3462',1,'7e2b4d1f-8c3a-4f9e-b6d2-1a9f3c7e5b8d',3),('cd5c407b-8b52-4627-856f-1994610a3462',3,'4c8a2d4b-9e1a-7b5d-3f2e-1a9f8c3a6d1f',1),('cd5c407b-8b52-4627-856f-1994610a3462',3,'5b8d1a3c-2e4f-9c7a-8c3a-6d1f2b4e9f7e',1);
/*!40000 ALTER TABLE `servire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tavolo`
--

DROP TABLE IF EXISTS `tavolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tavolo` (
  `numero` int NOT NULL AUTO_INCREMENT,
  `capienza` int NOT NULL,
  PRIMARY KEY (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tavolo`
--

LOCK TABLES `tavolo` WRITE;
/*!40000 ALTER TABLE `tavolo` DISABLE KEYS */;
INSERT INTO `tavolo` VALUES (1,5),(2,10),(3,8),(4,9);
/*!40000 ALTER TABLE `tavolo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilizzare`
--

DROP TABLE IF EXISTS `utilizzare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilizzare` (
  `id_evento` char(36) NOT NULL,
  `numero` int NOT NULL,
  `responsabile` varchar(20) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  PRIMARY KEY (`id_evento`,`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilizzare`
--

LOCK TABLES `utilizzare` WRITE;
/*!40000 ALTER TABLE `utilizzare` DISABLE KEYS */;
INSERT INTO `utilizzare` VALUES ('43978d9b-8f45-4ad7-b332-710ccc392874',4,'marco','5493785119'),('cd5c407b-8b52-4627-856f-1994610a3462',1,'giacomo','5486294538'),('cd5c407b-8b52-4627-856f-1994610a3462',3,'giada','5694281574');
/*!40000 ALTER TABLE `utilizzare` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-07  4:24:53
