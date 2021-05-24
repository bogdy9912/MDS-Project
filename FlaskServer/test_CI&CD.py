import unittest
from typing import List


def check_requirements(module_name) -> object:
    """

    Args:
        module_name (String):
    """
    f = open("FlaskServer/requirements.txt", "r")
    file = f.read()

    package: List[str] = file.split('\n')
    for p in package:
        package_name: str = p.split('==')[0]
        if package_name == module_name:
            return True
    return False


class MyTestCase(unittest.TestCase):
    def test_requiremts(self):
        self.assertEqual(check_requirements('Flask'), True)
        self.assertEqual(check_requirements('tensorflow'), True)
        self.assertEqual(check_requirements('scikit-learn'), True)
        pass


if __name__ == '__main__':
    unittest.main()
