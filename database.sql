-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.37 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ticket_reservation
CREATE DATABASE IF NOT EXISTS `ticket_reservation` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ticket_reservation`;

-- Dumping structure for table ticket_reservation.events
CREATE TABLE IF NOT EXISTS `events` (
    `event_id` int NOT NULL AUTO_INCREMENT,
    `active` bit(1) DEFAULT NULL,
    `approved` bit(1) DEFAULT NULL,
    `cancel_policy` bit(1) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `event_date` date NOT NULL,
    `event_name` varchar(255) NOT NULL,
    `event_time` time NOT NULL,
    `max_tickets` int NOT NULL,
    `ticket_price` double NOT NULL,
    `category_id` int NOT NULL,
    `location_id` int NOT NULL,
    `organizer_id` int NOT NULL,
    `subcategory_id` int NOT NULL,
    PRIMARY KEY (`event_id`),
    KEY `FKjgwxobdeneisw8h3nlaa2fwax` (`category_id`),
    KEY `FK7a9tiyl3gaugxrtjc2m97awui` (`location_id`),
    KEY `FKdocju8m76a3f8o6ljh2jrn2ra` (`organizer_id`),
    KEY `FK9m8ug0vbg9uysekgndryichpi` (`subcategory_id`),
    CONSTRAINT `FK7a9tiyl3gaugxrtjc2m97awui` FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`),
    CONSTRAINT `FK9m8ug0vbg9uysekgndryichpi` FOREIGN KEY (`subcategory_id`) REFERENCES `event_subcategories` (`subcategory_id`),
    CONSTRAINT `FKdocju8m76a3f8o6ljh2jrn2ra` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `FKjgwxobdeneisw8h3nlaa2fwax` FOREIGN KEY (`category_id`) REFERENCES `event_categories` (`category_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.events: ~7 rows (approximately)
INSERT INTO `events` (`event_id`, `active`, `approved`, `cancel_policy`, `description`, `event_date`, `event_name`, `event_time`, `max_tickets`, `ticket_price`, `category_id`, `location_id`, `organizer_id`, `subcategory_id`) VALUES
  (52, b'1', b'1', b'0', 'Spektakularni koncert mega zvijezde.', '2024-10-30', 'Tea Tairovic', '20:00:00', 3000, 30, 7, 12, 7, 8),
  (55, b'1', b'1', b'1', 'Nezaboravan koncert jednog od najpoznatijih izvođača današnjice.', '2024-12-12', 'Koncert Zvicera - Ed Sheeran', '21:00:00', 3000, 70, 7, 15, 7, 8),
  (56, b'1', b'1', b'0', 'Veliki derbi sarajevskih timova.', '2025-01-27', 'Sarajevo vs. Željezničar', '14:00:00', 2000, 50, 5, 17, 7, 9),
  (57, b'1', b'1', b'0', 'Klasični balet u izvedbi renomiranog baletskog ansambla', '2024-12-22', 'Balet "Orašar"', '17:00:00', 550, 10, 8, 14, 7, 10),
  (58, b'1', b'1', b'1', 'Revijalni meč boksa u kojem učestvuju najpoznatiji bokseri iz regije.', '2024-11-19', 'Boks meč - Revijalni spektakl', '21:00:00', 1000, 20, 5, 11, 7, 12),
  (59, b'1', b'1', b'0', 'Izložba moderne umjetnosti s radovima poznatih umjetnika iz Balkana i Evrope.', '2024-11-22', 'Izložba - Moderna umjetnost', '13:00:00', 1000, 25, 6, 16, 7, 15),
  (60, b'1', b'1', b'1', 'Festival koji okuplja najpoznatije rock bendove Balkana.', '2025-02-21', 'Tuzlanski Rock Festival', '17:00:00', 2000, 50, 7, 11, 7, 8);

-- Dumping structure for table ticket_reservation.event_categories
CREATE TABLE IF NOT EXISTS `event_categories` (
    `category_id` int NOT NULL AUTO_INCREMENT,
    `category_name` varchar(255) NOT NULL,
    PRIMARY KEY (`category_id`),
    UNIQUE KEY `UK_sdpfqw4797gb4o71s95r0bjr2` (`category_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.event_categories: ~4 rows (approximately)
INSERT INTO `event_categories` (`category_id`, `category_name`) VALUES
    (6, 'Kultura'),
    (7, 'Muzika'),
    (5, 'Sport'),
    (8, 'Zabava');

-- Dumping structure for table ticket_reservation.event_sectors
CREATE TABLE IF NOT EXISTS `event_sectors` (
    `event_id` int NOT NULL,
    `sector_id` int NOT NULL,
    `event_id`,`sector_id`),
    KEY `FK8d0pi5mci19dpjh6y4oy8vkui` (`sector_id`),
    CONSTRAINT `FK8d0pi5mci19dpjh6y4oy8vkui` FOREIGN KEY (`sector_id`) REFERENCES `sectors` (`sector_id`),
    CONSTRAINT `FKqqiqv6gdjgn6p4vablovyncxi` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.event_sectors: ~14 rows (approximately)
INSERT INTO `event_sectors` (`event_id`, `sector_id`) VALUES
   (58, 16),
   (60, 16),
   (58, 17),
   (60, 17),
   (60, 18),
   (52, 19),
   (52, 20),
   (57, 23),
   (57, 25),
   (55, 26),
   (55, 27),
   (59, 31),
   (56, 32),
   (56, 34);

-- Dumping structure for table ticket_reservation.event_subcategories
CREATE TABLE IF NOT EXISTS `event_subcategories` (
    `subcategory_id` int NOT NULL AUTO_INCREMENT,
    `subcategory_name` varchar(255) NOT NULL,
    `category_id` int NOT NULL,
    PRIMARY KEY (`subcategory_id`),
    KEY `FKo27gfiu62lf6tuodm4wg7lxvs` (`category_id`),
    CONSTRAINT `FKo27gfiu62lf6tuodm4wg7lxvs` FOREIGN KEY (`category_id`) REFERENCES `event_categories` (`category_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.event_subcategories: ~8 rows (approximately)
INSERT INTO `event_subcategories` (`subcategory_id`, `subcategory_name`, `category_id`) VALUES
    (8, 'Koncert', 7),
    (9, 'Utakmica', 5),
    (10, 'Ples', 8),
    (11, 'Predstava', 6),
    (12, 'Turnir', 5),
    (13, 'Cirkus', 8),
    (14, 'Simfonija', 7),
    (15, 'Izlozba', 6);

-- Dumping structure for table ticket_reservation.locations
CREATE TABLE IF NOT EXISTS `locations` (
    `location_id` int NOT NULL AUTO_INCREMENT,
    `capacity` int NOT NULL,
    `location_name` varchar(255) NOT NULL,
    `place_id` int NOT NULL,
    PRIMARY KEY (`location_id`),
    UNIQUE KEY `UK_dkw8mrr8kuqilpr7ti75dx1b9` (`location_name`),
    KEY `FKipqiao45quty3qjhsv8o9y928` (`place_id`),
    CONSTRAINT `FKipqiao45quty3qjhsv8o9y928` FOREIGN KEY (`place_id`) REFERENCES `place` (`town_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.locations: ~8 rows (approximately)
INSERT INTO `locations` (`location_id`, `capacity`, `location_name`, `place_id`) VALUES
   (10, 350, 'BKC', 7),
   (11, 2000, 'Mejdan', 7),
   (12, 3000, 'Zetra', 8),
   (13, 250, 'Dom Kulture', 10),
   (14, 750, 'Kino sala', 9),
   (15, 6000, 'Skenderija', 8),
   (16, 2000, 'Skver', 9),
   (17, 4000, 'Stadion OFK Gradina', 10);

-- Dumping structure for table ticket_reservation.place
CREATE TABLE IF NOT EXISTS `place` (
    `town_id` int NOT NULL AUTO_INCREMENT,
    `town` varchar(255) NOT NULL,
    PRIMARY KEY (`town_id`),
    UNIQUE KEY `UK_qnlqt4r8eep2do8xbkv7pxdgg` (`town`)
    ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.place: ~4 rows (approximately)
INSERT INTO `place` (`town_id`, `town`) VALUES
    (9, 'Gracanica'),
    (8, 'Sarajevo'),
    (10, 'Srebrenik'),
    (7, 'Tuzla');

-- Dumping structure for table ticket_reservation.requests
CREATE TABLE IF NOT EXISTS `requests` (
    `request_id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(255) NOT NULL,
    `full_name` varchar(255) DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `role_name` varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    PRIMARY KEY (`request_id`),
    UNIQUE KEY `UK_73x17tq11igj02la5qw98gwq8` (`email`),
    UNIQUE KEY `UK_hujep5oawgp50i0jdvfstrsvy` (`username`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.requests: ~0 rows (approximately)

-- Dumping structure for table ticket_reservation.sectors
CREATE TABLE IF NOT EXISTS `sectors` (
   `sector_id` int NOT NULL AUTO_INCREMENT,
   `capacity` int NOT NULL,
   `sector_name` varchar(255) DEFAULT NULL,
    `location_id` int NOT NULL,
    PRIMARY KEY (`sector_id`),
    KEY `FKcg4i8e9clnxbg28iyk9ukqka3` (`location_id`),
    CONSTRAINT `FKcg4i8e9clnxbg28iyk9ukqka3` FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.sectors: ~22 rows (approximately)
INSERT INTO `sectors` (`sector_id`, `capacity`, `sector_name`, `location_id`) VALUES
    (13, 100, 'Sektor A', 10),
    (14, 50, 'Loza', 10),
    (15, 200, 'Sektor B', 10),
    (16, 500, 'Tribine B', 11),
    (17, 500, 'Tribine A', 11),
    (18, 1000, 'Parter', 11),
    (19, 1000, 'Tribine', 12),
    (20, 2000, 'Parter', 12),
    (21, 100, 'Sala A', 13),
    (22, 150, 'Sala B', 13),
    (23, 500, 'Sprat I', 14),
    (24, 200, 'Sprat II', 14),
    (25, 50, 'Terasa', 14),
    (26, 1000, 'Tribina A', 15),
    (27, 2000, 'Tribina B', 15),
    (28, 3000, 'Parter', 15),
    (29, 500, 'Tribina Istok', 16),
    (30, 500, 'Tribina Zapad', 16),
    (31, 1000, 'Parter', 16),
    (32, 1000, 'Tribina Jug', 17),
    (33, 2000, 'Parter', 17),
    (34, 1000, 'Tribina Sjever', 17);

-- Dumping structure for table ticket_reservation.tickets
CREATE TABLE IF NOT EXISTS `tickets` (
    `ticket_id` int NOT NULL AUTO_INCREMENT,
    `cancellation_policy` bit(1) DEFAULT NULL,
    `price` double NOT NULL,
    `purchase_end_date` date DEFAULT NULL,
    `purchase_start_date` date DEFAULT NULL,
    `seat_number` int DEFAULT NULL,
    `ticket_status` int DEFAULT NULL,
    `event_id` int NOT NULL,
    `sector_id` int DEFAULT NULL,
    `user_id` int NOT NULL,
    PRIMARY KEY (`ticket_id`),
    KEY `FK3utafe14rupaypjocldjaj4ol` (`event_id`),
    KEY `FK3m71bqmlclisvjat05q6qyygo` (`sector_id`),
    KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
    CONSTRAINT `FK3m71bqmlclisvjat05q6qyygo` FOREIGN KEY (`sector_id`) REFERENCES `sectors` (`sector_id`),
    CONSTRAINT `FK3utafe14rupaypjocldjaj4ol` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
    CONSTRAINT `FK4eqsebpimnjen0q46ja6fl2hl` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.tickets: ~0 rows (approximately)

-- Dumping structure for table ticket_reservation.users
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` int NOT NULL AUTO_INCREMENT,
    `balance` double DEFAULT NULL,
    `email` varchar(255) NOT NULL,
    `full_name` varchar(255) DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `role` varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
    UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
    ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table ticket_reservation.users: ~3 rows (approximately)
INSERT INTO `users` (`user_id`, `balance`, `email`, `full_name`, `password`, `role`, `username`) VALUES
    (1, 0, 'elma@admin.com', 'Admin', 'admin', 'Administrator', 'admin'),
    (7, 0, 'elma@org.com', 'Elma Kahvedzic', 'elma', 'Organizator', 'elma'),
    (8, 2000, 'korisnik@gmail.com', 'Muhammed Ahmetovic', 'muhammed', 'Korisnik', 'muhammed');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
