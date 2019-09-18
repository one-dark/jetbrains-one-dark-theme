import os
import unittest

THEME_DIR = 'src/main/resources/themes'
META_DIR = 'src/main/resources/META-INF'
BUILD_DIR = 'build'


class TestFiles(unittest.TestCase):
    @staticmethod
    def file_exists(file_path):
        full_path = os.path.normpath('%s/../../%s' % (os.path.dirname(__file__), file_path))

        return os.path.exists(full_path) and os.path.isfile(full_path)

    def test_theme_files(self):
        # Normal
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark.theme.json'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark.xml'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_italic.theme.json'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_italic.xml'))

        # Vivid
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_vivid.theme.json'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_vivid.xml'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_vivid_italic.theme.json'))
        self.assertTrue(self.file_exists(THEME_DIR + '/one_dark_vivid_italic.xml'))

    def test_meta_files(self):
        self.assertTrue(self.file_exists(META_DIR + '/plugin.xml'))
        self.assertTrue(self.file_exists(META_DIR + '/pluginIcon.svg'))

    def test_build_files(self):
        self.assertTrue(self.file_exists(BUILD_DIR + '/README.html'))
        self.assertTrue(self.file_exists(BUILD_DIR + '/CHANGELOG.html'))


if __name__ == '__main__':
    unittest.main()
