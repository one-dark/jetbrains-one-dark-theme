import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

const val READ_ME_FILE = "README.md"

open class CopyReadme : DefaultTask() {

  init {
    group = "documentation"
    description = "Copies the readme to a place where it can be converted with other markdown files"
  }

  @TaskAction
  fun run() {
    val markdownPath = createMarkdownDirectory(project)
    val readmeMarkdown = Paths.get(
      markdownPath.toString(),
      READ_ME_FILE
    )
    Files.copy(
      Paths.get(project.rootDir.absolutePath, READ_ME_FILE),
      readmeMarkdown,
      StandardCopyOption.REPLACE_EXISTING
    )

    val trimmedREADME = Files.readAllLines(readmeMarkdown)
      .stream()
      .skip(1)
      .filter {
        // ignore badges
        it.contains("img.shields.io").not()
      }
      .map { readmeLine ->
        readmeLine to (readmeLine != "## Thanks")
      }
      .reduce { accum, next ->
        if (accum.second) {
          val shouldWrite = accum.second && next.second
          if (shouldWrite) {
            (accum.first + "\n" + next.first) to true
          } else {
            accum.first to false
          }
        } else {
          accum // ignore all lines after "Thanks"
        }
      }.get().first

    Files.newBufferedWriter(
      readmeMarkdown,
      StandardOpenOption.TRUNCATE_EXISTING
    ).use { it.write(trimmedREADME) }
  }
}
