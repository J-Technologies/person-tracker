CREATE TABLE IF NOT EXISTS "DomainEventEntry" (
	"aggregateIdentifier" varchar, 
	"sequenceNumber" bigint, 
	"type" varchar, 
	"eventIdentifier" varchar, 
	"metaDataBuffer" blob, 
	"payloadBuffer" blob, 
	"payloadRevision" varchar, 
	"payloadType" varchar, 
	"timeStamp" timestamp,
	"globalIndex" bigint,
 	PRIMARY KEY("aggregateIdentifier", "sequenceNumber")
);
CREATE TABLE IF NOT EXISTS "SnapshotEventEntry" (
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
CREATE TABLE IF NOT EXISTS "EventLogEntry" (
	"batchIndex" bigint,
	"globalIndex" bigint,
	"aggregateIdentifier" varchar,
	"sequenceNumber" bigint,
 	PRIMARY KEY("batchIndex", "globalIndex")
);
CREATE TABLE IF NOT EXISTS "Counters" (
	"name" varchar, 
	"value" bigint,
 	PRIMARY KEY("name")
);
