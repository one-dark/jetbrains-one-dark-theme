# Publishing

This plugin uses GitHub Actions for publishing builds, so the following instructions are included only for reference purposes for the future if the build process requires change.

- Create a personal access token
  - Login to [JetBrains Hub](https://hub.jetbrains.com)
  - Go to your user profile
  - Click on the **Authentication** tab
  - Click **New token ...**
  - Enter any name
  - Select **Plugin Repository** for the scope
- Enter the personal access token as a GitHub secret named `PUBLISH_TOKEN`.
