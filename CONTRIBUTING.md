# Contributing

## Build the plugin

To build the plugin, you can use the following command:

```bash
./gradlew buildPlugin
```

## Publishing

To publish a new version of the plugin, you can use the following command:

```bash
./gradlew publishPlugin -PintellijPlatformPublishingToken=$(op item get <item_id> --fields password --reveal)
```

_This is only necessary if you are the maintainer of the plugin. If you aren't, why are you trying to publish it?_
