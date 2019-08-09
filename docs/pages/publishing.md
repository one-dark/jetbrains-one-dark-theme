---
layout: default
title: Publishing
nav_order: 6
---

This plugin uses Travis CI for publishing builds, so the following instructions are included only for reference purposes for the future if the build process requires change.

- Create a personal access token
  - Login to [JetBrains Hub](https://hub.jetbrains.com)
  - Go to your user profile
  - Click on the **Authentication** tab
  - Click **New token ...**
  - Enter any name
  - Select **Plugin Repository** for the scope
- Copy `gradle.properties.sample` to `gradle.properties` and enter your JetBrains Hub username and the token from the previous step.
- Run `./gradlew publishPlugin` (or Publish in IntelliJ)
