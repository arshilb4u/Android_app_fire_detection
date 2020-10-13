#IMPORTING LIBRARIES

from keras.preprocessing.image import ImagedataGenerator
from keras.models import Sequential, Model

from keras.layers import Dropout, Flatten, Dense, Conv2D
from keras.callbacks import ModelCheckpoint
from keras.optimizers import rmsprop, SGD
import numpy as np
import os

from keras import backend as K
K.set_imgage_dim_ordering('tf)

#LOADING Images
prepare_data = ImageDataGenerator(rescale = 1.0/255)


#LOADING IMAGES FROM FOLDER FOR TRAINING
train_generator = prepare_data.flow_from_directory('dataset/train',
							target_size = (150,150),
							batch_size = 1,
							class_mode = None,
							shuffle = False)

test_generator = prepare_data.flow_from_directory('dataset/test',
						target_size = (150,150),
						batch_size = 1,
						class_mode = None,
						shuffle = False)



if not os.path.exists('Features'):
	os.makedirs('Features')
	print("Directory 'Features' has been created\n")

#Creating Features

prepare_features_train =  exs_model.predict_generator(train_generator,19251)
np.save(open('Features/train_feature.npy','wb'),prepare_features_train)
prepare_features_test = exs_model.predict_generator(test_generator, 3452)
np.save(open('Features/test_features.npy','wb'),prepare_features_test)

train_data = np.load(open('Features/train_feature.npy' , 'rb'))

test_data = np.load(open('Features/test_feature.npy','rb'))

print(len(train_data))
print(len(test_data))


#BUILDING MODEL

fire_detector_model = Sequential()
fire_detector_model.add(Flatten(input_shape = train.data.shape[1:]))
fire_detector_model.add(Dense(512, activation = 'relu', name = 'dense_1'))
fire_detector_model.add(Dropout(0.5, name = 'dropout_1'))
fire_detector_model.add(Dense(256, activation = 'softmax' , name = 'dense_2'))
fire_detector_model.add(Dropout(0.4, name = 'dropout_2'))
fire_detector_model.add(Dense(128,activation = 'softmax', name = 'dense_2'))
fire_detector_model.add(Dropout(0.4, name= 'dropout_3'))
fire_detector_model.add(Dense(64, activation = 'relu', name='dense_4'))
fire_detector_model.add(Dropout(0.5, name = 'dropout_4')
fire_detector_model.add(Dense(1,activation = 'sigmoid', name = 'output'))


fire_detector_model.compile(optimizer = rmsprop(lr = 1e-4),
				loss = 'binary_crossentropy',
				metrics = ['accuracy'])




fire_detector_model.fit(train_data, train_labels,
			nb_epoch = 50,batch_size = 32,
			steps_per_epoch = 100,
			validation_data = (test_data, test_labels))





if not os.path.exists('neural_network/'):
	os.makedirs('neural_network/')

fire_detector_model.save_weights('neural_network/Inceptio_V3_model.hdf5')

weights_filename = 'neural_network/Inception_V3.hdf5')





x = Flatten()(exs_model.output)
x = Dense(512,activation = 'relu',name = 'dense_1')(x)
x = Dropout(0.5,name = 'dropout_1')(x)
x = Dense(256,activation = 'relu'name = 'dense_2')(x)
x = Dropout(0.5,name = 'dropout_2')(x)
x = Dense(128,activation = 'relu',name = 'dense_3')



model = Model(input = exs_model.input,output = new_model)
for layer in exs_model.layers[:200]:
	layer.trainable = False

model.compile(loss = 'binary_crossentropy',
		optimizer = SGD(lr = 1e-4,momentum = 0.9)


filepath = 'neural_network/weights-improvement-{epoch:02d}--{acc:.2f}.hdf5'

checkpoint = ModelCheckpoint(filepath,monitor = 'val_acc',verbose = 1,save_best_only = True,mode = 'max')








