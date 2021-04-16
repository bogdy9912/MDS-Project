import json

def parse_config_file(location):
    with open(location, 'r') as f:
        model_json = json.load(f)
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
                return False

    # Checking the Layers key
    if 'Layers' not in configs.keys():
        return False
    
    for idx, layer in enumerate(configs["Layers"]):
        if layer['type'] == None or layer['neurons'] == None or \
            layer['activation'] == None:
            return False

        if type(layer['type']) != str or type(layer['neurons']) != int or \
            type(layer['activation']) != str:
                return False
        
        if layer['type'] != 'dense' and layer['type'] != 'conv2d': 
            return False

        if layer['activation'] != 'relu' and layer['activation'] != 'sigmoid':
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
    
    # Checking the Metrics key
    if 'Metrics' not in configs.keys():
        return False

    return True


if __name__ == '__main__':
    json = parse_config_file('model_config_files\model_4dense_bc_4keypoints.hjson')
    print(validate_config(json))