from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array
import numpy as np 
import cv2
import urllib.request
import time

model = load_model("sd.h5")
url='http://192.168.0.100:8080/shot.jpg'
time.sleep(2)
IMG_SIZE = 64

while True:
    imgResp=urllib.request.urlopen(url)
    imgNp=np.array(bytearray(imgResp.read()),dtype=np.uint8)
    image=cv2.imdecode(imgNp,-1)
    image_small = cv2.resize(image, (IMG_SIZE,IMG_SIZE))
    image_small = image_small.astype("float") / 255.0
    image_small = img_to_array(image_small)
    image_small = np.expand_dims(image_small,axis = 0)
    # print(image.shape)
    tic = time.time()
    fire_prob = model.predict(image_small)[0][0] * 100
    toc = time.time()

    print("Time taken = ",toc - tic)
    print("FPS",1/np.float64(toc-tic))
    print("Fire Prob",fire_prob )
    print("Predictions" ,model.predict(image_small))
    print(image.shape)
 
    label = "Fire Prob" + str(fire_prob)
    cv2.putText(image,label,(10,25), cv2.FONT_HERSHEY_SIMPLEX,0.7,(0,255,0),2)
    #image = cv2.resize(image, (512,512))
    cv2.imshow("oUTPUT", image)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

end = time.time()

cap.release()
cv2.destroyAllWindows()
