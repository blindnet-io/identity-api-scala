ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.0"

Test / fork := true

val circeVersion  = "0.14.3"
val doobieVersion = "1.0.0-RC2"
val http4sVersion = "0.23.12"
val tapirVersion  = "1.1.0"

lazy val root = (project in file("."))
  .settings(
    name                             := "identity",
    organization                     := "io.blindnet",
    organizationName                 := "blindnet",
    organizationHomepage             := Some(url("https://blindnet.io")),
    idePackagePrefix                 := Some("io.blindnet.identity"),
    resolvers += "Blindnet Snapshots" at "https://nexus.blindnet.io/repository/maven-snapshots",
    libraryDependencies ++= Seq(
      "com.password4j"               % "password4j"                    % "1.6.2",
      "com.softwaremill.sttp.tapir" %% "tapir-core"                    % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"           % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"              % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"       % tapirVersion,
      "io.blindnet"                 %% "identity-client"               % "1.0.1-SNAPSHOT",
      "io.circe"                    %% "circe-core"                    % circeVersion,
      "io.circe"                    %% "circe-generic"                 % circeVersion,
      "io.circe"                    %% "circe-literal"                 % circeVersion % Test,
      "org.apache.commons"           % "commons-email"                 % "1.5",
      "org.flywaydb"                 % "flyway-core"                   % "9.3.0",
      "org.http4s"                  %% "http4s-blaze-server"           % http4sVersion,
      "org.http4s"                  %% "http4s-ember-client"           % http4sVersion,
      "org.http4s"                  %% "http4s-circe"                  % http4sVersion,
      "org.scalatest"               %% "scalatest"                     % "3.2.12"     % Test,
      "org.slf4j"                    % "slf4j-simple"                  % "2.0.1",
      "org.tpolecat"                %% "doobie-core"                   % doobieVersion,
      "org.tpolecat"                %% "doobie-hikari"                 % doobieVersion,
      "org.tpolecat"                %% "doobie-postgres"               % doobieVersion,
      "org.typelevel"               %% "cats-effect"                   % "3.3.14",
      "org.typelevel"               %% "cats-effect-testing-scalatest" % "1.4.0"      % Test,
      "org.typelevel"               %% "log4cats-slf4j"                % "2.5.0",
      "is.cir"                      %% "ciris"                         % "2.3.3"
    ),
    assembly / mainClass             := Some("io.blindnet.identity.Main"),
    assembly / assemblyJarName       := "identity.jar",
    assembly / assemblyMergeStrategy := {
      // format: off
      case PathList(ps @ _*) if ps.last == "module-info.class" => MergeStrategy.discard
      // format: o
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.discard
      case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") => MergeStrategy.singleOrError
      case x => assemblyMergeStrategy.value(x)
    },
    assembly / packageOptions += Package.ManifestAttributes(
      "Multi-Release" -> "true"
    )
  )
