--liquibase formatted sql

--changeset souvik:4 runOnChange:true

ALTER TABLE `signals` DROP COLUMN `server_timestamp`;
ALTER TABLE `signals` ADD COLUMN `server_timestamp` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;


