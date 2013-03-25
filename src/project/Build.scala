import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "morplay"
  val appVersion      = "0.7"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "com.github.jmkgreen.morphia"       % "morphia"                 % "1.2.3-SNAPSHOT",
    ("com.github.jmkgreen.morphia"      % "morphia-logging-slf4j"   % "1.2.3-SNAPSHOT" % "compile" notTransitive()),
    ("com.github.jmkgreen.morphia"      % "morphia-validation"      % "1.2.3-SNAPSHOT" % "compile" notTransitive()),
    "org.mockito"                       % "mockito-all"             % "1.9.0"
  )

  object Resolvers {
    val githubRepository =  Resolver.file("GitHub Repository", Path.userHome / "Projects" / "onlite" / "ladoch.github.com" / "repository" asFile)(Resolver.ivyStylePatterns)
    val sonatypeSnapshotsRepository = "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  }

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(DefaultMavenRepository, Resolvers.sonatypeSnapshotsRepository),
    publishMavenStyle := false,
    publishTo := Some(Resolvers.githubRepository),
    checksums := Nil
  )

}
