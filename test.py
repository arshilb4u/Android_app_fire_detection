#Testing Code for Fire detection

import os
import cv2
import numpy as np 
from tqdm import tqdm
import matplotlib.pyplot as plt 
from sklearn.metrics import confusion_matrix 
import itertools 
import keras

DATADIR = 'Dataset_test'
CATEGORIES = ['Fire','NoFire']

IMG_SIZE = 64

def create_testing_data():
	testing_data = []
	for category in CATEGORIES:

		path = os.path.join(DATADIR,category)
		class_num = CATEGORIES.index(category)

		for img in tqdm(os.listdir(path)):
			try:
				img_array = cv2.imread(os.path.join(path,img))
				new_array = cv2.resize(img_array, (IMG_SIZE,IMG_SIZE))
				testing_data.append([new_array,class_num])
			except Exception as e:
				pass

	return testing_data


testing_data = create_testing_data()

import random
test_image_num = len(testing_data)

random.shuffle(testing_data)
test_label = np.zeros((test_image_num,1))

c = 0
for sample in testing_data:
	test_label[c] = (sample[1])
	c+=1
print(c)

actual_labels = (test_label.reshape(test_image_num,))
print(actual_labels.shape)
actual_labels.astype(int)

X = []
Y = []

for features,label in testing_data:
	X.append(features)
	Y.append(label)

X = np.array(X).reshape(-1,IMG_SIZE,IMG_SIZE,3)
X = X/255.0
print(X.shape[1:])
import tensorflow as tf
from keras.models import load_model
#model = load_model('model_epochs-1000.h5')
model = tf.keras.models.load_model('sd.h5')

predicted_labels = model.predict_classes(X)
predicted_labels = (predicted_labels.reshape(test_image_num,))
predicted_labels.astype(int)

def plot_confusion_matrix(cm,classes,normalize = False,title = 'Confusion_Matrix',cmap = plt.cm.Blues):

	if normalize:
		cm = cm.astype('float') / cm.sum(axis = 1)[:,np.newaxis]
		print("Normalized confusion matrix")
	else:
		print(" COnfusion matrix, without normalizarion")

	print(cm)

	plt.imshow(cm,interpolation = 'nearest', cmap = cmap)
	plt.title(title)
	plt.colorbar()
	tick_marks = np.arange(len(classes))
	plt.xticks(tick_marks,classes,rotation = 45)
	plt.yticks(tick_marks,classes)

	fmt = '.2f' if normalize else 'd'
	thresh = cm.max()/2.0
	for i,j in itertools.product(range(cm.shape[0]),range(cm.shape[1])):
		plt.text(j,i,format(cm[i,j],fmt),horizontalalignment = 'center',color = 'white' if cm[i,j] > thresh else 'black')

	plt.ylabel('True label')
	plt.xlabel('Prediction label')
	plt.tight_layout()

cm = confusion_matrix(actual_labels,predicted_labels)
cm_plot_labels = ['Fire','NoFire']
plot_confusion_matrix(cm, cm_plot_labels,title = 'Confusion Matrix')

tp = cm[0][0]
fn = cm[0][1]
fp = cm[1][0]
tn = cm[1][1]

print("tp",str(tp))
print("fn",str(fn))
print("fp",str(fp))
print("tn",str(tn))


Recall = tp/(tp + fn)
Precision = tp/(tp+fp)
f_measure = 2 * ((Precision * Recall)/(Precision+Recall))

print(Precision,Recall,f_measure)
X = np.array(X).reshape(-1 , IMG_SIZE,IMG_SIZE,3)
Y = np.array(Y)
model.evaluate(X,Y)