version := "1.0"
scalaVersion := "2.11.8"

//Server deps
libraryDependencies ++= Seq(
  "org.apache.derby" % "derby" % "10.11.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.7"
)