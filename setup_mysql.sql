/*Create user and grand him priveleges*/
GRANT ALL PRIVILEGES ON organizer.* TO 'tomcat'@'localhost' IDENTIFIED by 'tomcat..';
--
-- Table structure for table `organizer_event`
--

/*Create database*/
DROP DATABASE IF EXISTS organizer;
CREATE DATABASE organizer;

USE organizer;

DROP TABLE IF EXISTS `organizer_event`;
CREATE TABLE `organizer_event` (
  `type` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `start` datetime NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1633 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `organizer_event`
--

LOCK TABLES `organizer_event` WRITE;
-- Insert events for the demo user (id=1)
INSERT INTO `organizer_event` VALUES ('DEFAULT',17,NULL,'2014-07-14 13:30:00','Doctor appointment','2014-07-14 09:30:00',1),('DEFAULT',26,NULL,'2014-07-14 15:00:00','Monday\'s gym','2014-07-14 14:00:00',1),('DEFAULT',27,NULL,'2014-07-16 04:30:00','Morning jogging','2014-07-16 04:00:00',1),('DEFAULT',28,NULL,'2014-07-16 16:00:00','Meeting of the flat owners','2014-07-16 15:00:00',1),('DEFAULT',29,NULL,'2014-07-17 10:00:00','Lunch with a friend','2014-07-17 09:00:00',1),('DEFAULT',30,NULL,'2014-07-19 07:00:00','Morning jogging','2014-07-19 06:00:00',1),('DEFAULT',31,NULL,'2014-07-20 10:00:00','Bicycle trip','2014-07-20 06:00:00',1),('DEFAULT',32,NULL,NULL,'Call the car service','2014-07-15 08:00:00',1),('DEFAULT',33,NULL,'2014-07-21 08:30:00','Dentist','2014-07-21 08:00:00',1),('DEFAULT',34,NULL,'2014-07-21 15:00:00','Monday\'s gym','2014-07-21 14:00:00',1),('DEFAULT',35,NULL,'2014-07-18 15:00:00','Friday\'s gym','2014-07-18 14:00:00',1),('DEFAULT',51,NULL,'2014-07-23 04:30:00','Morning jogging','2014-07-23 04:00:00',1),('DEFAULT',52,NULL,'2014-07-26 07:00:00','Morning jogging','2014-07-26 06:00:00',1),('DEFAULT',53,NULL,'2014-07-27 09:00:00','Squash match with a friend','2014-07-27 07:30:00',1),('DEFAULT',54,NULL,'2014-07-22 09:30:00','Lunch with a friend','2014-07-22 08:30:00',1),('DEFAULT',55,NULL,'2014-07-22 18:00:00','Preparation for the next day\'s presentation','2014-07-22 16:00:00',1),('DEFAULT',56,NULL,NULL,'Call mom','2014-07-23 16:00:00',1),('DEFAULT',57,NULL,NULL,'Blood taking at GP','2014-07-24 04:30:00',1),('DEFAULT',58,NULL,NULL,'Phone the GP for blood results','2014-07-25 08:00:00',1),('DEFAULT',60,NULL,'2014-07-25 09:00:00','Go buy a birthday present ','2014-07-25 08:00:00',1),('DEFAULT',63,NULL,'2014-07-25 15:00:00','Friday\'s gym','2014-07-25 14:00:00',1),('DEFAULT',66,NULL,NULL,'Friend\'s birthday party','2014-07-25 16:00:00',1),('DEFAULT',71,NULL,NULL,'Shopping trip','2014-07-18 15:30:00',1),('DEFAULT',727,NULL,'2014-07-28 15:00:00','Monday\'s gym','2014-07-28 14:00:00',1),('DEFAULT',728,NULL,'2014-07-30 04:30:00','Morning jogging','2014-07-30 04:00:00',1),('DEFAULT',730,NULL,'2014-08-02 07:00:00','Morning jogging','2014-08-02 06:00:00',1),('DEFAULT',731,NULL,NULL,'Meeting with the former schoolmates','2014-07-30 15:00:00',1),('DEFAULT',732,NULL,'2014-08-03 15:30:00','Trip to Viena','2014-08-03 05:00:00',1),('DEFAULT',733,NULL,NULL,'Rock concert','2014-07-31 17:00:00',1),('DEFAULT',734,NULL,'2014-07-31 10:00:00','Buy tickets for the concert','2014-07-31 09:00:00',1),('DEFAULT',735,NULL,NULL,'Take bike to the bike repair shop','2014-08-01 14:00:00',1),('DEFAULT',736,NULL,'2014-07-30 10:00:00','Lunch with a friend','2014-07-30 09:00:00', 1);
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kiqfjabx9puw3p1eg7kily8kg` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1', 'demo_user','demo_password');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

