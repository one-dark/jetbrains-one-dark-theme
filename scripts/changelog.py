import os
import re
import markdown
from bs4 import BeautifulSoup

SOURCE_FILE = os.path.join(os.path.dirname(__file__), '..', 'CHANGELOG.md')
DEST_PATH = os.path.join(os.path.dirname(__file__), '..', 'build')

if __name__ == '__main__':
    if not os.path.exists(DEST_PATH):
        os.makedirs(DEST_PATH)

    with open(SOURCE_FILE, 'r') as source:
        html = markdown.markdown(source.read())
        soup = BeautifulSoup(html, 'html.parser')

        # Remove the title
        soup.find('h1').decompose()

        # Find the fourth h4 tag
        tag_to_remove = soup.find('h4') \
            .find_next_sibling('h4') \
            .find_next_sibling('h4') \
            .find_next_sibling('h4')

        # Remove everything after the fourth h4 tag
        for tag in tag_to_remove.find_next_siblings():
            tag.decompose()

        # Remove the fourth h4 tag
        tag_to_remove.decompose()

        # Remove empty space
        soup = BeautifulSoup(re.sub(r'[\n]{2,}', '', str(soup)), 'html.parser')

        # Add a triple dot indicating more history
        triple_dot = soup.new_tag('p')
        triple_dot.string = '...'
        soup.append('\n')
        soup.append(triple_dot)
        soup.append(soup.new_tag('br'))

        # Add the link to the full changelog
        more_link = soup.new_tag(
            'a',
            href='https://github.com/markypython/jetbrains-one-dark-theme/blob/master/CHANGELOG.md'
        )
        more_link.string = 'GitHub repo'

        more = soup.new_tag('p')
        more.append('View the full changelog on our ')
        more.append(more_link)
        more.append('.')
        soup.append('\n')
        soup.append(more)

    with open(os.path.join(DEST_PATH, 'CHANGELOG.html'), 'w') as output_file:
        # Trim whitespace and write to file
        output_file.write(str(soup).strip())
