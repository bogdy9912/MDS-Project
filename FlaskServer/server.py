from flask import Flask, request
import tensorflow as tf
import cv2
import os
import numpy as np
from sklearn.preprocessing import MinMaxScaler

app = Flask(__name__)

net = cv2.dnn.readNetFromTensorflow('graph_opt.pb')
model = tf.keras.models.load_model('saved_models\openpose_bicepscurl_nobg_sigmoid')

scaler = MinMaxScaler()
IMG_SIZE = 256
BODY_PARTS = { "Nose": 0, "Neck": 1, "RShoulder": 2, "RElbow": 3, "RWrist": 4,
               "LShoulder": 5, "LElbow": 6, "LWrist": 7, "RHip": 8, "RKnee": 9,
               "RAnkle": 10, "LHip": 11, "LKnee": 12, "LAnkle": 13, "REye": 14,
               "LEye": 15, "REar": 16, "LEar": 17, "Background": 18 }

def get_openpose_data(frame, thr=0.2):
    """Function to run the openpose model over an image and get the coordinates for all 19 keypoints.
    
    Arguments:
        frame {list} -- The image that the openpose model is run over.
    
    Returns:
        list -- A list that consists of coordinates of the keypoints.
    """
    frameWidth = frame.shape[1]
    frameHeight = frame.shape[0]
    
    net.setInput(cv2.dnn.blobFromImage(frame, 1.0, (IMG_SIZE, IMG_SIZE), (127.5, 127.5, 127.5), swapRB=True, crop=False))
    out = net.forward()
    out = out[:, :19, :, :]

    assert(len(BODY_PARTS) == out.shape[1])

    points = []
    for i in range(len(BODY_PARTS)):
        heatMap = out[0, i, :, :]

        _, conf, _, point = cv2.minMaxLoc(heatMap)
        x = (frameWidth * point[0]) / out.shape[3]
        y = (frameHeight * point[1]) / out.shape[2]

        points.append([int(x), int(y)] if conf > thr else None)

    return points


def process_openpose_data(data, scaler, fit=True):
    """Functions that is processing the data output of openpose.

    Arguments:
        data {list} -- List of keypoints.
        scaler {object} -- Scikit-learn scaler.
        fit {boolean} -- Boolean that tells the function to fit the data before transforming.

    Returns:
        {numpy.array} -- Processed data.
    """
    x_data = []

    for image in data:
        openpose_data = get_openpose_data(image)
        for idx, coords in enumerate(openpose_data):
            if coords == None:
                openpose_data[idx] = [0, 0]
        x_data.append(openpose_data[1:5])
    
    x_data = np.array(x_data).astype(float)
    # Reshape so we can fit the scaler on our data.
    x_data = x_data.reshape(x_data.shape[0], x_data.shape[1] * 2)
    
    # Normalize the data
    if fit == True:
        scaler.fit(x_data)
    x_data = scaler.transform(x_data)
    return x_data


def fragment_video(video_name, save_location, interval):
    """Function to fragment video into frames at a given interval of time
        and saves the in a given directory.

    Arguments:
        video_name {string} -- Path to the video.
        save_location {string} -- Path to the save location.
        interval {integer} -- Interval in ms.
    """
    vidcap = cv2.VideoCapture(video_name)
    success, image = vidcap.read()

    # Change the current directory if we are not already there.
    cwd = os.getcwd()
    if cwd != save_location:
        os.chdir(save_location)

    # While we have frames, we read and save them.
    count = 1
    while success:
        vidcap.set(cv2.CAP_PROP_POS_MSEC, (count * interval))
        cv2.imwrite(f'frame{count}.jpg', image)
        success, image = vidcap.read()
        count += 1

    # Change the directory back.
    os.chdir(cwd)


def read_frames(location):
    """Functions that reads the video frames from a director.

    Arguments:
        location {string} -- Path to the directory where the frames are located at.

    Returns:
        {list} -- Resized video frames.
    """
    frames = []
    directory = os.listdir(location)

    for frame in directory:
        path = os.path.join(location, frame)
        frame = cv2.resize(cv2.imread(path, cv2.IMREAD_COLOR), (IMG_SIZE, IMG_SIZE))
        frames.append(frame)
    return np.array(frames).astype(np.float32)


def delete_frames(location):
    """Functions that deletes the frames that we generated to free the memory.

    Arguments:
        location {string} -- Path to the directory where the frames are located at.
    """
    directory = os.listdir(location)

    for frame in directory:
        path = os.path.join(location, frame)
        os.remove(path)


@app.route('/', methods=['POST', 'GET'])
def function():

    if request.method == 'GET':
        
        """
        if "video" in request.files:
            video = request.files["video"]
            filename = secure_filename(file.filename) # Secure the filename to prevent some kind of attack
            media.save(video, name=filename)
        """

        # Fragment the video into frames
        fragment_video('videos\\1.mp4', 'frames', 250)

        # Read the frames
        input_data = read_frames('frames')

        # Delete the frames
        delete_frames('frames')

        # Run the openpose model over the frames
        x_valid_video = process_openpose_data(input_data, scaler, fit=True)

        # Run our model on the preprocessed data
        output = model.predict(x_valid_video)
        output = output.round()

        # Get a score
        good = 0
        for o in output:
            if o == 1:
                good += 1

        score = good / len(output) * 100

    return f'Biceps curl: {round(score, 2)}% correct'

if __name__ == '__main__':
    app.run(debug=True)