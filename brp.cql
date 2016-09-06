CREATE KEYSPACE IF NOT EXISTS brp WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3 };
DROP TABLE IF EXISTS brp."DomainEventEntry";
CREATE TABLE IF NOT EXISTS brp."DomainEventEntry" (
	"index" varchar, 
	"aggregateIdentifier" varchar, 
	"sequenceNumber" bigint, 
	"type" varchar, 
	"eventIdentifier" varchar, 
	"metaData" blob, 
	"payload" blob, 
	"payloadRevision" varchar, 
	"payloadType" varchar, 
	"timeStamp" timestamp,
	"globalIndex" bigint,
 	PRIMARY KEY("index", "aggregateIdentifier", "sequenceNumber", "globalIndex")
);
DROP TABLE IF EXISTS brp."SnapshotEventEntry";
CREATE TABLE IF NOT EXISTS brp."SnapshotEventEntry" (
	"aggregateIdentifier" varchar, 
	"sequenceNumber" bigint, 
	"type" varchar, 
	"eventIdentifier" varchar, 
	"metaData" blob, 
	"payload" blob, 
	"payloadRevision" varchar, 
	"payloadType" varchar, 
	"timeStamp" timestamp,
 	PRIMARY KEY("aggregateIdentifier")
);
DROP TABLE IF EXISTS brp."BatchEntry";
CREATE TABLE IF NOT EXISTS brp."BatchEntry" (
	"batchIndex" bigint,
	"events" list<frozen<tuple<varchar, int>>>,
	PRIMARY KEY ("batchIndex")
);
DROP TABLE IF EXISTS brp."Indexes";
CREATE TABLE IF NOT EXISTS brp."Indexes" (
	"index" varchar,
	"batchIndex" counter,
	"globalIndex" counter,
	PRIMARY KEY ("index")
);
