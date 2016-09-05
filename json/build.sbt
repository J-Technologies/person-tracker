version := "1.0"
scalaVersion := "2.11.8"

//Server deps
libraryDependencies ++= Seq(
  "org.apache.derby" % "derbynet" % "10.11.1.1",
  "org.apache.derby" % "derbyclient" % "10.11.1.1",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.1.Final",
  "dom4j" % "dom4j" % "1.6.1", // nodig voor hibernate-entitymanager
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.7"
)