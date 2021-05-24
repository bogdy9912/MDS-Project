import unittest

def check_requirements(module_name):
    f = open("requiremets.txt", "r")
    file = f.read()
    package= file.split('\n')
    for p in package:
        package_name = p.split('==')[0]
        if (package_name == module_name):
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
