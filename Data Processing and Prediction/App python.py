# -*- coding: utf-8 -*-
"""
Created on Fri Jan 17 19:07:39 2020

@author: Sarthak
"""

import numpy as np
from flask import Flask, request, jsonify, render_template
import pickle

import firebase_admin
from firebase_admin import credentials

cred = credentials.Certificate("C:/Users/Sarthak/Desktop/gsigntest-a4db7-firebase-adminsdk-dpmw3-d13e630ec6.json")
firebase_admin.initialize_app(cred)

app = Flask(__name__)
model = pickle.load(open('SepsisMean.pkl', 'rb'))

@app.route('/')
def home():
    return render_template('Frontend.html')

@app.route('/predict',methods=['POST'])
def predict():

    int_features = [int(x) for x in request.form.values()]
    final_features = np.array(int_features)
    final_features = final_features.reshape(1,6)
    print(final_features)
    prediction = model.predict(final_features)
    output = int(prediction[0])
    print(output)
                                                                                        
    return render_template('Frontend.html', prediction_text = 'Sepsis {}'.format(output))

@app.route('/results',methods=['POST'])
def results():

    data = request.get_json(force=True)
    prediction = model.predict([np.array(list(data.values()))])

    output = prediction[0]
    return jsonify(output)

if __name__ == "__main__":
    app.run(debug=True)