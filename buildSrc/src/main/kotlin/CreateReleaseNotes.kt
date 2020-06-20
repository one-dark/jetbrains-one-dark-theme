import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateReleaseNotes : DefaultTask() {

  init {
    group = "documentation"
    description = "Creates release notes from system env set by pipeline"
  }

  @TaskAction
  fun run() {
  }
}
