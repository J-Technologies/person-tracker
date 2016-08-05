name := "person-tracker"
version := "1.0"
scalaVersion := "2.11.8"

lazy val datatype = project
lazy val brp = project.dependsOn(datatype)
lazy val json = project.dependsOn(datatype)
lazy val soap = project.dependsOn(datatype)
