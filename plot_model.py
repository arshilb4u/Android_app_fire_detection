import tensorflow as tf 
from tensorflow.keras.utils import plot_model

model = tf.keras.models.load_model('model.h5')

plot_model(model, to_file = 'model.svg',show_layer_names = False, show_shapes = True)