#!/usr/bin/env node

import { readFile, writeFile } from "fs/promises";

const COLOR_MAP = {
  "#e06c75": "#ef596f", // coral
  "#56b6c2": "#2bbac5", // fountainBlue
  "#98c379": "#89ca78", // green
  "#abb2bf": "#bbbbbb", // lightWhite
  "#c678dd": "#d55fde", // purple
};

async function createVividVariant(inputPath, outputPath) {
  let content = await readFile(
    new URL(`../resources/${inputPath}`, import.meta.url),
    "utf8"
  );

  // Replace hex colors
  for (const [key, value] of Object.entries(COLOR_MAP)) {
    content = content.replaceAll(key, value);
  }

  // Replace theme references
  content = content.replaceAll("one_dark.xml", "one_dark_vivid.xml");
  content = content.replaceAll("One Dark", "One Dark Vivid");

  await writeFile(
    new URL(`../resources/${outputPath}`, import.meta.url),
    content
  );
}

const themes = [
  {
    input: "one_dark.theme.json",
    output: "one_dark_vivid.theme.json",
  },
  {
    input: "one_dark_islands.theme.json",
    output: "one_dark_islands_vivid.theme.json",
  },
  {
    input: "one_dark.xml",
    output: "one_dark_vivid.xml",
  },
];

for (const { input, output } of themes) {
  await createVividVariant(input, output);
}
