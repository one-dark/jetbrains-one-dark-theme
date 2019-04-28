import os
import markdown
from bs4 import BeautifulSoup, Comment

SOURCE_FILE = os.path.join(os.path.dirname(__file__), '..', 'README.md')
DEST_PATH = os.path.join(os.path.dirname(__file__), '..', 'build')

if __name__ == '__main__':
    if not os.path.exists(DEST_PATH):
        os.makedirs(DEST_PATH)

    with open(SOURCE_FILE, 'r') as source:
        html = markdown.markdown(source.read())
        soup = BeautifulSoup(html, 'html.parser')

        # Remove title
        soup.find('h1').decompose()
        # Remove build badges
        soup.find('p').decompose()

        # Remove contributors section
        contributors = soup.find('h2', text='Contributors')

        for tag in contributors.find_next_siblings():
            tag.decompose()

        contributors.decompose()

        # Remove comments
        for element in soup(text=lambda text: isinstance(text, Comment)):
            element.extract()

    with open(os.path.join(DEST_PATH, 'README.html'), 'w') as output_file:
        output_file.write(str(soup).strip())
