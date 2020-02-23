# -*- coding: utf-8 -*-
"""
Created on Tue Jan 21 00:45:57 2020

@author: Sarthak
"""

from zipfile import ZipFile
import pandas as pd
import numpy as np
from glob import glob

filenames=glob('training/*.psv')
li = []
for j,f in enumerate(filenames):
        flag=0
        df=pd.read_csv(f , sep = '|')    
        for i,row in enumerate(df.itertuples()):
        
            if row.SepsisLabel == 1:
                df1 = df.iloc[:i+1]
                df1.to_csv(r'C:\Users\Sarthak\Desktop\doc data\validation_sepsis_doc{}'.format(str(j) + '.csv'), index = None, header=True)
                flag=1
                break
        if flag==0:
            df.to_csv(r'C:\Users\Sarthak\Desktop\doc data\validation_sepsis_doc{}'.format(str(j) + '.csv'), index = None, header=True)


#missing = (train.isnull().sum() / train.shape[0]) * 100
#selected_cols = list(missing[missing < 90].index)


