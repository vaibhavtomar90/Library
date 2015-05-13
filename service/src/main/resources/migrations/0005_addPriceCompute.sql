


CREATE TABLE `ListingDataVersion` (
`listingID` char(26) NOT NULL,
`version` bigint(20) UNSIGNED  NOT NULL,
 PRIMARY KEY(`listing_id`)
)ENGINE=InnoDB ;


CREATE TABLE `PriceComputationAudit` (
  `price_version` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `listing_id` char(26) NOT NULL,
  `mrp` double NOT NULL,
  `fsp` double NOT NULL,
  `fk_discount` double NOT NULL,
  `computedAt` timestamp DEFAULT CURRENT_TIMESTAMP ,
  `jsonContext` text,

  PRIMARY KEY (`price_version`)
) ENGINE=InnoDB ;


CREATE TABLE `PriceReConComputation` (
   `listing_id` char(26) NOT NULL,
   `price_version` bigint(20) unsigned ,

   PRIMARY KEY (`listing_id`),
   UNIQUE KEY (`price_version`),
   FOREIGN KEY (`price_version`) REFERENCES `PriceComputationAudit` (`price_version`)

)ENGINE=InnoDB ;

CREATE INDEX PriceReConComputationIdx_ListingID on PriceReConComputation(listing_id);

CREATE INDEX PriceReConComputationIdx_PriceVersion on PriceReConComputation(price_version);

CREATE INDEX ListingDataVersionIdx_ListingID on ListingDataVersion(listing_id);



