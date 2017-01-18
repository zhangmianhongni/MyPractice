# -*- coding: utf-8 -*-
import sys
import csv
import jieba.analyse
import pandas as pd
import numpy as np
from sklearn import svm
reload(sys)
sys.setdefaultencoding("utf-8")

words = []
wordFreqs = {}
df = pd.DataFrame()

with open('Text\words_complete.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile, dialect='excel')
    for line in reader:
        lineStr = ', '.join(line).decode('gb2312')
        word = lineStr.split(',')[0]
        words.append(word)

for i in range(1, 120 + 1):
    text = open('Text\\' + str(i) + '.txt', "r").read()
    zidian = {}
    fenci = jieba.cut(text)

    for fc in fenci:
        if fc in words:
            if fc in zidian:
                zidian[fc] += 1
            else:
                zidian[fc] = 1

    for w in words:
        if w not in zidian:
            zidian[w] = 0

    wordFreqs[str(i)] = zidian

for w in words:
    serList = []
    for i in range(1, 120 + 1):
        serList.append(wordFreqs[str(i)][w])
    s = pd.Series(serList)
    df[w] = s

dfF = df[:80]
dfE = df[80:]
print len(df[u'如今'])
print len(dfF[u'如今'])
print len(dfE[u'如今'])

samplerF = np.random.permutation(80)[:15]
trainDfF = dfF.take(samplerF)
print len(trainDfF[u'如今'])
trainTargetF = [0 for i in range(15)]
print trainTargetF

samplerE = np.random.permutation(40)[:15]
trainDfE = dfE.take(samplerE)
print len(trainDfE[u'如今'])
trainTargetE = [1 for i in range(15)]
print trainTargetE

trainDf = pd.concat([trainDfF, trainDfE])
trainTargetF.extend(trainTargetE)

print len(trainDf[u'如今'])
print trainTargetF

clf = svm.LinearSVC()
clf.fit(trainDf, trainTargetF)

predict = clf.predict(df)
print predict



