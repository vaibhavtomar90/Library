--liquibase formatted sql

--changeset souvik:5 runOnChange:true

DROP TABLE `signals`;
DROP TABLE `listing_infos`;
DROP TABLE `signal_types`;