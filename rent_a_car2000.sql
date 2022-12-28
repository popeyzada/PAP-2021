-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: rent-a-car_2000
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.13-MariaDB

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
-- Table structure for table `acidentes`
--

DROP TABLE IF EXISTS `acidentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acidentes` (
  `acidenteID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `viaturas_viaturaID` int(10) unsigned NOT NULL,
  `categoria` varchar(40) DEFAULT NULL,
  `descricao` varchar(240) DEFAULT NULL,
  `data` date DEFAULT NULL,
  PRIMARY KEY (`acidenteID`),
  UNIQUE KEY `acidenteID_UNIQUE` (`acidenteID`),
  KEY `fk_acidentes_viaturas1_idx` (`viaturas_viaturaID`),
  CONSTRAINT `fk_acidentes_viaturas1` FOREIGN KEY (`viaturas_viaturaID`) REFERENCES `viaturas` (`viaturaID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acidentes`
--

LOCK TABLES `acidentes` WRITE;
/*!40000 ALTER TABLE `acidentes` DISABLE KEYS */;
INSERT INTO `acidentes` VALUES (21,10,'Grave','O cliente de ID 67 bateu com a viatura num poste quando estava dando recúo na rua XYZ','2021-03-01'),(22,11,'Leve','Um turista colidiu na viatura quando estacionada. \nA viatura não estava reservada no momento','2021-03-06'),(23,10,'Outro (a)','Viatura precisa de mudar os discos dos travões ','2021-03-02'),(24,11,'Muito grave','O cliente João Baraco deu PT na viatura','2021-02-04');
/*!40000 ALTER TABLE `acidentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `clienteID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contatos_contatoID` int(10) unsigned NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  `genero` char(1) DEFAULT NULL,
  `cidade` varchar(30) NOT NULL,
  `morada` varchar(50) NOT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `pais` varchar(30) NOT NULL,
  `carta_conducao` varchar(15) NOT NULL,
  `validade_carta_conducao` varchar(10) NOT NULL,
  `ID_passaporte` varchar(25) NOT NULL,
  `contribuinte` int(9) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `empresa` char(1) DEFAULT NULL,
  PRIMARY KEY (`clienteID`,`contatos_contatoID`,`ID_passaporte`,`validade_carta_conducao`,`carta_conducao`,`contribuinte`),
  UNIQUE KEY `clienteID_UNIQUE` (`clienteID`),
  KEY `fk_clientes_contatos_idx` (`contatos_contatoID`),
  CONSTRAINT `fk_clientes_contatos` FOREIGN KEY (`contatos_contatoID`) REFERENCES `contatos` (`contatoID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (67,2,'2001-12-11','M','Lagoa','Rua da lagoa','913-213','Portugal','2133419 A','2023-01-01','',214514213,'cliente1@email.pt','N');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contatos`
--

DROP TABLE IF EXISTS `contatos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contatos` (
  `contatoID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(75) NOT NULL,
  `codigo_discagem` int(3) NOT NULL,
  `contato` int(9) NOT NULL,
  PRIMARY KEY (`contatoID`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contatos`
--

LOCK TABLES `contatos` WRITE;
/*!40000 ALTER TABLE `contatos` DISABLE KEYS */;
INSERT INTO `contatos` VALUES (1,'João Camelo',351,2147483647),(2,'Cliente 1',351,2147483647);
/*!40000 ALTER TABLE `contatos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extras`
--

DROP TABLE IF EXISTS `extras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extras` (
  `extrasID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `preco` double unsigned DEFAULT NULL,
  PRIMARY KEY (`extrasID`,`nome`),
  UNIQUE KEY `extrasID_UNIQUE` (`extrasID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extras`
--

LOCK TABLES `extras` WRITE;
/*!40000 ALTER TABLE `extras` DISABLE KEYS */;
INSERT INTO `extras` VALUES (1,'Segundo condutor',5),(2,'Assento',4),(3,'Cadeira Bebé',2),(4,'Ovinho',4);
/*!40000 ALTER TABLE `extras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionarios`
--

DROP TABLE IF EXISTS `funcionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionarios` (
  `funcionarioID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contatos_contatoID` int(10) unsigned NOT NULL,
  `data_nascimento` char(10) NOT NULL,
  `cidade` varchar(25) NOT NULL,
  `morada` varchar(75) NOT NULL,
  `codigo_postal` char(8) DEFAULT NULL,
  `carta_conducao` varchar(3) DEFAULT 'Não',
  `validade_carta_conducao` date DEFAULT NULL,
  `cartao_cidadao` int(8) unsigned NOT NULL,
  `contribuinte` int(9) unsigned NOT NULL,
  `email` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`funcionarioID`,`contatos_contatoID`,`cartao_cidadao`,`contribuinte`),
  KEY `fk_funcionarios_contatos1_idx` (`contatos_contatoID`),
  CONSTRAINT `fk_funcionarios_contatos1` FOREIGN KEY (`contatos_contatoID`) REFERENCES `contatos` (`contatoID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionarios`
--

LOCK TABLES `funcionarios` WRITE;
/*!40000 ALTER TABLE `funcionarios` DISABLE KEYS */;
INSERT INTO `funcionarios` VALUES (1,1,'1996-12-12','Vila Franca do Campo','Rua do camelo','9602-321','Sim','2022-10-13',931245783,4294967295,'joaocamelo@gmail.com');
/*!40000 ALTER TABLE `funcionarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lista_negra`
--

DROP TABLE IF EXISTS `lista_negra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lista_negra` (
  `lista_negraID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `clienteID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`lista_negraID`,`clienteID`),
  UNIQUE KEY `lista_negraID_UNIQUE` (`lista_negraID`),
  KEY `fk_lista_negra_clientes_idx` (`clienteID`),
  CONSTRAINT `fk_lista_negra_clientes` FOREIGN KEY (`clienteID`) REFERENCES `clientes` (`clienteID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lista_negra`
--

LOCK TABLES `lista_negra` WRITE;
/*!40000 ALTER TABLE `lista_negra` DISABLE KEYS */;
/*!40000 ALTER TABLE `lista_negra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_funcionarios`
--

DROP TABLE IF EXISTS `login_funcionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_funcionarios` (
  `loginID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `usuario` varchar(45) NOT NULL,
  `password` varchar(300) NOT NULL,
  `is_admin` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `funcionarioID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`loginID`,`usuario`,`funcionarioID`),
  KEY `fk_login_funcionarios_funcionarios1_idx` (`funcionarioID`),
  CONSTRAINT `fk_login_funcionarios_funcionarios1` FOREIGN KEY (`funcionarioID`) REFERENCES `funcionarios` (`funcionarioID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_funcionarios`
--

LOCK TABLES `login_funcionarios` WRITE;
/*!40000 ALTER TABLE `login_funcionarios` DISABLE KEYS */;
INSERT INTO `login_funcionarios` VALUES (3,'admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',1,1);
/*!40000 ALTER TABLE `login_funcionarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logs` (
  `logID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descricao` varchar(120) DEFAULT NULL,
  `data` char(10) DEFAULT NULL,
  PRIMARY KEY (`logID`),
  UNIQUE KEY `logID_UNIQUE` (`logID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pontos_entrega`
--

DROP TABLE IF EXISTS `pontos_entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pontos_entrega` (
  `pontoID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(80) NOT NULL,
  PRIMARY KEY (`pontoID`,`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pontos_entrega`
--

LOCK TABLES `pontos_entrega` WRITE;
/*!40000 ALTER TABLE `pontos_entrega` DISABLE KEYS */;
INSERT INTO `pontos_entrega` VALUES (1,'Aeroporto Jõao Paulo II - P6'),(2,'AzoresPark - Armazém 3.27');
/*!40000 ALTER TABLE `pontos_entrega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `precario`
--

DROP TABLE IF EXISTS `precario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `precario` (
  `precoID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `categoria` varchar(20) NOT NULL,
  `valor` double DEFAULT NULL,
  PRIMARY KEY (`precoID`,`categoria`),
  UNIQUE KEY `categoria_UNIQUE` (`categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `precario`
--

LOCK TABLES `precario` WRITE;
/*!40000 ALTER TABLE `precario` DISABLE KEYS */;
INSERT INTO `precario` VALUES (1,'A',12.44),(2,'B',14.99);
/*!40000 ALTER TABLE `precario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `reservaID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `situacao` varchar(20) NOT NULL,
  `clientes_clienteID` int(10) unsigned NOT NULL,
  `viaturas_viaturaID` int(10) unsigned NOT NULL,
  `seguro_seguroID` int(10) unsigned NOT NULL,
  `pontos_entregaID` int(10) unsigned NOT NULL,
  `pontos_devolucaoID` int(10) unsigned NOT NULL,
  `data_entrega` datetime NOT NULL,
  `data_devolucao` datetime NOT NULL,
  `taxa_aeroporto` double unsigned DEFAULT NULL,
  `IVA` double unsigned DEFAULT NULL,
  `misc_taxas` double unsigned DEFAULT NULL,
  `desconto` double unsigned DEFAULT NULL,
  `valor_extras` double DEFAULT NULL,
  `valor_diario` double unsigned NOT NULL,
  `valor_total` double unsigned NOT NULL,
  `data_reserva` date NOT NULL,
  PRIMARY KEY (`reservaID`),
  UNIQUE KEY `reservaID_UNIQUE` (`reservaID`),
  KEY `fk_reservas_viaturas1_idx` (`viaturas_viaturaID`),
  KEY `fk_reservas_seguro1_idx` (`seguro_seguroID`),
  KEY `fk_reservas_cliente1_idx` (`clientes_clienteID`),
  KEY `fk_reservas_pontos_entrega_idx` (`pontos_entregaID`),
  CONSTRAINT `fk_reservas_clientesID` FOREIGN KEY (`clientes_clienteID`) REFERENCES `clientes` (`clienteID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservas_pontos_entrega` FOREIGN KEY (`pontos_entregaID`) REFERENCES `pontos_entrega` (`pontoID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservas_seguro1` FOREIGN KEY (`seguro_seguroID`) REFERENCES `seguro` (`seguroID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservas_viaturas1` FOREIGN KEY (`viaturas_viaturaID`) REFERENCES `viaturas` (`viaturaID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (1,'Aceito',67,11,1,1,0,'2021-04-12 13:00:00','2021-07-12 09:30:00',12,15,0,0,NULL,19,46,'2021-03-10');
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas_extras`
--

DROP TABLE IF EXISTS `reservas_extras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas_extras` (
  `reservas_reservaID` int(10) unsigned NOT NULL,
  `extras_extrasID` int(10) unsigned NOT NULL,
  `quantidade` int(2) unsigned NOT NULL DEFAULT 0,
  KEY `fk_extras_has_reservas_reservas1_idx` (`reservas_reservaID`),
  KEY `fk_extras_has_reservas_extras1_idx` (`extras_extrasID`),
  CONSTRAINT `fk_extras_has_reservas_extras1` FOREIGN KEY (`extras_extrasID`) REFERENCES `extras` (`extrasID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_extras_has_reservas_reservas1` FOREIGN KEY (`reservas_reservaID`) REFERENCES `reservas` (`reservaID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas_extras`
--

LOCK TABLES `reservas_extras` WRITE;
/*!40000 ALTER TABLE `reservas_extras` DISABLE KEYS */;
INSERT INTO `reservas_extras` VALUES (1,2,1),(1,3,2),(1,1,1),(1,4,0);
/*!40000 ALTER TABLE `reservas_extras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seguro`
--

DROP TABLE IF EXISTS `seguro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seguro` (
  `seguroID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `preco_diario` double DEFAULT NULL,
  PRIMARY KEY (`seguroID`,`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seguro`
--

LOCK TABLES `seguro` WRITE;
/*!40000 ALTER TABLE `seguro` DISABLE KEYS */;
INSERT INTO `seguro` VALUES (1,'Pack Silver',10),(2,'Pack Gold',19.99);
/*!40000 ALTER TABLE `seguro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `viaturas`
--

DROP TABLE IF EXISTS `viaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `viaturas` (
  `viaturaID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `precario_grupo` varchar(20) NOT NULL,
  `marca` varchar(20) DEFAULT NULL,
  `modelo` varchar(20) DEFAULT NULL,
  `ano` int(4) unsigned DEFAULT NULL,
  `tipo_combustivel` varchar(10) DEFAULT NULL,
  `caixa_velocidades` varchar(14) DEFAULT NULL,
  `data_garantia` date DEFAULT NULL,
  `portas` int(2) unsigned DEFAULT NULL,
  `lugares` int(2) unsigned DEFAULT NULL,
  `matricula` varchar(10) NOT NULL,
  `prox_inspecao` date DEFAULT NULL,
  `tanque_combustivel` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`viaturaID`,`precario_grupo`,`matricula`),
  UNIQUE KEY `viaturaID_UNIQUE` (`viaturaID`),
  UNIQUE KEY `matricula_UNIQUE` (`matricula`),
  KEY `fk_viaturas_precario1_idx` (`precario_grupo`),
  CONSTRAINT `fk_viaturas_precario1` FOREIGN KEY (`precario_grupo`) REFERENCES `precario` (`categoria`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viaturas`
--

LOCK TABLES `viaturas` WRITE;
/*!40000 ALTER TABLE `viaturas` DISABLE KEYS */;
INSERT INTO `viaturas` VALUES (10,'A','Mercedes','GLC SUV',2021,'Gásoleo','Automática','2023-10-12',5,7,'AB UF 32','2022-10-12','CHEIO'),(11,'B','Smart','Fourfour',2014,'Gasolina','Manual','2016-10-12',5,5,'AU UF 21','2021-10-12','1/4');
/*!40000 ALTER TABLE `viaturas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-28 12:29:43
