SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `lapps` (in our current case, we have to stick with the name 'henm1415g1'
--
--CREATE DATABASE `lapps` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
-- `lapps`;

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
  `url` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
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
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

--
-- Dumping data; fully optional, remove if not needed
--

INSERT INTO `user` (`id`, `oidc_id`, `email`) VALUES
(1, '1', 'test@lapps.com');

INSERT INTO `app` (`id`, `name`, `rating`) VALUES
(1, 'First App', 0);

INSERT INTO `app_platform` (`id`, `name`) VALUES
(1, 'IOS');

INSERT INTO `tag` (`id`, `app_id`, `name`) VALUES
(1, 1, 'Some Tag');

INSERT INTO `comment` (`id`, `app_id`, `user_id`, `text`) VALUES
(1, 1, 1, 'Great App');

INSERT INTO `app_instance` (`id`, `app_id`, `platform_id`, `url`) VALUES
(1, 1, 1, 'www.apple.something');

INSERT INTO `app_instance_management` (`id`, `user_id`, `app_instance_id`, `rights`) VALUES
(1, 1, 1, 0);

INSERT INTO `app_detail_type` (`id`, `type`) VALUES
(1, 'Some Detail Type');

INSERT INTO `app_detail` (`id`, `app_detail_type_id`, `app_id`, `contents`) VALUES
(1, 1, 1, 'Some Content');

INSERT INTO `app_artifact_type` (`id`, `type`) VALUES
(1, 'Some Artifact Type');

INSERT INTO `app_artifact` (`id`, `app_artifact_type_id`, `app_id`, `url`) VALUES
(1, 1, 1, 'www.some.url');
