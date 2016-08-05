version := "1.0"
scalaVersion := "2.11.8"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

//unmanagedBase := Path.fileProperty("java.home").toPath.resolveSibling("db").resolve("lib").toFile

//Axon deps
libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.8.1",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.3",
  "org.hibernate" % "hibernate-validator" % "5.2.4.Final"
  // mogelijk hebben we voor hibernate-validator ook nog een implementatie van EL nodig,
  // zie ook http://hibernate.org/validator/documentation/getting-started/
)

//Server deps
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.4"
)

//Test deps
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
  "org.axonframework" % "axon-test" % "3.0-M1",
  "org.hamcrest" % "hamcrest-core" % "1.3"
)