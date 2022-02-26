lazy val versions = new Object {
  val scala = "2.12.12"
  val spark = "2.4.6"
}

lazy val assemblySettings = Seq(
  assemblyJarName := name.value + ".jar",
  assembly / assemblyMergeStrategy := {
    case x if x.endsWith(".class") => MergeStrategy.last
    case x if x.endsWith(".properties") => MergeStrategy.last
    case x if x.contains("/resources") => MergeStrategy.last
    case x if x.startsWith("META-INF/maven/org.slf4j/slf4j-api/pom.") => MergeStrategy.first
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      if (oldStrategy==MergeStrategy.deduplicate)
        MergeStrategy.first
      else
        oldStrategy(x)
  }
)

lazy val settings = Seq(
  name := "flat-jar",
  version := "1.0",
  compile / mainClass := some("flat-jar"),
  scalaVersion := versions.scala,
  organization :="com.demo.test",
  resolvers := Seq(
    "Maven repo" at "https://repo1.maven.org/maven2",
    "aws-glue-etl-artifacts" at "https://aws-glue-etl-artifacts.s3.amazonaws.com/release/"
  ),
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % versions.spark,
    "org.apache.spark" %% "spark-sql" % versions.spark % "provided",
  ),
  Test / testOptions += Tests.Argument(TestFrameworks.ScalaCheck,"-verbosity","3"),
  Test / testOptions += Tests.Argument("-oF"),
  Test / javaOptions ++=Seq("-Xms2048M","-Xmx8G","-XX:MaxPermSize=2048M","-XX:+CMSClassUnloadingEnabled")
) ++ assemblySettings

lazy val root = (project in file("."))
  .settings(settings,assemblySettings)