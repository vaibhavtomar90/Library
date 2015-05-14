--liquibase formatted sql

--changeset souvik:6 runOnChange:true

CREATE TABLE `SignalType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `defaultValue` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ListingInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `listing` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`listing`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `SignalInfo` (
  `listingId` bigint(20) NOT NULL,
  `signalTypeId` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `value` varchar(255) NOT NULL,
  `serverTimestamp` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `qualifier` varchar(64),
  PRIMARY KEY (`listingId`, `signalTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--rollback drop table `SignalInfo`;
--rollback drop table `ListingInfo`;
--rollback drop table `SignalType`;



