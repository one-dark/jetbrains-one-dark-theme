# Publishing

This plugin uses GitHub Actions for publishing builds. The following instructions are a reference for maintainers.

## Publishing a Release

Follow the steps below to publish a new release. Once the GitHub release has been created, a GitHub action will be triggered which will build the plugin and push it to the plugin repository.

1. Update the CHANGELOG.md file with the new version number and the change notes.
1. Push all changes to master.
1. Go to the [release page](https://github.com/one-dark/jetbrains-one-dark-theme/releases).
1. Click the "Draft a new release" button.
1. Enter the new version number for the tag and release name (e.g. `v1.0.0`).
1. Add the change notes from the changelog to the description.
1. Click "Publish release".

## Creating a Personal Access Token

Follow the steps below to create a personal access token. This should be added to a GitHub secret named `PUBLISH_TOKEN`.

1. Login to [JetBrains Hub](https://hub.jetbrains.com)
1. Go to your user profile
1. Click on the **Authentication** tab
1. Click **New token ...**
1. Enter any name
1. Select **Marketplace** for the scope
