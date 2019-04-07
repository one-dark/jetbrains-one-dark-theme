import os
import re
import shutil
import yaml
import xml.etree.cElementTree as ET
from xml.etree import ElementTree
from xml.etree.ElementTree import Element

SOURCE_DIR = os.path.join(os.path.dirname(__file__), 'config')
DEST_DIR = os.path.join(
    os.path.dirname(__file__),
    '..',
    'src',
    'main',
    'resources',
    'colorSchemes'
)
FILE_NAME = 'OneDark'


class Builder:
    def __init__(self, italic: bool, filename: str):
        self.italic = italic
        self.filename = filename

    def run(self) -> None:
        self.yaml = self.build_yaml()
        self.xml = self.build_xml()
        self.write_file()

    @staticmethod
    def read_yaml(filename: str) -> object:
        path = os.path.join(SOURCE_DIR, '%s.yaml' % filename)

        with open(path, 'r') as input_file:
            return yaml.load(input_file, Loader=yaml.FullLoader)

    def should_add_option(self, condition: str) -> bool:
        return condition == 'always' or (
            condition == 'theme' and self.italic is True
        )

    def get_color(self, color: str) -> str:
        for name, value in self.colors.items():
            if name == color:
                return value

        return color

    def build_yaml(self) -> dict:
        self.colors = self.read_yaml('colors')
        ide = self.read_yaml('ide')
        theme = self.read_yaml('theme')

        # Get a map of the ide options that points the option name
        #   to what type of style it is (i.e. font-type)
        ide_map = {
            option: style
            for style, options in ide.items()
            for option, _ in options.items()
        }

        for attribute, options in list(theme['attributes'].items()):
            # String-only options are the foreground color
            if isinstance(options, str):
                theme['attributes'][attribute] = {
                    'foreground': self.get_color(options)
                }

                continue

            for option, condition in list(options.items()):
                if option in ide_map:
                    # Remove the original option
                    del theme['attributes'][attribute][option]

                    # Add the actual JetBrains option if it applies to this theme
                    if self.should_add_option(condition):
                        key = ide_map[option]
                        value = ide[key][option]

                        theme['attributes'][attribute][key] = value
                else:
                    theme['attributes'][attribute][option] = self.get_color(condition)

        return theme

    def transform(self, text: str) -> str:
        return text.replace('-', '_').upper()

    def build_xml(self) -> ElementTree:
        scheme = ET.Element('scheme')

        scheme.attrib['name'] = '%s italic' % self.yaml['name'] \
            if self.italic \
            else self.yaml['name']

        scheme.attrib['parent_scheme'] = self.yaml['parent-scheme']
        scheme.attrib['version'] = '142'

        colors = ET.SubElement(scheme, 'colors')
        for name, value in self.yaml['colors'].items():
            ET.SubElement(colors, 'option', name=name, value=self.get_color(value))

        attributes = ET.SubElement(scheme, 'attributes')

        for attribute, base_attribute in self.yaml['inheriting-attributes'].items():
            ET.SubElement(
                attributes,
                'option',
                name=attribute,
                baseAttributes=base_attribute
            )

        for name, styles in self.yaml['attributes'].items():
            option = ET.SubElement(attributes, 'option', name=name)
            value = ET.SubElement(option, 'value')

            for style_name, style_value in styles.items():
                ET.SubElement(
                    value,
                    'option',
                    name=self.transform(style_name),
                    value=style_value
                )

        attributes[:] = sorted(attributes, key=lambda e: e.get('name'))

        return ET.ElementTree(scheme)

    def write_file(self) -> None:
        self.xml.write(os.path.join(DEST_DIR, self.filename))


def main():
    if not os.path.exists(DEST_DIR):
        os.makedirs(DEST_DIR)

    Builder(False, '%s.xml' % FILE_NAME).run()
    Builder(True, '%sItalic.xml' % FILE_NAME).run()


if __name__ == '__main__':
    main()
