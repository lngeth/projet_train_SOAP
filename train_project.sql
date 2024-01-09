-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 09 jan. 2024 à 23:54
-- Version du serveur : 8.0.28
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `train_project`
--

-- --------------------------------------------------------

--
-- Structure de la table `billet`
--

DROP TABLE IF EXISTS `billet`;
CREATE TABLE IF NOT EXISTS `billet` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flexible` tinyint(1) DEFAULT NULL,
  `classe` varchar(50) DEFAULT NULL,
  `idClient` int NOT NULL,
  `idVoyage` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idClient` (`idClient`),
  KEY `idVoyage` (`idVoyage`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `billet`
--

INSERT INTO `billet` (`id`, `flexible`, `classe`, `idClient`, `idVoyage`) VALUES
(1, 1, 'First', 1, 1),
(34, 1, 'First', 1, 1),
(47, 1, 'First', 9, 1),
(48, 1, 'First', 9, 1),
(49, 0, 'First', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`) VALUES
(1, 'Steerent'),
(2, 'Steeven'),
(3, 'Laurent'),
(9, 'Didier');

-- --------------------------------------------------------

--
-- Structure de la table `station`
--

DROP TABLE IF EXISTS `station`;
CREATE TABLE IF NOT EXISTS `station` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `station`
--

INSERT INTO `station` (`id`, `nom`) VALUES
(1, 'Nice'),
(2, 'Paris'),
(3, 'Toulon'),
(4, 'Toulouse'),
(5, 'Strasbourg'),
(6, 'Lille'),
(7, 'Lyon'),
(8, 'Montpellier'),
(9, 'Evry');

-- --------------------------------------------------------

--
-- Structure de la table `train`
--

DROP TABLE IF EXISTS `train`;
CREATE TABLE IF NOT EXISTS `train` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) DEFAULT NULL,
  `maxFirstClassePlaces` int NOT NULL,
  `maxBusinessClassePlaces` int NOT NULL,
  `maxStandardClassePlaces` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `train`
--

INSERT INTO `train` (`id`, `nom`, `maxFirstClassePlaces`, `maxBusinessClassePlaces`, `maxStandardClassePlaces`) VALUES
(1, 'BX45', 5, 5, 5),
(2, 'BX46', 5, 5, 5),
(3, 'BX4859', 25, 10, 15),
(4, 'ED4857', 30, 15, 10),
(5, 'YK9512', 20, 20, 25),
(6, 'DC3223', 10, 5, 20),
(7, 'QS8529', 10, 10, 15),
(8, 'OO4859', 15, 10, 10);

-- --------------------------------------------------------

--
-- Structure de la table `voyage`
--

DROP TABLE IF EXISTS `voyage`;
CREATE TABLE IF NOT EXISTS `voyage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dateDepart` datetime DEFAULT NULL,
  `idStationDepart` int NOT NULL,
  `idStationArrivee` int NOT NULL,
  `idTrain` int NOT NULL,
  `prixStandard` int NOT NULL,
  `prixFirst` int NOT NULL,
  `prixPremium` int NOT NULL,
  `prixFlexibilite` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idStationDepart` (`idStationDepart`),
  KEY `idStationArrivee` (`idStationArrivee`),
  KEY `idTrain` (`idTrain`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `voyage`
--

INSERT INTO `voyage` (`id`, `dateDepart`, `idStationDepart`, `idStationArrivee`, `idTrain`, `prixStandard`, `prixFirst`, `prixPremium`, `prixFlexibilite`) VALUES
(1, '2024-01-01 12:00:00', 2, 1, 1, 100, 120, 200, 30),
(3, '2024-01-02 12:00:00', 2, 3, 2, 120, 150, 250, 20),
(4, '2024-01-03 14:00:00', 3, 4, 2, 241, 254, 300, 10),
(5, '2024-01-04 15:00:00', 4, 5, 2, 100, 124, 150, 20),
(6, '2024-02-01 10:00:00', 5, 6, 5, 150, 250, 350, 30),
(7, '2024-02-02 08:00:00', 6, 7, 6, 75, 90, 130, 55),
(9, '2024-02-03 19:00:00', 7, 8, 4, 150, 170, 200, 25);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `billet`
--
ALTER TABLE `billet`
  ADD CONSTRAINT `billet_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `client` (`id`),
  ADD CONSTRAINT `billet_ibfk_2` FOREIGN KEY (`idVoyage`) REFERENCES `voyage` (`id`);

--
-- Contraintes pour la table `voyage`
--
ALTER TABLE `voyage`
  ADD CONSTRAINT `voyage_ibfk_1` FOREIGN KEY (`idStationDepart`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `voyage_ibfk_2` FOREIGN KEY (`idStationArrivee`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `voyage_ibfk_3` FOREIGN KEY (`idTrain`) REFERENCES `train` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
