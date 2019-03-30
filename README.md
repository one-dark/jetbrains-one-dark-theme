# JetBrains One dark theme

[![Build status][1]][2]
[![GitHub tag][3]][4]
[![JetBrains plugin version][5]][6]
[![JetBrains plugin downloads][7]][8]

[One dark](https://github.com/atom/one-dark-syntax) theme for JetBrains IDEs

## Installation

### Plugin marketplace **(recommended)**

1. Go to `Preferences | Plugins | Marketplace` and search for **One dark theme**
1. Install the plugin
1. When prompted, restart your IDE
1. Go to `Preferences | Editor | Colors & Fonts` and select one of the new color themes

### Manual

To install the plugin manually, use the following steps.

1. Download the JAR file for the [latest release][4] on GitHub
1. Go to `Preferences | Plugins` in your editor and click the gear icon at the top
1. Click **Install Plugin from Disk...**
1. Select the JAR file you downloaded
1. When prompted, restart your IDE
1. Go to `Preferences | Editor | Colors & Fonts` and select one of the new color themes

## Contributing

### Theme source files

The source files for this theme are stored in the `scripts/config` directory. They are built into the theme files by running `python scripts/build.py`. Python 3 and PyYAML are required.

### Building the plugin

To build the plugin, run `./gradlew build`. If using IntelliJ, sync the Gradle project and run the **Build** task.

### Publishing

- Create a personal access token
  - Login to [JetBrains Hub](https://hub.jetbrains.com)
  - Go to your user profile
  - Click on the **Authentication** tab
  - Click **New token ...**
  - Enter any name
  - Select **Plugin Repository** for the scope
- Copy `gradle.properties.sample` to `gradle.properties` and enter your JetBrains Hub username and the token from the previous step.
- Run `./gradlew publishPlugin` (or Publish in IntelliJ)

## Thanks

- [Egor Yurtaev](https://github.com/yurtaev) - Creator of the repository this project was based on

[1]: https://img.shields.io/travis/com/markypython/jetbrains-one-dark-theme.svg
[2]: https://travis-ci.com/markypython/jetbrains-one-dark-theme "Build status"
[3]: https://img.shields.io/github/tag/markypython/jetbrains-one-dark-theme.svg
[4]: https://github.com/markypython/jetbrains-one-dark-theme/releases/latest "Latest release"
[5]: https://img.shields.io/jetbrains/plugin/v/11938-one-dark-theme.svg
[6]: https://plugins.jetbrains.com/plugin/11938-one-dark-theme "Plugin homepage"
[7]: https://img.shields.io/jetbrains/plugin/d/11938-one-dark-theme.svg
[8]: https://plugins.jetbrains.com/dashboard/statistics/downloads?pluginId=11938 "Plugin downloads"
