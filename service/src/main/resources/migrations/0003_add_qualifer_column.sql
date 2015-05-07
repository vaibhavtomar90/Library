--liquibase formatted sql

--changeset souvik:3 runOnChange:true

ALTER TABLE `signals` ADD COLUMN `qualifier` varchar(64);
