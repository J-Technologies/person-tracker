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
	"overleden" boolean,
 	PRIMARY KEY("bsn")
);
