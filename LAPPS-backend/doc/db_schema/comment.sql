CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authorId` int(11) NOT NULL,
  `appId` int(11) NOT NULL,
  `content` varchar(1400) COLLATE utf8_unicode_ci NOT NULL,
  `rating` int(11) DEFAULT NULL,
  `releaseDate` timestamp NULL DEFAULT NULL,
  `lastUpdateDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `authorId_idx` (`authorId`),
  CONSTRAINT `authorId` FOREIGN KEY (`authorId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `appId` FOREIGN KEY (`appId`) REFERENCES `app` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
