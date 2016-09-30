version := "1.0"
scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

//Webserver deps
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"
)

//Runtime deps
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)

//Test deps
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
  "org.axonframework" % "axon-test" % "3.0-M4",
  "org.hamcrest" % "hamcrest-core" % "1.3"
)