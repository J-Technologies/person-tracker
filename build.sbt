name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.axonframework.scynapse"  % "scynapse-core_2.11"  % "0.5.5",

  // Test deps
  "org.axonframework.scynapse"  % "scynapse-test_2.11"  % "0.5.5" % "test",
  "org.scalatest"               %% "scalatest"          % "2.2.6" % "test"
)