import os
import shutil
import yaml
import xml.etree.cElementTree as ET
import xml.etree.ElementTree as ElementTree

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


def read_yaml(name: str) -> object:
    with open(os.path.join(SOURCE_DIR, f'{name}.yaml'), 'r') as input_file:
        return yaml.load(input_file)


def should_add_option(condition: str, italic: bool) -> bool:
    return condition == 'always' or (
        condition == 'theme' and italic is True
    )


def build_yaml(italic: bool) -> dict:
    colors = read_yaml('colors')
    ide = read_yaml('ide')
    theme = read_yaml('theme')

    # Get a map of the ide options that points the option name
    #   to what type of style it is (i.e. font-type)
    ide_map = {
        option: style
        for style, options in ide.items()
        for option, _ in options.items()
    }

    for attribute, options in list(theme['attributes'].items()):
        for option, condition in list(options.items()):
            if option in ide_map:
                # Remove the original option
                del theme['attributes'][attribute][option]

                # Add the actual JetBrains option if it applies to this theme
                if should_add_option(condition, italic):
                    key = ide_map[option]
                    value = ide[key][option]

                    theme['attributes'][attribute][key] = value
    return theme


def transform(text: str) -> str:
    return text.replace('-', '_').upper()


def build_xml(theme: dict, italic: bool) -> ElementTree:
    scheme = ET.Element('scheme')
    scheme.attrib['name'] = theme['name']
    scheme.attrib['parent_scheme'] = theme['parent-scheme']
    scheme.attrib['version'] = theme['version']

    colors = ET.SubElement(scheme, 'colors')
    for name, value in theme['colors'].items():
        ET.SubElement(colors, 'option', name=name, value=value)

    attributes = ET.SubElement(scheme, 'attributes')

    for attribute, base_attribute in theme['inheriting-attributes'].items():
        ET.SubElement(
            attributes,
            'option',
            name=attribute,
            baseAttributes=base_attribute
        )

    for name, styles in theme['attributes'].items():
        option = ET.SubElement(attributes, 'option', name=name)
        value = ET.SubElement(option, 'value')

        for style_name, style_value in styles.items():
            ET.SubElement(
                value,
                'option',
                name=transform(style_name),
                value=style_value
            )

    return ET.ElementTree(scheme)


def write_file(tree: ElementTree, name: str) -> None:
    tree.write(os.path.join(DEST_DIR, name))


def main():
    if not os.path.exists(DEST_DIR):
        os.mkdir(DEST_DIR)

    theme_xml = build_xml(build_yaml(italic=False), italic=False)
    italic_xml = build_xml(build_yaml(italic=True), italic=True)

    write_file(theme_xml, f'{FILE_NAME}.xml')
    write_file(italic_xml, f'{FILE_NAME}Italic.xml')


if __name__ == '__main__':
    main()
