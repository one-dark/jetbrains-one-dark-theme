# Development

## Theme

The One Dark Theme is similar to other [custom UI themes](https://blog.jetbrains.com/idea/2019/03/brighten-up-your-day-add-color-to-intellij-idea) for the JetBrains platform.

The look and feel scheme is a static file that can be modified and is located here: `buildSrc/templates/oneDark.template.json`.

The 4 editor color schemes are built when the plugin is built or the `./gradlew runIde` task run.

Settings for the theme can be found in [configuration directory](https://intellij-support.jetbrains.com/hc/en-us/articles/206544519-Directories-used-by-the-IDE-to-store-settings-caches-plugins-and-logs)
on path `<configDir>/options/one_dark_config.xml` or when developing `build/idea-sandbox/config/options/one_dark_theme.xml`.

Template replacement structure:

- `%bold$theme^comments%` will always have the `bold` variant, so it will be italic only if the user specifies an italic variant: `ITALIC` or `BOLD_ITALIC`.
- `theme^attribute` will set the attribute to whatever the user has configured for the `attribute` group.
- `$purple$` will replace the value with the associated color.

## Color palette

This theme aims to create a color scheme that is as consistent as possible across languages. 
As such, this theme uses colors from the following color palette for as many scopes as applicable.

| Name | Value | Preview | Name | Value | Preview |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **chalky** | \#e5c07b | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/chalky.jpg) | **green** | \#98c379 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/green.jpg) |
| **coral** | \#e06c75 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/coral.jpg) | **lightWhite** | \#abb2bf | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/light-white.jpg) |
| **dark** | \#5c6370 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/dark.jpg) | **malibu** | \#61afef | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/malibu.jpg) |
| **error** | \#f44747 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/error.jpg) | **purple** | \#c678dd | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/purple.jpg) |
| **fountainBlue** | \#56b6c2 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/fountain-blue.jpg) | **whiskey** | \#d19a66 | ![](https://raw.githubusercontent.com/one-dark/jetbrains-one-dark-theme/master/docs/colors/whiskey.jpg) |

## Building the plugin

To build the plugin, run `./gradlew build`. 
If using IntelliJ, sync the Gradle project and run the **Build** task.

## Testing the plugin

To test the plugin, run `./gradlew runIde` to build and launch the plugin in a fresh instance of IntelliJ. 
If using IntelliJ, run the **Run IDE** task. 
All plugins will be disabled and settings will be the default settings for a new installation. 
When first running this task, you will need to change the appearance to Darcula to enable the One Dark themes in the color schemes. 
Select whichever scheme you wish to test, and change the settings.

