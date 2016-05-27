name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.8"

//Axon deps
libraryDependencies ++= Seq(
  "org.axonframework.scynapse"  % "scynapse-core_2.11"  % "0.5.5",
  "joda-time"                   % "joda-time"           % "2.8.1",
  "org.axonframework.scynapse"  % "scynapse-test_2.11"  % "0.5.5" % "test"
)

//Server deps
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % "0.13.2",
  "org.http4s" %% "http4s-blaze-core"   % "0.13.2",
  "org.http4s" %% "http4s-blaze-server" % "0.13.2"
)

//Test deps
libraryDependencies ++= Seq(
  "org.scalatest"               %% "scalatest"          % "2.2.6" % "test"
)


//Couchbase deps
resolvers += "ReactiveCouchbase Releases" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases/"
libraryDependencies ++= Seq(
  "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.3"
)