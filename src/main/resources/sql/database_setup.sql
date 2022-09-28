CREATE DATABASE IF NOT EXISTS `search`;

USE `search`;

CREATE TABLE IF NOT EXISTS `sites` (
  `id` int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `link` text NOT NULL,
  `title` text NOT NULL,
  `text` longtext NOT NULL,
  `added` timestamp not NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FULLTEXT KEY `title` (`title`, `link`, `text`)
);