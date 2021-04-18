import hjson
import unittest

def parse_config_file(location):
    with open(location, 'r') as f:
        model_json = hjson.loads(f.read())
    return model_json

def validate_config(configs):
    
    # Checking the Input key
    if 'Input' not in configs.keys():
        return False
    
    if configs['Input']['shape'] == None or \
        configs['Input']['flatten'] == None:
        return False
    
    if type(configs['Input']['shape']) != int or \
        type(configs['Input']['flatten']) != bool:
            raise ValueError('Input shape or flatten is of a wrong data type.')

    # Checking the Layers key
    if 'Layers' not in configs.keys():
        return False
    
    for idx, layer in enumerate(configs["Layers"]):
        if layer['type'] == None or layer['neurons'] == None or \
            layer['activation'] == None:
            return False

        if type(layer['type']) != str or type(layer['neurons']) != int or \
            type(layer['activation']) != str:
                raise ValueError(f'On Layer {idx}, type, neurons or activation is of a wrong data type')
        
        if layer['type'] != 'dense' and layer['type'] != 'conv2d': 
            return False

        if layer['activation'] != 'relu' and layer['activation'] != 'sigmoid' and \
            layer['activation'] != 'softmax':
            return False
    
    # Checking the Optimizer key
    if 'Optimizer' not in configs.keys():
        return False

    if configs['Optimizer']['name'] == None or \
        configs['Optimizer']['learning_rate'] == None:
        return False

    if type(configs['Optimizer']['name']) != str or \
        type(configs['Optimizer']['learning_rate']) != float:
        return False

    if configs['Optimizer']['name'] != 'Adam' and configs['Optimizer']['name'] != 'SGD':
        return False

    # Checking the Loss_function key
    if 'Loss_function' not in configs.keys():
        return False

    if configs['Loss_function']['name'] == None:
        return False
    
    if type(configs['Loss_function']['name']) != str:
        return False

    if configs['Loss_function']['name'] != 'binary_crossentropy' and configs['Loss_function']['name'] != 'mean_squared_error':
        return False
    
    # Checking the Metrics key
    if 'Metrics' not in configs.keys():
        return False

    return True


# Unit testing class
class TestValidate(unittest.TestCase):

    # TODO write more config files and test them
    def test_validate_config(self):
        # False
        json1 = parse_config_file('config files to be tested\config1_False.hjson')
        self.assertEqual(validate_config(json1), False)

        json2 = parse_config_file('config files to be tested\config2_False.hjson')
        self.assertEqual(validate_config(json2), False)

        json3 = parse_config_file('config files to be tested\config3_False.hjson')
        self.assertEqual(validate_config(json3), False)

        json4 = parse_config_file('config files to be tested\config4_False.hjson')
        self.assertEqual(validate_config(json4), False)

        json5 = parse_config_file('config files to be tested\config5_False.hjson')
        self.assertEqual(validate_config(json5), False)

        # True
        json1 = parse_config_file('config files to be tested\config1_True.hjson')
        self.assertEqual(validate_config(json1), True)

        json2 = parse_config_file('config files to be tested\config2_True.hjson')
        self.assertEqual(validate_config(json2), True)

        json3 = parse_config_file('config files to be tested\config3_True.hjson')
        self.assertEqual(validate_config(json3), True)

        json4 = parse_config_file('config files to be tested\config4_True.hjson')
        self.assertEqual(validate_config(json4), True)

        json5 = parse_config_file('config files to be tested\config5_True.hjson')
        self.assertEqual(validate_config(json5), True)

if __name__ == '__main__':
    unittest.main()