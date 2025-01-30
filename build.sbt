val Http4sVersion = "0.23.29"
val CirceVersion = "0.14.10"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "demo-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.3.3",
    libraryDependencies ++= Seq(
      "org.apache.logging.log4j" % "log4j-core" % "2.14.1",
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion
    ),
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x                   => (assembly / assemblyMergeStrategy).value.apply(x)
    },
    javaOptions ++= Seq(
      "-Dcom.sun.jndi.rmi.object.trustURLCodebase=true",
      "-Dcom.sun.jndi.ldap.object.trustURLCodebase=true"
    ),
    fork := true
  )
