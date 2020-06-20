import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun createMarkdownDirectory(project: Project): Path =
  buildDirectory(project, "markdown")

private fun buildDirectory(project: Project, html: String): Path {
  val markdownPath = Paths.get(
    project.rootDir.absolutePath,
    "build",
    html
  )
  Files.createDirectories(markdownPath)
  return markdownPath
}
