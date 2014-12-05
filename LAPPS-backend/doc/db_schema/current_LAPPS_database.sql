SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `lapps`
--
CREATE DATABASE `lapps` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `lapps`;

-- --------------------------------------------------------

--
-- Table structure for table `app`
--

CREATE TABLE IF NOT EXISTS `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=61 ;

--
-- Dumping data for table `app`
--

INSERT INTO `app` (`id`, `name`) VALUES
(55, 'Yeah'),
(56, 'Tutor 2.0'),
(57, 'Turbo App'),
(58, 'Serendipity'),
(59, 'ExpertApp'),
(60, 'HelpApp');

-- --------------------------------------------------------

--
-- Table structure for table `app_artifact`
--

CREATE TABLE IF NOT EXISTS `app_artifact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_artifact_type_id` int(11) NOT NULL,
  `app_instance_id` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_artifact_type_id` (`app_artifact_type_id`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=46 ;

--
-- Dumping data for table `app_artifact`
--

INSERT INTO `app_artifact` (`id`, `app_artifact_type_id`, `app_instance_id`, `url`) VALUES
(34, 1, 54, 'http://lorempixel.com/500/280/cats'),
(35, 2, 54, 'http://lorempixel.com/150/150/cats'),
(36, 1, 55, 'http://lorempixel.com/500/280/cats'),
(37, 2, 55, 'http://lorempixel.com/150/150/cats'),
(38, 1, 56, 'http://lorempixel.com/500/280/cats'),
(39, 2, 56, 'http://lorempixel.com/150/150/cats'),
(40, 1, 57, 'http://lorempixel.com/500/280/cats'),
(41, 2, 57, 'http://lorempixel.com/150/150/cats'),
(42, 1, 58, 'http://lorempixel.com/500/280/cats'),
(43, 2, 58, 'http://lorempixel.com/150/150/cats'),
(44, 1, 59, 'http://lorempixel.com/500/280/cats'),
(45, 2, 59, 'http://lorempixel.com/150/150/cats');

-- --------------------------------------------------------

--
-- Table structure for table `app_artifact_type`
--

CREATE TABLE IF NOT EXISTS `app_artifact_type` (
  `id` int(11) NOT NULL,
  `type` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `app_artifact_type`
--

INSERT INTO `app_artifact_type` (`id`, `type`) VALUES
(1, 'image/png'),
(2, 'thumbnail');

-- --------------------------------------------------------

--
-- Table structure for table `app_detail`
--

CREATE TABLE IF NOT EXISTS `app_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_detail_type_id` int(11) NOT NULL,
  `app_instance_id` int(11) NOT NULL,
  `contents` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_detail_type_id` (`app_detail_type_id`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=34 ;

--
-- Dumping data for table `app_detail`
--

INSERT INTO `app_detail` (`id`, `app_detail_type_id`, `app_instance_id`, `contents`) VALUES
(28, 1, 54, 'Lorem ipsum...'),
(29, 1, 55, 'Lorem ipsum...'),
(30, 1, 56, 'Lorem ipsum...'),
(31, 1, 57, 'Lorem ipsum...'),
(32, 1, 58, 'Lorem ipsum...'),
(33, 1, 59, 'Lorem ipsum...');

-- --------------------------------------------------------

--
-- Table structure for table `app_detail_type`
--

CREATE TABLE IF NOT EXISTS `app_detail_type` (
  `id` int(11) NOT NULL,
  `type` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `app_detail_type`
--

INSERT INTO `app_detail_type` (`id`, `type`) VALUES
(1, 'Short description'),
(2, 'General description');

-- --------------------------------------------------------

--
-- Table structure for table `app_instance`
--

CREATE TABLE IF NOT EXISTS `app_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `platform_id` int(11) NOT NULL,
  `url` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  `version` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `min_platform_version` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'The minimal ver of the platform the app instance can run on.',
  `size` int(11) DEFAULT NULL COMMENT 'in KB',
  `sourceUrl` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `date_modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `platform_id` (`platform_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=60 ;

--
-- Dumping data for table `app_instance`
--

INSERT INTO `app_instance` (`id`, `app_id`, `platform_id`, `url`, `version`, `min_platform_version`, `size`, `sourceUrl`, `date_created`, `date_modified`) VALUES
(54, 55, 1, 'http://store.apple.com/Yeah', NULL, NULL, 0, NULL, '2014-12-03 20:22:25', '2014-12-03 20:22:26'),
(55, 56, 1, 'http://store.apple.com/Tutor 2.0', NULL, NULL, 0, NULL, '2014-12-03 20:22:25', '2014-12-03 20:22:26'),
(56, 57, 1, 'http://store.apple.com/Turbo App', NULL, NULL, 0, NULL, '2014-12-03 20:22:25', '2014-12-03 20:22:26'),
(57, 58, 1, 'http://store.apple.com/Serendipity', NULL, NULL, 0, NULL, '2014-12-03 20:22:25', '2014-12-03 20:22:26'),
(58, 59, 1, 'http://store.apple.com/ExpertApp', NULL, NULL, 0, NULL, '2014-12-03 20:22:25', '2014-12-03 20:22:27'),
(59, 60, 1, 'http://store.apple.com/HelpApp', NULL, NULL, 0, NULL, '2014-12-03 20:22:26', '2014-12-03 20:22:27');

-- --------------------------------------------------------

--
-- Table structure for table `app_instance_management`
--

CREATE TABLE IF NOT EXISTS `app_instance_management` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `app_instance_id` int(11) NOT NULL,
  `rights` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'User (2^0), Admin (2^1)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_to_app_rights` (`user_id`,`app_instance_id`),
  KEY `user_id` (`user_id`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `app_platform`
--

CREATE TABLE IF NOT EXISTS `app_platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `app_platform`
--

INSERT INTO `app_platform` (`id`, `name`) VALUES
(1, 'iOS');

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_instance_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=12 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `app_instance_id`, `user_id`, `text`, `date`) VALUES
(10, 54, 34, 'I really love this app!', '2014-12-03 20:22:26'),
(11, 54, 36, 'Aaah.. that''s a crap...', '2014-12-03 20:22:26');

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_instance_id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oidc_id` bigint(20) NOT NULL,
  `email` varchar(128) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `username` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `roles` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=45 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `oidc_id`, `email`, `username`, `roles`) VALUES
(34, 2974, 'test1573@lapps.com', NULL, 0),
(35, 3428, 'test2644@lapps.com', NULL, 0),
(36, 1358, 'test1067@lapps.com', NULL, 0),
(39, 3195, 'test1177@test.com', NULL, 0),
(44, 1234567, 'test@lapps.com', NULL, 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `app_artifact`
--
ALTER TABLE `app_artifact`
  ADD CONSTRAINT `app_artifact_ibfk_2` FOREIGN KEY (`app_artifact_type_id`) REFERENCES `app_artifact_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_artifact_ibfk_3` FOREIGN KEY (`app_instance_id`) REFERENCES `app_instance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `app_detail`
--
ALTER TABLE `app_detail`
  ADD CONSTRAINT `app_detail_ibfk_2` FOREIGN KEY (`app_detail_type_id`) REFERENCES `app_detail_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_detail_ibfk_3` FOREIGN KEY (`app_instance_id`) REFERENCES `app_instance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `app_instance`
--
ALTER TABLE `app_instance`
  ADD CONSTRAINT `app_instance_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_instance_ibfk_2` FOREIGN KEY (`platform_id`) REFERENCES `app_platform` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `app_instance_management`
--
ALTER TABLE `app_instance_management`
  ADD CONSTRAINT `app_instance_management_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_instance_management_ibfk_2` FOREIGN KEY (`app_instance_id`) REFERENCES `app_instance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`app_instance_id`) REFERENCES `app_instance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tag`
--
ALTER TABLE `tag`
  ADD CONSTRAINT `tag_ibfk_1` FOREIGN KEY (`app_instance_id`) REFERENCES `app_instance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
