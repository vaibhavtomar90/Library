--liquibase formatted sql

--changeset souvik:2 runOnChange:true

ALTER TABLE `signals` DROP COLUMN `expiry`;