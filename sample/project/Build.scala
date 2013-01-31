import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sample"
  val appVersion      = "0.5.1"

  val appDependencies = Seq(
    javaCore,
    "morplay"   % "morplay_2.10" % "0.5.1"
  )

  object Resolvers {
     val githubRepository = Resolver.url("GitHub plugin repository", url("http://ladoch.github.com/repository/"))(Resolver.ivyStylePatterns)
  }

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(DefaultMavenRepository, Resolvers.githubRepository)
  )

}
