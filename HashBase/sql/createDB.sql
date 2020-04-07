-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 29. Nov 2016 um 16:04
-- Server-Version: 10.1.19-MariaDB
-- PHP-Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `kaese`
--
CREATE DATABASE IF NOT EXISTS `kaese` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `kaese`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hashing`
--

CREATE TABLE `hashing` (
  `id` int(12) UNSIGNED NOT NULL,
  `plain` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sha1` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sha256` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sha384` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sha512` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `md5` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `md2` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `hashing`
--
ALTER TABLE `hashing`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `hashing`
--
ALTER TABLE `hashing`
  MODIFY `id` int(12) UNSIGNED NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
