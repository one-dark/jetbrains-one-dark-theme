## Theme source files

The color scheme source files are stored in the `scripts/config` directory. To build the XML files used in IntelliJ, run `python3 ./scripts/build.py`. This will create the following files in the `src/main/resources/themes` directory:

* `one_dark.xml`
* `one_dark_italic.theme.json`
* `one_dark_italic.xml`

The JSON files were added as part of version 2 of this plugin when IntelliJ added support for [custom UI themes][custom-ui-themes]. Because IntelliJ includes the ability to preview a UI theme within the editor, these JSON files are not constructed using the same process as the XML color scheme files. Instead, the complete UI theme is contained in `src/main/resources/themes/one_dark.theme.json` and is used when creating the italic version which contains the same properties, but with a different name and editor color scheme.

## Color palette

This theme aims to create a color scheme that is as consistent as possible across languages.  As such, this theme uses colors from the following color palette for as many scopes as applicable.

| Name             | Value   | Preview          | Name           | Value   | Preview        |
|------------------|---------|------------------|----------------|---------|----------------|
| **chalky**       | #e5c07b | ![chalky]        | **green**      | #98c379 | ![green]       |
| **coral**        | #e06c75 | ![coral]         | **lightWhite** | #abb2bf | ![light-white] |
| **dark**         | #5c6370 | ![dark]          | **malibu**     | #61afef | ![malibu]      |
| **error**        | #f44747 | ![error]         | **purple**     | #c678dd | ![purple]      |
| **fountainBlue** | #56b6c2 | ![fountain-blue] | **whiskey**    | #d19a66 | ![whiskey]     |

## Building the plugin

To build the plugin, run `./gradlew build`. If using IntelliJ, sync the Gradle project and run the **Build** task.

## Testing the plugin

To test the plugin, run `./gradlew runIde` to build and launch the plugin in a fresh instance of IntelliJ. If using IntelliJ, run the **Run IDE** task. All plugins will be disabled and settings will be the default settings for a new installation. When first running this task, you will need to change the appearance to Darcula to enable the one dark themes in the color schemes. Select whichever scheme you wish to test, and save the settings.

[custom-ui-themes]: https://blog.jetbrains.com/idea/2019/03/brighten-up-your-day-add-color-to-intellij-idea "Custom UI themes"
[chalky]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/chalky.jpg
[coral]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/coral.jpg
[dark]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/dark.jpg
[error]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/error.jpg
[fountain-blue]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/fountain-blue.jpg
[green]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/green.jpg
[invalid]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/invalid.jpg
[light-white]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/light-white.jpg
[malibu]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/malibu.jpg
[purple]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/purple.jpg
[whiskey]: https://raw.githubusercontent.com/markypython/jetbrains-one-dark-theme/master/docs/colors/whiskey.jpg
