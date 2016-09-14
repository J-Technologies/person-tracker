CREATE KEYSPACE IF NOT EXISTS "json" WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3 };
DROP TABLE IF EXISTS "json"."TokenEntry";
CREATE TABLE IF NOT EXISTS "json"."TokenEntry" (
	"processName" varchar, 
	"segment" int,
	"tokenBuffer" blob, 
	"tokenType" varchar, 
	"timeStamp" timestamp,
 	PRIMARY KEY("processName", "segment")
);
DROP TABLE IF EXISTS "json"."PersoonEntry";
CREATE TABLE IF NOT EXISTS "json"."PersoonEntry" (
	"bsn" varchar, 
	"naam" varchar, 
	"overleden" boolean,
 	PRIMARY KEY("bsn")
);
