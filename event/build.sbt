version := "1.0"
scalaVersion := "2.11.8"

//Compile deps
libraryDependencies ++= Seq(
  "org.axonframework" % "axon-core" % "3.0-M3",
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.1.0"
)

//Runtime deps
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)