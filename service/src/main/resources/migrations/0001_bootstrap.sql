--liquibase formatted sql

--changeset souvik:1 runOnChange:true

CREATE TABLE `signal_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `default_value` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `listing_infos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `listing` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`listing`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `signals` (
  `listing_id` bigint(20) NOT NULL,
  `signal_type_id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `value` varchar(255) NOT NULL,
  `server_timestamp` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expiry` datetime NOT NULL,
  PRIMARY KEY (`listing_id`, `signal_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--rollback drop table signals;
--rollback drop table listing_infos;
--rollback drop table signal_types;



