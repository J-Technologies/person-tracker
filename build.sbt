name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.8"

//Axon deps
libraryDependencies ++= Seq(
  "org.axonframework"           % "axon-core"  % "3.0-M1",
  "joda-time"                   % "joda-time"  % "2.8.1"
)

//Server deps
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % "0.13.2",
  "org.http4s" %% "http4s-blaze-core"   % "0.13.2",
  "org.http4s" %% "http4s-blaze-server" % "0.13.2"
)

//Test deps
libraryDependencies ++= Seq(
  "org.scalatest"               %% "scalatest"          % "2.2.6" % "test",
  "org.axonframework"           % "axon-test"           % "3.0-M1",
  "org.hamcrest"                % "hamcrest-core"           % "1.3"
)


//Couchbase deps
resolvers += "ReactiveCouchbase Releases" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases/"
resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies ++= Seq(
  "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.3"
)

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.3"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
