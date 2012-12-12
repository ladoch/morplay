import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "morplay"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "com.google.code.morphia"    % "morphia"               % "1.00-SNAPSHOT",
    "com.google.code.morphia"    % "morphia-logging-slf4j" % "0.99",
    "org.mockito"                % "mockito-all"           % "1.9.0"
  )

  object Resolvers {
    val morphiaRepository = "Morphia Repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
  }

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(DefaultMavenRepository, Resolvers.morphiaRepository)
  )

}
