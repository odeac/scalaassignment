name := "scalaassignment"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.10"

val sparkVersion = "3.2.1"
val framelessVersion = "0.13.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.hadoop" % "hadoop-client" % sparkVersion,
  "org.apache.hadoop" % "hadoop-client-api" % sparkVersion,
  "org.typelevel" %% "frameless-dataset-spark32" % framelessVersion,
//  "org.typelevel" %% "frameless-ml" % framelessVersion,
  "org.typelevel" %% "frameless-cats" % framelessVersion,
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