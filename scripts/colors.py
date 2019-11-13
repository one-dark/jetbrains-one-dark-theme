import os
import yaml

CONFIG_DIR = os.path.join(os.path.dirname(__file__), 'config')


if __name__ == '__main__':
    with open(os.path.join(CONFIG_DIR, 'colors', 'normal.yaml'), 'r') as input_file:
        colors = yaml.load(input_file, Loader=yaml.FullLoader)

    theme_file_path = os.path.join(CONFIG_DIR, 'theme.yaml')

    with open(theme_file_path, 'r') as theme_file:
        content = theme_file.read()

    for name, value in colors.items():
        content = content.replace(value, name)

    with open(theme_file_path, 'w') as theme_file:
        theme_file.write(content)
