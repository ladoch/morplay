import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "morplay"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "com.github.jmkgreen.morphia"       % "morphia"                 % "1.2.2",
    ("com.github.jmkgreen.morphia"      % "morphia-logging-slf4j"   % "1.2.2" % "compile" notTransitive())
      .exclude("org.slf4j","slf4j-simple")
      .exclude("org.slf4j","slf4j-jdk14"),
    ("com.github.jmkgreen.morphia"      % "morphia-validation"      % "1.2.2" % "compile" notTransitive())
      .exclude("org.slf4j","slf4j-simple")
      .exclude("org.slf4j","slf4j-jdk14"),
    "org.mockito"                       % "mockito-all"             % "1.9.0"
  )

  object Resolvers {
    val morphiaRepository = "Morphia Repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
    //val githubRepository =  Resolver.file("GitHub Repository", Path.userHome / "dev" / "leodagdag.github.com" / "repository" asFile)(Resolver.ivyStylePatterns)
  }

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(DefaultMavenRepository, Resolvers.morphiaRepository),
    publishMavenStyle := false,
    //publishTo := Some(githubRepository),
    checksums := Nil
  )

}
