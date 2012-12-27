import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sample"
  val appVersion      = "0.4"

  val appDependencies = Seq(
    javaCore,
    "morplay"   % "morplay_2.10" % "0.4"
  )

  object Resolvers {
     val githubRepository = "GitHub plugin repository" at "http://ladoch.github.com/repository/"
  }

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // TODO: uncomment, when repository will be public
    //resolvers ++= Seq(DefaultMavenRepository, Resolvers.githubRepository)
  )

}
