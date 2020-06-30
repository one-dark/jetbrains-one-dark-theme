import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths

open class CreateReleaseNotes : DefaultTask() {

  init {
    group = "documentation"
    description = "Creates release notes from system env set by pipeline"
  }

  @TaskAction
  fun run() {
    val markdownDir = createMarkdownDirectory(project)
    Files.newBufferedWriter(
      Paths.get(markdownDir.toString(), "CHANGELOG.md")
    ).use {
      it.write("""
          - Fixed issue with the settings not showing up in 2019.X builds.
          - Fixed issue with the notifications not showing up.
        """.trimIndent()
      )
    }
  }
}
