# -*- coding: utf-8 -*-
"""
Created on Wed Jan 15 21:27:12 2020

@author: Sarthak
"""

from zipfile import ZipFile
import pandas as pd
import numpy as np
from glob import glob

filenames=glob('training_setB/*.psv')
li = []

patients = pd.DataFrame(columns = ['mhr','mt','mo2','mrr','Gender','Age','Sepsis'])
for i,f in enumerate(filenames):
    if i > 10000 and i<17500:
        mehr,met,meo2,merr,age,gender,k,h,t,o,r=0,0,0,0,0,0,0,0,0,0,0
        df=pd.read_csv(f , sep = '|')
        df=df.fillna(0)
        age=float(df.iloc[0][34])
        gender=df.iloc[0][35]
        for row in df.itertuples():
            
            if row.SepsisLabel == 1 :
                k=k+1
                if row.HR>0: h=h+1 
                if row.Temp>0: t=t+1
                if row.O2Sat>0: o=o+1
                if row.Resp>0: r=r+1
                mehr+=row.HR
                met+=row.Temp
                meo2+=row.O2Sat
                merr+=row.Resp
        if h!=0: mehr=mehr/h
        if t!=0: met=met/t
        if o!=0: meo2=meo2/o
        if r!=0: merr=merr/r
        if k>1:
            k=1
        df=df.replace(0,np.nan)
        lsn=[df.loc[:]['HR'].mean(skipna=True),df.loc[:]['Temp'].mean(skipna=True),df.loc[:]['O2Sat'].mean(skipna=True),df.loc[:]['Resp'].mean(skipna=True),df.iloc[0][35],df.iloc[0][34],0]
        ls=[mehr,met,meo2,merr,gender,age,k]
        if k==0:
            newRow = pd.DataFrame(np.array(lsn).reshape(1,7), columns = list(patients.columns))
            patients=patients.append(newRow,ignore_index=True)
        else:
            newRow = pd.DataFrame(np.array(ls).reshape(1,7), columns = list(patients.columns))
            patients=patients.append(newRow,ignore_index=True)
patients=patients.fillna(0)
patients.to_csv(r'C:\Users\Sarthak\Desktop\temporal_valmean_sepsis.csv', index = None, header=True)
