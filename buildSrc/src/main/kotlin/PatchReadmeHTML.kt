import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PatchReadmeHTML : DefaultTask() {

  init {
    group = "documentation"
    description = "Removes the content that is not wanted for the plugin description"
  }

  @TaskAction
  fun run() {
  }
}
