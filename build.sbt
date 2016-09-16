name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.8"

lazy val cassandra = project
lazy val datatype = project
lazy val event = project.dependsOn(datatype, cassandra)
lazy val brp = project.dependsOn(event)
lazy val json = project.dependsOn(event)
lazy val soap = project.dependsOn(event)

run in Compile <<= (run in Compile in brp)