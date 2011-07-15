import sbt._

class SampleProject(info: ProjectInfo) extends DefaultProject(info) {
    
  override def compileOptions = super.compileOptions ++ Seq(Unchecked, Deprecation)

  val slf4jSimple = "org.slf4j" % "slf4j-simple" % "1.6.0" % "test->default"

  val snapshots = "snapshots" at "http://scala-tools.org/repo-snapshots"
  val releases  = "releases" at "http://scala-tools.org/repo-releases"

}

