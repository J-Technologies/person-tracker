version := "1.0"
scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

//Compile deps
libraryDependencies ++= Seq(
  "org.axonframework" % "axon-cassandra" % "3.0-M4"
)
