-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 26 avr. 2023 à 16:35
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `pi2023`
--

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` varchar(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `user_ID` int DEFAULT NULL,
  `conversation_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_ID` (`user_ID`),
  KEY `conversation_id` (`conversation_id`)
) ENGINE=MyISAM AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `message`
--

INSERT INTO `message` (`id`, `author`, `timestamp`, `content`, `user_ID`, `conversation_id`) VALUES
(50, 'ethyle', '15:59', 'yo', NULL, NULL),
(27, 'tonio', '20:40', 'vous me recevez ?', NULL, NULL),
(45, 'ethyle', '15:58', 'j\'y vais a plus', NULL, NULL),
(48, 'ethyle', '15:59', 'à la prochaine guerre', NULL, NULL),
(44, 'ethyle', '15:58', 'faut que tu fasses tes preuves', NULL, NULL),
(43, 'kaito69', '15:58', 'quelqu\'un pourrait me faire passer modérateur ?', NULL, NULL),
(42, 'ethyle', '15:57', 'slt kaito', NULL, NULL),
(41, 'kaito69', '15:56', 'je suis nouveau', NULL, NULL),
(55, 'kaito69', '18:14', 'hello world', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_ID` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(191) NOT NULL,
  `first_name` varchar(191) NOT NULL,
  `pseudo` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  `last_connection` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT NULL,
  `grade` int NOT NULL,
  `ban` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_ID`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`user_ID`, `last_name`, `first_name`, `pseudo`, `password`, `last_connection`, `status`, `grade`, `ban`) VALUES
(29, 'Doe', 'John', 'johndoe', 'da39a3ee5e6b4b0d3255bfef95601890afd80709', NULL, 0, 2, 0),
(30, 'Martin', 'Emma', 'emmamartin', '40256ecddde9bc7cc0df6c443dc8ef18f9f7edae', NULL, 0, 1, 0),
(31, 'Taylor', 'Ethan', 'ethantaylor69', 'fb48a03d1bbdb4f219470dca3936bf32c60074f4', NULL, 0, 1, 0),
(32, 'Soty', 'Valentin', 'valouzz', 'd576096a8f85ba5492c5c435b0ddc993544e41b6', NULL, 0, 0, 0),
(33, 'Goudard', 'Arthur', 'ethyle', '4fabf5d260f80ab8ce1607e5f1ffcb1a40e135fd', NULL, 0, 2, 0),
(34, 'Gonnet', 'Matheo', 'matlebg', 'd511a3f4e786327b54f772198eafee762d2cb8ca', NULL, 0, 0, 0),
(43, 'Martinez', 'Antony', 'tonio', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', NULL, 0, 0, 0),
(45, 'Joye', 'Kaito', 'kaito69', '23ed30a594145905e5fb264400bdab1ee8bb5c1c', NULL, 0, 0, 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
