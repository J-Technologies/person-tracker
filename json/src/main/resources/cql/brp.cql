CREATE TABLE IF NOT EXISTS "TokenEntry" (
	"processName" varchar, 
	"segment" int,
	"tokenBuffer" blob, 
	"tokenType" varchar, 
	"timeStamp" timestamp,
 	PRIMARY KEY("processName", "segment")
);
CREATE TABLE IF NOT EXISTS "PersoonEntry" (
	"bsn" varchar, 
	"naam" varchar,
	"geslacht" varchar,
	"geboortedatum" varchar,
	"ouders" list<varchar>,
	"burgelijkeStaat" varchar,
	"partner" varchar,
	"overleden" boolean,
 	PRIMARY KEY("bsn")
);
