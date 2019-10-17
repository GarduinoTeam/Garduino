import tensorflow as tf
from keras.models import load_model
from keras.preprocessing import image
import numpy as np

graph = tf.get_default_graph()
model = load_model('AlexNetModel.hdf5')

output_dict = {
	'Apple_AppleScab': 0,
	'Apple_BlackRot': 1,
	'Apple_CedarAppleRust': 2,
	'Apple_Healthy': 3,
	'Blueberry_Healthy': 4,
	'Cherry_PowderyMildew': 5,
	'Cherry_Healthy': 6,
	'Corn_CercosporaLeaf_spot GrayLeafSpot': 7,
	'Corn_CommonRust': 8,
	'Corn_NorthernLeafBlight': 9,
	'Corn_Healthy': 10,
	'Grape_BlackRot': 11,
	'Grape_Esca(BlackMeasles)': 12,
	'Grape_LeafBlight(IsariopsisLeafSpot)': 13,
	'Grape_Healthy': 14,
	'Orange_Haunglongbing(CitrusGreening)': 15,
	'Peach_BacterialSpot': 16,
	'Peach_Healthy': 17,
	'Pepper,bell_BacterialSpot': 18,
	'Pepper,bell_Healthy': 19,
	'Potato_EarlyBlight': 20,
	'Potato_LateBlight': 21,
	'Potato_Healthy': 22,
	'Raspberry_Healthy': 23,
	'Soybean_Healthy': 24,
	'Squash_PowderyMildew': 25,
	'Strawberry_LeafScorch': 26,
	'Strawberry_Healthy': 27,
	'Tomato_BacterialSpot': 28,
	'Tomato_EarlyBlight': 29,
	'Tomato_LateBlight': 30,
	'Tomato_LeafMold': 31,
	'Tomato_Septoria_leaf_spot': 32,
	'Tomato_SpiderMites Two-spottedSpiderMite': 33,
	'Tomato_TargetSpot': 34,
	'Tomato_TomatoYellowLeafCurlVirus': 35,
	'Tomato_TomatoMosaicvirus': 36,
	'Tomato_Healthy': 37
}

output_list = list(output_dict.keys())

#plant_path = 'plant.jpeg'
plant_path = 'tomato_bact.jpg'

img = image.load_img(plant_path, target_size = (224, 224))
img = image.img_to_array(img)
img = np.expand_dims(img, axis = 0)
img = img / 255

with graph.as_default():
    prediction = model.predict(img)

prediction_flatten = prediction.flatten()
max_val_index = np.argmax(prediction_flatten)
result = output_list[max_val_index]

print( ({ 'result' : result }) )