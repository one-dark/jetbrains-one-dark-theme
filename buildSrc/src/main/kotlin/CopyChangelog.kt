import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CopyChangelog : DefaultTask() {

  init {
    group = "documentation"
    description = "Copies the changelog to a place where it can be converted with other markdown files"
  }

  @TaskAction
  fun run() {
  }
}
