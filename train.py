import os
import cv2
import numpy as np
from tqdm import tqdm

DATADIR = 'Dataset'
CATERGORIES = ['Fire','NoFire'] 


IMG_SIZE = 64
def create_training_data():
	training_data = []
	for category in CATERGORIES:
		path = os.path.join(DATADIR,category)
		class_num = CATERGORIES.index(category)
		print('path : ',path)
		for img in tqdm(os.listdir(path)):
			try:
				img_array = cv2.imread(os.path.join(path,img))
				new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
				# print('new_array ',new_array)
				training_data.append([new_array , class_num])
			except Exception as e:
				pass
	return training_data


training_data = create_training_data()
print('training_data: ',training_data)



#To check that data is loading properly with both categories 
import random

print(len(training_data))
random.shuffle(training_data)
for sample in training_data[:10]:
	print(sample[1])


X = []
Y = []

for features,label in training_data:
	X.append(features)
	Y.append(label)


X = np.array(X).reshape(-1,IMG_SIZE, IMG_SIZE,3)
X = X/255.0
print("Shape of sample image",X.shape[1:])


import tensorflow as tf 
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.models import Sequential 
from tensorflow.keras.layers import Dense, Dropout,Activation, Flatten, Conv2D, AveragePooling2D


model = Sequential()

model.add(Conv2D(filters=16, kernel_size = (3,3), activation = 'relu',input_shape = X.shape[1:]))
model.add(AveragePooling2D())
model.add(Dropout(0.5))

model.add(Conv2D(filters = 32, kernel_size = (3,3), activation = 'relu'))
model.add(AveragePooling2D())
model.add(Dropout(0.5))

model.add(Conv2D(filters = 64,kernel_size = (3,3),activation = 'relu'))
model.add(AveragePooling2D())
model.add(Dropout(0.5))

model.add(Flatten())

model.add(Dense(units = 256, activation = 'relu'))
model.add(Dropout(0.2))

model.add(Dense(units = 128 , activation = 'relu'))
model.add(Dense(units = 2,activation = 'softmax'))


model.compile(loss = 'sparse_categorical_crossentropy',optimizer = 'adam' , metrics = ['accuracy'])

IMG_SIZE = 64

X = np.array(X).reshape(-1 , IMG_SIZE,IMG_SIZE,3)
Y = np.array(Y)

history = model.fit(X,Y, batch_size = 16,epochs = 1000,validation_split = 0.3)

model.save('model_epochs-1000.h5')


from matplotlib import pyplot as plt 
plt.plot(history.history['accuracy'])
plt.plot(history.history['val_accuracy'])
plt.title('model accuracy')
plt.ylabel('accuracy')
plt.xlabel('epochs')
plt.legend(['train','validate'],loc = 'upper left')
plt.show()


plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('model loss')
plt.ylabel('loss')
plt.xlabel('epochs')
plt.legend(['train', 'validation'], loc = 'upper left')
plt.show()


















