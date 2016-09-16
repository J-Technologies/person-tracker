version := "1.0"
scalaVersion := "2.11.8"

//Compile deps
libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.1.0"
)

//Webserver deps
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.7"
)