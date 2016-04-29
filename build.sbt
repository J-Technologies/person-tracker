name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.7"

//Axon deps
libraryDependencies ++= Seq(
  "org.axonframework.scynapse"  % "scynapse-core_2.11"  % "0.5.5",
  "org.axonframework.scynapse"  % "scynapse-test_2.11"  % "0.5.5" % "test"
)

//Server deps
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.13.2",
  "org.http4s" %% "http4s-blaze-core" % "0.13.2",
  "org.http4s" %% "http4s-blaze-server" % "0.13.2"
)

//Test deps
libraryDependencies ++= Seq(
  "org.scalatest"               %% "scalatest"          % "2.2.6" % "test"
)