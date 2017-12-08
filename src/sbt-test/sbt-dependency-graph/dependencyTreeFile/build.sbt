import java.nio.file.{ Files, Paths }

import scala.io.Source

scalaVersion := "2.9.1"

resolvers += "typesafe maven" at "https://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  "com.codahale" % "jerkson_2.9.1" % "0.5.0"
)

InputKey[Unit]("check") := {
  val is = Files.newInputStream(Paths.get("foo"))
  val tree = Source.fromInputStream(is).mkString
  is.close()

  require(
    tree ==
    """default:dependencytreefile_2.9.1:0.1-SNAPSHOT [S]
      |  +-com.codahale:jerkson_2.9.1:0.5.0 [S]
      |    +-org.codehaus.jackson:jackson-core-asl:1.9.11
      |    +-org.codehaus.jackson:jackson-mapper-asl:1.9.11
      |      +-org.codehaus.jackson:jackson-core-asl:1.9.11
      |      """
      .stripMargin,
    s"Tree didn't match expected:\n$tree"
  )
  ()
}
