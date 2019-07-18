name := "ScalaUdfVsSqlFunction"

version := "1.0"

scalaVersion := "2.12.8"
val sparkVersion = "2.4.3"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.apache.spark" %% "spark-core" % sparkVersion ,  // spark runtime already provides jars, but when not running through spark-submit, the dependency is not “provided”
    "org.apache.spark" %% "spark-sql" % sparkVersion ,
    "org.apache.spark" %% "spark-hive" % sparkVersion,
    
)

// see https://tpolecat.github.io/2017/04/25/scalac-flags.html for scalacOptions descriptions
scalacOptions ++= Seq(
    "-deprecation",     //emit warning and location for usages of deprecated APIs
    "-unchecked",       //enable additional warnings where generated code depends on assumptions
    "-explaintypes",    //explain type errors in more detail
    "-Ywarn-dead-code", //warn when dead code is identified
    "-Xfatal-warnings"  //fail the compilation if there are any warnings
)


