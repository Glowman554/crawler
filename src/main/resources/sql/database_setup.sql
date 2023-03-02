CREATE TABLE IF NOT EXISTS `sites` (
  `site_id` int NOT NULL AUTO_INCREMENT,
  `link` text,
  `title` text,
  `text` LONGTEXT,
  `added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`site_id`),
  FULLTEXT KEY `title` (`title`,`link`,`text`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

