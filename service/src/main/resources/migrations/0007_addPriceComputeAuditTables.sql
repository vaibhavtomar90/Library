


CREATE TABLE `ListingDataVersion` (
`listingID` char(26) NOT NULL,
`dataVersion` bigint(20) UNSIGNED  NOT NULL,
 PRIMARY KEY(`listingID`)
)ENGINE=InnoDB ;


CREATE TABLE `PriceComputationAudit` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `listingID` char(26) NOT NULL,
  `mrp` double NOT NULL,
  `fsp` double NOT NULL,
  `fk_discount` double NOT NULL,
  `computedAt` timestamp DEFAULT CURRENT_TIMESTAMP ,
  `jsonContext` text,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;


CREATE TABLE `PriceComputationLatest` (
   `listingID` char(26) NOT NULL,
   `priceVersion` bigint(20) unsigned ,

   PRIMARY KEY (`listingID`),
   UNIQUE KEY (`priceVersion`),
   FOREIGN KEY (`priceVersion`) REFERENCES `PriceComputationAudit` (`id`)

)ENGINE=InnoDB ;

CREATE INDEX PriceComputationLatest_ListingID on PriceComputationLatest(listingID);

CREATE INDEX PriceComputationLatestIdx_PriceVersion on PriceComputationLatest(priceVersion);

CREATE INDEX ListingDataVersionIdx_ListingID on ListingDataVersion(listingID);





