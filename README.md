# JetBrains One dark theme

![Build status](https://img.shields.io/travis/com/markypython/jetbrains-one-dark-theme.svg)
![JetBrains plugin version](https://img.shields.io/jetbrains/plugin/v/11938-one-dark-theme.svg)
![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/11938-one-dark-theme.svg)

[One dark](https://github.com/atom/one-dark-syntax) theme for JetBrains IDEs

## Installation

1. Go to `Preferences | Plugins | Marketplace` and search for **One dark theme**
1. Install the plugin
1. Restart IDE
1. Go to `Preferences | Editor | Colors & Fonts` and select one of the new color themes

## Contributing

### Theme source files

The source files for this theme are stored in the `scripts/config` directory. They are built into the theme files by running `python scripts/build.py`. Python 3 and PyYAML are required.

### Building the plugin

To build the plugin, run the following commands.

```sh
./gradlew assemble
./gradlew build
```

If using IntelliJ, sync the Gradle project and run the **Build** task.

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
