name := "scalaassignment"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.apache.spark" %% "spark-core" % "3.3.2",
  "org.apache.spark" %% "spark-sql" % "3.3.2",
  "org.apache.hadoop" % "hadoop-client" % "3.3.2",
  "org.apache.hadoop" % "hadoop-client-api" % "3.3.2",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.scalatestplus" %% "scalacheck-1-17" % "3.2.15.0" % "test",
  "io.chrisdavenport" %% "cats-scalacheck" % "0.3.2"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-Ymacro-annotations"
)

javaOptions ++= Seq(
  "--add-opens", "java.base/java.lang=ALL-UNNAMED",
  "--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED",
  "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
  "--add-opens", "java.base/java.io=ALL-UNNAMED",
  "--add-opens", "java.base/java.net=ALL-UNNAMED",
  "--add-opens", "java.base/java.nio=ALL-UNNAMED",
  "--add-opens", "java.base/java.util=ALL-UNNAMED",
  "--add-opens", "java.base/java.util.concurrent=ALL-UNNAMED",
  "--add-opens", "java.base/java.util.concurrent.atomic=ALL-UNNAMED",
  "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
  "--add-opens", "java.base/sun.nio.cs=ALL-UNNAMED",
  "--add-opens", "java.base/sun.security.action=ALL-UNNAMED",
  "--add-opens", "java.base/sun.util.calendar=ALL-UNNAMED",
  "--add-opens", "java.secuiy.jgss/sun.security.krb5=ALL-UNNAMED"
)
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)