# -*- coding: utf-8 -*-
__author__ = 'mian'
from sklearn import linear_model
import numpy as np
import pandas as pd
import datetime
import time
import csv
from sklearn.feature_extraction import DictVectorizer, FeatureHasher
from numpy import genfromtxt  # 可以将非array格式的list转化为array

df = pd.read_csv("data.csv")
#deliverData = genfromtxt(dataPath, delimiter=",")  # 将csv文件转化为numpy.array格式

#print "data:", df

#df['installTime'] = df['installTime'].apply(lambda x: time.mktime(datetime.datetime.strptime(x, "%Y/%m/%d").date().timetuple()))
#df['uninstallTime'] = df['uninstallTime'].apply(lambda x: time.mktime(datetime.datetime.strptime(x, "%Y/%m/%d").date().timetuple()))
del df['installTime']
del df['uninstallTime']

X = df.iloc[:, 0:-1]
Y = df.iloc[:, -1]

dict_X = X.to_dict(orient='records')
vec = DictVectorizer()
X_train = vec.fit_transform(dict_X).toarray()
csvfile = file('data_result.csv', 'wb')
writer = csv.writer(csvfile)
writer.writerow(vec.feature_names_)
for word in X_train:
    writer.writerow(word.tolist())
# with open('data_result.csv', 'wb') as csvfile:
#     for word in X_train:
#         a = ''
#         for aa in word.tolist():
#             print str(aa)
#             a += ' ' + str(aa)
#         spamwriter = csv.writer(csvfile, dialect='excel')
#         spamwriter.writerow(a)
print X_train
#print "X:", X
print "Y:", Y

regression = linear_model.LinearRegression()
regression.fit(X_train, Y)

#print "coefficients:", regression.coef_  # 与X结合的值
#print "intercept:", regression.intercept_  # 类似于截距

df_pre = pd.read_csv("data1.csv")
#df_pre['installTime'] = df_pre['installTime'].apply(lambda x: time.mktime(datetime.datetime.strptime(x, "%Y/%m/%d").date().timetuple()))
#df_pre['uninstallTime'] = df_pre['uninstallTime'].apply(lambda x: time.mktime(datetime.datetime.strptime(x, "%Y/%m/%d").date().timetuple()))
del df_pre['installTime']
del df_pre['uninstallTime']
del df_pre['rate']
dict_X_data = df_pre.to_dict(orient='records')
x_pre = vec.transform(dict_X_data).toarray()
print X_train[0:1, :] == x_pre[0:1, :]
y_pre = regression.predict(x_pre)
print "Y-Predict:", y_pre
