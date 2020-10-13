from firebase import firebase
import smtplib
from email.mime.text import MIMEText

firebase = firebase.FirebaseApplication('https://pickimageanduploadtofirebase.firebaseio.com/')
firealert=firebase.put('/FireAlert1/1','name','Fire has occured in Acropolis Technical Campus. Tap to know more.')
firealert=firebase.put('/FireAlert2/1','title','Emergency Fire Alert :-')
firealert=firebase.put('/FireAlert2/1','name','Fire has been occured in Acropolis Technical Campus. \nClick below link to know the location of this college \n\n')
firealert=firebase.put('/FireAlert2/1','map','https://goo.gl/maps/BTx2iZZ9SVimLjYe8')
print(firealert)

Receivers=[]
result=firebase.get('/Uploads/',None)
for x,y in result.items():
    for q,w in y.items():
        if q=='email':
            Receivers.append(w)

print(Receivers)

"""def send_email():
    sender = 'gokuthp97@gmail.com'
    port = 587
    user = 'gokuthp97@gmail.com'
    password = 'arunspower'
    
    server=smtplib.SMTP("smtp.gmail.com", port)
    server.starttls()
    server.login(user, password)
    msg = MIMEText('Emergency Alert :-\nFire has been occured in Acropolis Technical Campus. \nClick link to know the location of this college https://goo.gl/maps/BTx2iZZ9SVimLjYe8')
    msg['Subject'] = 'Fire Alert'
    msg['From'] = 'gokuthp97@gmail.com'
    
    for dest in Receivers:
        msg['To'] = dest
        server.sendmail(sender, dest, msg.as_string())
        print('mail successfully sent')
    server.quit()
send_email()"""

    
    



