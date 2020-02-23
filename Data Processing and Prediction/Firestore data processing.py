# -*- coding: utf-8 -*-
"""
Created on Mon Jan 20 14:21:44 2020

@author: Sarthak
"""
import pickle
import numpy as np

class Patient(object):
    def __init__(self, age, output, temp, hr, gender, o2, rr):
        self.age = age
        self.output = output
        self.temp = temp
        self.hr = hr
        self.gender = gender
        self.o2 = o2
        self.rr = rr
        
    @staticmethod
    def from_dict(source):
        # [START_EXCLUDE]
        ptest = Patient(source[u'age'], source[u'output'], source[u'temp'],source[u'hr'],source[u'gender'],source[u'o2'],source[u'rr'])

        
        return ptest
        # [END_EXCLUDE]

    def to_dict(self):
        # [START_EXCLUDE]
        dest = {
            u'age': self.age,
            u'output': self.output,
            u'temp': self.temp,
            u'hr': self.hr,
            u'gender': self.gender,
            u'o2': self.o2,
            u'rr': self.rr
        }

        
        return dest
        # [END_EXCLUDE]

    def __repr__(self):
        return(
            u'Patient(age={}, output={}, temp={}, hr={}, gender={}, o2={}, rr={})'
            .format(self.age, self.output, self.temp, self.hr,
                    self.gender,self.o2,self.rr))
# [END custom_class_def]




import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("C:/Users/Sarthak/Desktop/gsigntest-a4db7-firebase-adminsdk-dpmw3-c1e74001e7.json")
app = firebase_admin.initialize_app(cred)

store = firestore.client()
doc_ref = store.collection(u'Results').document(u'testResult')

docs = doc_ref.get()
testp = Patient.from_dict(docs.to_dict())


    
model = pickle.load(open('SepsisMean.pkl', 'rb')) 
data = [testp.hr,testp.temp,testp.o2,int(testp.rr),testp.gender,testp.age]
findata = np.array(data)
findata = findata.reshape(1,6)
pred = model.predict(findata)
print(findata)
pred = int(pred)
print(pred)
doc_ref.set({
    u'output': pred
}, merge=True)

