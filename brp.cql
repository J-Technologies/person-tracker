CREATE KEYSPACE IF NOT EXISTS "brp" WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3 };
DROP TABLE IF EXISTS "brp"."DomainEventEntry";
CREATE TABLE IF NOT EXISTS "brp"."DomainEventEntry" (
	"aggregateIdentifier" varchar, 
	"sequenceNumber" bigint, 
	"type" varchar, 
	"eventIdentifier" varchar, 
	"metaDataBuffer" blob, 
	"payloadBuffer" blob, 
	"payloadRevision" varchar, 
	"payloadType" varchar, 
	"timeStamp" timestamp,
	"transactionIndex" bigint,
 	PRIMARY KEY("aggregateIdentifier", "sequenceNumber")
);
DROP TABLE IF EXISTS "brp"."SnapshotEventEntry";
CREATE TABLE IF NOT EXISTS "brp"."SnapshotEventEntry" (
	"aggregateIdentifier" varchar, 
	"sequenceNumber" bigint, 
	"type" varchar, 
	"eventIdentifier" varchar, 
	"metaDataBuffer" blob, 
	"payloadBuffer" blob, 
	"payloadRevision" varchar, 
	"payloadType" varchar, 
	"timeStamp" timestamp,
 	PRIMARY KEY("aggregateIdentifier")
);
DROP TABLE IF EXISTS "brp"."TransactionEntry";
CREATE TABLE IF NOT EXISTS "brp"."TransactionEntry" (
	"transactionIndex" bigint, 
	"eventsReferences" list<frozen<tuple<varchar,bigint,bigint>>>,
 	PRIMARY KEY("transactionIndex")
);
DROP TABLE IF EXISTS "brp"."Counters";
CREATE TABLE IF NOT EXISTS "brp"."Counters" (
	"name" varchar, 
	"value" bigint,
 	PRIMARY KEY("name")
);
