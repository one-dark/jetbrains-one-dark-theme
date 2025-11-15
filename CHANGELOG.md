# Changelog

#### 5.15.0

- Added 2025.3 Build Support with removing restriction of `untilBuild` 

#### 5.14.0

- Added 2025.2 Build Support

#### 5.13.1

- Github action/cache v4

#### 5.13.0

- Added 2025.1 Build Support

#### 5.12.0

- Added 2024.3 Build Support

#### 5.11.0

- Added 2024.2 Build Support

#### 5.10.0

- Added 2024.1 Build Support

#### 5.9.0

- Added 2023.3 Build Support
- Removed some internal API usages

#### 5.8.0

- Added 2023.2 Build Support

#### 5.7.4

- More usable pressed action button state.
- Themed New UI Scrollbars in Rider to be consistent.

#### 5.7.3

- Themed the status bar memory indicator widget.
- Added a bit more Rust lang syntax highlighting support. 
- Added initial 2023.1 Build Support

#### 5.7.2

- Actually setting better foreground contrast for the experimental UI run widget and also coloring the inactive run widget. [#271](https://github.com/one-dark/jetbrains-one-dark-theme/issues/271)
- Small Experimental UI consistency updates.

#### 5.7.1

- Better foreground contrast for the experimental UI run widget and also coloring the inactive run widget. [#271](https://github.com/one-dark/jetbrains-one-dark-theme/issues/271)
- Inverted indeterminate progress bar colors to better support [running Jupyter notebook progression indicator](https://github.com/one-dark/jetbrains-one-dark-theme/issues/268) 

#### 5.7.0

- 2022.3 Build Support.
- Better Experimental UI Support.
- Made all highlighted commands in the terminal readable.

#### 5.6.0

- Added initial 2022.2 Build Support.

#### 5.5.2

- Added `Error Stripe Mark` to identifiers under caret.


#### 5.5.1

- Better 2022.1 Build Support
- Updated Cucumber syntax highlighting a bit.

#### 5.5.0

- 2022.1 Build Support.
- Themed PHP predefined symbols to be distinguishable from default symbols.

#### 5.4.2

- Better Jupyter Notebook support.

#### 5.4.1

- Themed [mnemonic line bookmarks](https://www.jetbrains.com/help/idea/bookmarks.html#add-mnemonic-line-bookmark).
- Updated the lookup background color to be consistent with the [vscode extension](https://github.com/one-dark/vscode-one-dark-theme).
- Updated the current tree node selection on the "Welcome Page".

#### 5.4.0

- Plugin only supports 2020.3 to 2021.3.X platform builds now.

#### 5.3.1

- Updates the colors used by the [IDE Features Trainer](https://plugins.jetbrains.com/plugin/8554-ide-features-trainer) plugin, for a more consistent learning experience.
- Restored autocomplete window coloring in 2021.2 builds.
- Made bookmark icon visible in favorites tree.

#### 5.3.0

- Plugin is now compatible with the `Code With Me` platform.
- Updated plugin's build tools.

#### 5.2.0

- Added themed [CSV Plugin](https://plugins.jetbrains.com/plugin/10037-csv/) configurations.
- Better 2021.2 build look and feel support.

#### 5.1.6

- 2021.2 EAP build support!
- Fixed UI Freeze issues

#### 5.1.5

- Added identifier under caret error stripe color.
- Themed Default Inlays
- Updated Ruby's local variable foreground color.

#### 5.1.4

- 2021.1 EAP build support!
- Updated the foreground coloring of the Windows 10 Title bar

#### 5.1.3

- Fixed the problem with Typescript Type Guard not showing up. [See issue for more details](https://github.com/one-dark/jetbrains-one-dark-theme/issues/198)
- Updated VCS log list hover color.

#### 5.1.2

- Fixed a bug with the project file colors. [See the issue for more details](https://github.com/one-dark/jetbrains-one-dark-theme/issues/192)
- Fixed usability issue with the IntelliJ Ultimate UML Diagram. [See the issue for more details](https://github.com/one-dark/jetbrains-one-dark-theme/issues/194)
- Enhanced 2020.3 Welcome screen styling. [See the pull request for more details](https://github.com/one-dark/jetbrains-one-dark-theme/pull/193)

#### 5.1.1

- 2020.3 Build support.
- More distinguishable VCS current branch highlighting. [See issue for more details](https://github.com/one-dark/jetbrains-one-dark-theme/issues/187).

#### 5.1.0

- Velocity Template Language support.
- Changed `Default Templating Language` color, known languages effected:
    - Angular
    - JSP
    - Twig
    - XSLT
- Enhanced Shell Script scope colorings.
- Enhanced HTTP Request scope colorings.
- Enhanced EJS tag scope colorings
- Supporting SCSS variable colors now.

Please see [this pull request for more details](https://github.com/one-dark/jetbrains-one-dark-theme/pull/184#issue-475964163)

#### 5.0.1 

- Added settings spotlight border color.
    - Color of the border around highlighted items when searching in the Settings dialog
- Non-Functional changes:
    - Cleaned up documentation
    - Updated build process
    - Removed log noise

#### 5.0.0

- Reverted 4.X changes
- HTML Attributes can now be italic. 
- Added Android Log-Cat Colors

#### 4.0.0

- Customizable Theme Settings
    - Removed the: `One Dark Italic`, `One Dark Vivid`, and `One Dark Vivid Italic` variants.
    - You can now customize your theme's font settings using the `One Dark Theme` in the settings menu located at: `Preferences | Appearance & Behavior | One Dark Theme`
    - Font settings can be `Regular`, `Bold`, `Italic`, or `Bold Italic`.
    - You can also choose between the `Vivid` and `Regular` color One Dark color palette.

- HTML Attributes can now be: `Regular`, `Bold`, `Italic`, or `Bold Italic`. 
    
#### 3.3.6

- Add support for 2020.2

#### 3.3.5

- Fix border color inconsistencies.
- Add sponsorship section to the readme.

#### 3.3.4

- Fixed issue with remaining editors not applying correct colors.

#### 3.3.3

- Fixed issue with Go not applying correct colors.

#### 3.3.2

- Fixed issue with PyCharm not applying correct colors after restart.

#### 3.3.1

- Fix JavaScript colors

#### 3.3.0

- Add blue background to current item in combo box and tree list.
- Fix Go colors.

#### 3.2.0

- Move information from the docs to the readme to make initial setup easier.
- Update the main screenshot with a higher res image.
- Remove the v2 migration information from the docs.
- Remove legacy v2 installation instructions from the docs.

#### 3.1.0

Improve the info label color to be more visible and have better color contrast. This is also used for the info color in the auto completion popup.

Thanks @tulongxCodes for the report of this issue!

#### 3.0.0

- Adds vivid and vivid italic themes!
  - This change may require you to reselect the color theme in the `Editor` -> `Color Scheme` preferences.
- Update file colors to improve contrast

#### 2.12.1

- Fix JavaScript and TypeScript colors after the 2019.2 release

#### 2.12.0

- Update the selection and identifier under caret background colors
- Update editor tab colors for IntelliJ 2019.2 compatibility
- Remove active debugger tab background in favor of the blue underline

#### 2.11.3

- Fix Go syntax highlighting

#### 2.11.2

- Improve tabbed pane hover color

#### 2.11.1

- Lighten selection color
- Fix console black color

#### 2.10.1

- Improve file colors

#### 2.10.0

- Minor Kotlin improvements
- Make labels bold
- Improve debugger colors
- Improve diff and merge colors

#### 2.9.0

- Improves syntax highlighting for the following languages:
  - Ruby
  - ERB
  - RDoc
  - Slim
  - Objective-C
  - Swift

#### 2.8.0

- Improves syntax highlighting for the following languages:
  - C
  - C++
  - Rust
- Improves syntax highlighting for Rider

#### 2.7.0

- Update class reference to chalky
- Improves syntax highlighting for the following languages:
  - Go
  - x86 assembly
  - Python
  - Django/Jinja2 Template
  - Mako Template
  - Jupyter
  - Puppet
  - GQL

#### 2.6.0

- Update Java static final field color to whiskey
- Update less and stylus variables to white
- Improves syntax highlighting for the following languages:
  - Dart
  - Pug

#### 2.5.0

- Update global constant color to whiskey
- Improves syntax highlighting for the following languages:
  - CoffeeScript
  - Haml
  - PHP
  - Sass
  - XPath

#### 2.4.1

- Fix issue where both theme files were being built

#### 2.4.0

- Fix bash shebang not using bold italic for italic theme
- Improves syntax highlighting database and database diagrams

#### 2.3.0

- Update groovy unresolved reference color
- Improves syntax highlighting for the following languages:
  - Python
  - Bash
  - reStructuredText
  - Buildout config

#### 2.2.1

- Update plugin description to pull from readme
- Trim changelog to the latest three entries for the plugin listing

#### 2.2.0

- Update current branch background in version control log
- Update console colors
- Update version control gutter and annotation colors
- Update diff and merge colors
- Update line coverage colors
- Update custom keyword colors

#### 2.1.0

- Update border colors
- Remove Rainbow Bracket colors. These should be defined by each user if they use this plugin.
- Update ignored file color
- Update popup colors
- Change unused symbol to use the default foreground and red underwaved.
- Update screenshots

#### 2.0.1

- Fixes an issue where the italic theme was not inheriting colors properly due to the CI server changing the order of the JSON theme definition file.

#### 2.0.0

- Added UI theme to make the editor and UI complement each other for the full One Dark experience!
- See [the wiki](https://github.com/one-dark/jetbrains-one-dark-theme/wiki/Updating-to-v2) for more details and upgrade instructions.

#### 1.7.1

- Improves syntax highlighting for the following languages:
  - JSP
  - Sass
  - OSGi Manifest
  - XPath
  - Less

#### 1.6.0

- Improves syntax highlighting for the following languages:
  - Markdown
  - Properties
  - Angular
  - XML
  - Database
  - Regex

#### 1.5.0

- Improves syntax highlighting for the following languages:
  - JavaScript
  - TypeScript
  - Kotlin
  - CSS

#### 1.4.1

- Improves syntax highlighting for the following languages:
  - Groovy
  - YAML
  - EditorConfig
  - Gerkin
  - JSON
  - HTML

#### 1.3.6

- Add changelog
- Change version and since build

#### 1.3.5

- Major updates to base colors and Java colors

#### 1.2.4

- Version bump due to JetBrains API problems

#### 1.2.3

- Update for version 2019
- Publish using Travis CI
- Add JARs to GitHub releases for manual installation
