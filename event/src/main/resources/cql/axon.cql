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
	"transactionIndex" bigint,
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
CREATE TABLE IF NOT EXISTS "TransactionEntry" (
	"transactionIndex" bigint, 
	"eventsReferences" set<frozen<tuple<varchar,bigint,bigint>>>,
 	PRIMARY KEY("transactionIndex")
);
CREATE TABLE IF NOT EXISTS "Counters" (
	"name" varchar, 
	"value" bigint,
 	PRIMARY KEY("name")
);
