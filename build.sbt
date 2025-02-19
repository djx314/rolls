ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)
ThisBuild / version := "0.1.0-SNAPSHOT"
inThisBuild(
  List(
    organization           := "org.bitlap",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository :=
      "https://s01.oss.sonatype.org/service/local",
    homepage := Some(url("https://github.com/bitlap/bitlap")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        id = "dreamylost",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://blog.dreamylost.cn")
      )
    )
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheckAll")

lazy val scala3Version  = "3.2.0"
lazy val circeVersion   = "0.14.1"
lazy val jacksonVersion = "2.14.1"

Global / onChangedBuildSource := ReloadOnSourceChanges
lazy val commonSettings =
  Seq(
    scalaVersion                  := scala3Version,
    Global / onChangedBuildSource := ReloadOnSourceChanges,
//    publishConfiguration      := publishConfiguration.value.withOverwrite(true),
//    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    scalacOptions ++= Seq(
      /** "-Ycheck:all",** */
      "-language:dynamics",
      "-explain",
      "unchecked",
      "-deprecation",
      "-feature",
      "-Ydebug"
    )
  )

lazy val `rolls` = (project in file("."))
  .aggregate(
    `rolls-compiler-plugin`,
    `rolls-annotations`,
    `rolls-server`
  )
  .settings(
    publish / skip := true,
    commonSettings
  )

lazy val `rolls-annotations` = (project in file("rolls-annotations"))
  .settings(
    commonSettings,
    name := "rolls-annotations",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % jacksonVersion,
      "com.github.pjfanning"          %% "jackson-module-scala3-enum" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"    % jacksonVersion
    )
  )

lazy val `rolls-server` = (project in file("rolls-server"))
  .settings(
    commonSettings,
    name := "rolls-server",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.6.0",
      "com.typesafe"   % "config"     % "1.4.2"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
  .dependsOn(`rolls-compiler-plugin`)

lazy val `rolls-compiler-plugin` = (project in file("rolls-compiler-plugin"))
  .settings(
    commonSettings,
    name := "rolls-compiler-plugin",
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % scala3Version
    )
  )

lazy val `rolls-example` = (project in file("rolls-example"))
  .settings(
    commonSettings,
    publish / skip      := true,
    name                := "rolls-example",
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "0.1.0-SNAPSHOT")
//    libraryDependencies ++= List(
//      "org.bitlap" %% "rolls-annotations" % "0.1.0-SNAPSHOT"
//    )
  )
  .dependsOn(`rolls-annotations`)
