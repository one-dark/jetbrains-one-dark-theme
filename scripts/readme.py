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

        # Set image widths
        for img in soup.find_all('img'):
            img['width'] = '700'

        # Add margin above images
        for img in soup.find_all('img'):
            img.insert_before(soup.new_tag('br'))

        sections_to_remove = ['Contributors ✨', 'Thanks 🙏']

        for header in sections_to_remove:
            section = soup.find('h2', text=header)

            for tag in section.find_next_siblings():
                tag.decompose()

            section.decompose()

        # Remove comments
        for element in soup(text=lambda text: isinstance(text, Comment)):
            element.extract()

    with open(os.path.join(DEST_PATH, 'README.html'), 'w') as output_file:
        output_file.write(str(soup).strip())

    print('README generated!')
