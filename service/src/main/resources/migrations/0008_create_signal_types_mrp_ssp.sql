--liquibase formatted sql

--changeset yogesh:8 runOnChange:true

INSERT INTO SignalType(defaultValue,name,type) VALUES (NULL,'mrp','PRICE');
INSERT INTO SignalType(defaultValue,name,type) VALUES (NULL,'ssp','PRICE');

--rollback delete from `SignalType`;




