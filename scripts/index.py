import os
import shutil
import yaml
import xml.etree.cElementTree as ET


DIST_DIR = os.path.join(os.path.dirname(__file__), '..', 'dist')
THEME_NAME = 'One dark'


def build_xml() -> str:
    root = ET.Element('root')
    doc = ET.SubElement(root, 'doc')

    ET.SubElement(doc, 'field1', name='blah').text = 'some value1'
    ET.SubElement(doc, 'field2', name='asdfasd').text = 'some vlaue2'
    tree = ET.ElementTree(root)
    print(tree)


def write_file(dest: str, data: str) -> None:
    with open(os.path.join(DIST_DIR, dest), 'w') as output_file:
        output_file.write(data)


def main():
    if not os.path.exists(DIST_DIR):
        os.mkdir(DIST_DIR)

    color_xml = build_xml()
    write_file(f'{THEME_NAME}.icls', color_xml)
    write_file(f'{THEME_NAME}.xml', color_xml)
    # shutil.rmtree(DIST_DIR)


if __name__ == '__main__':
    main()
