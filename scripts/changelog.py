import os
import markdown

SOURCE_FILE = os.path.join(os.path.dirname(__file__), '..', 'CHANGELOG.md')
DEST_FILE = os.path.join(
    os.path.dirname(__file__),
    '..',
    'build',
    'CHANGELOG.html'
)

if __name__ == '__main__':
    markdown.markdownFromFile(input=SOURCE_FILE, output=DEST_FILE)
