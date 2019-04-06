import os
import markdown

SOURCE_FILE = os.path.join(os.path.dirname(__file__), '..', 'CHANGELOG.md')
DEST_PATH = os.path.join(os.path.dirname(__file__), '..', 'build')

if __name__ == '__main__':
    if not os.path.exists(DEST_PATH):
        os.makedirs(DEST_PATH)

    markdown.markdownFromFile(
        input=SOURCE_FILE,
        output=os.path.join(DEST_PATH, 'CHANGELOG.html')
    )
