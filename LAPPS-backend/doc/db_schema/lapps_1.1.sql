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
  `rating` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

--
-- Dumping data for table `app`
--

INSERT INTO `app` (`id`, `name`, `rating`) VALUES
(1, 'First App', 0),
(2, 'Second App', 0),
(3, 'Third App', 0),
(4, 'Fourth App', 0);

-- --------------------------------------------------------

--
-- Table structure for table `app_artifact`
--

CREATE TABLE IF NOT EXISTS `app_artifact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_artifact_type_id` int(11) NOT NULL,
  `app_id` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `app_artifact_type_id` (`app_artifact_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `app_artifact_type`
--

CREATE TABLE IF NOT EXISTS `app_artifact_type` (
  `id` int(11) NOT NULL,
  `type` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `app_detail`
--

CREATE TABLE IF NOT EXISTS `app_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_detail_type_id` int(11) NOT NULL,
  `app_id` int(11) NOT NULL,
  `contents` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `app_detail_type_id` (`app_detail_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `app_detail_type`
--

CREATE TABLE IF NOT EXISTS `app_detail_type` (
  `id` int(11) NOT NULL,
  `type` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `app_instance`
--

CREATE TABLE IF NOT EXISTS `app_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `platform_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `platform_id` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
  KEY `user_id` (`user_id`),
  KEY `app_instance_id` (`app_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `app_platform`
--

CREATE TABLE IF NOT EXISTS `app_platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_platform_type_id` int(11) NOT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_platform_type_id` (`app_platform_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `app_platform_type`
--

CREATE TABLE IF NOT EXISTS `app_platform_type` (
  `id` int(11) NOT NULL,
  `type` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `app_id`, `user_id`, `text`) VALUES
(1, 4, 9, 'Comment 1');

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oidc_id` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(128) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `oidc_id`, `email`) VALUES
(1, '1', 'test@lapps.com'),
(3, '3', 'author_test@ok.com'),
(5, '2', 'comment_test@ok.com'),
(6, '4', 'comment_test2@ok.com'),
(8, '5', 'comment_test3@ok.com'),
(9, '6', 'comment_test4@ok.com');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `app_artifact`
--
ALTER TABLE `app_artifact`
  ADD CONSTRAINT `app_artifact_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_artifact_ibfk_2` FOREIGN KEY (`app_artifact_type_id`) REFERENCES `app_artifact_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `app_detail`
--
ALTER TABLE `app_detail`
  ADD CONSTRAINT `app_detail_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `app_detail_ibfk_2` FOREIGN KEY (`app_detail_type_id`) REFERENCES `app_detail_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
-- Constraints for table `app_platform`
--
ALTER TABLE `app_platform`
  ADD CONSTRAINT `app_platform_ibfk_1` FOREIGN KEY (`app_platform_type_id`) REFERENCES `app_platform_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tag`
--
ALTER TABLE `tag`
  ADD CONSTRAINT `tag_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
  
CREATE USER `java_user`@`localhost` IDENTIFIED BY 'A6XwKeLFVCadZh2t';
GRANT ALL PRIVILEGES ON `lapps`.* TO `java_user`@`localhost`;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
