version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.derby" % "derbyclient" % "10.11.1.1",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.1.Final",
  "dom4j" % "dom4j" % "1.6.1", // nodig voor hibernate-entitymanager
  "org.axonframework" % "axon-core" % "3.0-M1"
)
