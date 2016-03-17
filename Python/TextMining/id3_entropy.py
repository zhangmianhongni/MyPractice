#-- coding: UTF-8 --#
from math import  log
import operator


#计算熵
def calcShannonEnt(dataSet):
    numEntries = len(dataSet)
    labelCounts = {}
    for featVec in dataSet:
        currentLabel = featVec[-1]
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] =  0
        labelCounts[currentLabel] +=  1
    shannonEnt = 0.0
    for key in labelCounts:
        prob = float (labelCounts[key]) / numEntries #当前数据每个分类的比例
        shannonEnt  -= prob * log(prob, 2)
    return shannonEnt

#创建数据集
def createDataSet():
    dataSet = [['sunny', 'hot', 'high', 'FALSE', 'no'], ['sunny', 'hot', 'high', 'TRUE', 'no'], ['overcast', 'hot', 'high', 'FALSE', 'yes'],
                ['rainy', 'mild', 'high', 'FALSE', 'yes'], ['rainy', 'cool', 'normal', 'FALSE', 'yes'], ['rainy', 'cool', 'normal', 'TRUE', 'no'],
                ['overcast', 'cool', 'normal', 'TRUE', 'yes'], ['sunny', 'mild', 'high', 'FALSE', 'no'], ['sunny', 'cool', 'normal', 'FALSE', 'yes'],
                ['rainy', 'mild', 'normal', 'FALSE', 'yes'], ['sunny', 'mild', 'normal', 'TRUE', 'yes'], ['overcast', 'mild', 'high', 'TRUE', 'yes'],
                ['overcast', 'hot', 'normal', 'FALSE', 'yes'], ['rainy', 'mild', 'high', 'TRUE', 'no']]
    labels = ['outlook', 'temperature', 'humidity', 'windy']
    return dataSet, labels

# 离散特征分割 根据特征值分割数据集，axis是属性索引，value是属性对应的值
def splitDataSet(dataSet, axis, value):
    retDataSet = []
    for featVec in dataSet:
        if  featVec[axis] == value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            retDataSet.append(reducedFeatVec)
    return retDataSet

#根据信息增益选择最好的属性
def chooseBestFeatureToSplit(dataSet):
    numFeatures = len(dataSet[0]) - 1
    baseEntropy = calcShannonEnt(dataSet) #信息熵
    print("baseEntropy: " + str(baseEntropy))
    bestInfoGain = 0.0
    bestFeature = -1
    for i in range(numFeatures):
        featList = [example[i] for example in dataSet]
        uniqueVals = set(featList)
        print(uniqueVals)
        newEntropy = 0.0 #条件熵
        #splitEntropy = 0.0 #分裂信息熵
        for value in uniqueVals: #计算离散值的条件熵
            subDataSet = splitDataSet(dataSet, i, value)
            prob = len(subDataSet) / float(len(dataSet))
            newEntropy += prob * calcShannonEnt(subDataSet)
            #splitEntropy -= prob * log(prob, 2)
        print(i)
        print("newEntropy: " + str(newEntropy))
        #print("splitEntropy: " + str(splitEntropy))
        infoGain = baseEntropy - newEntropy #信息增益，使用信息增益有一个缺点，那就是它偏向于具有大量值的特征，所以C4.5选用信息增益率
        #infoGainRatio = infoGain / splitEntropy
        print("infoGain: " + str(infoGain))
        #print("infoGainRatio: " + str(infoGainRatio))
        if infoGain > bestInfoGain: #选择最大信息增益率的特征作为树节点
            bestInfoGain = infoGain
            bestFeature = i
    return bestFeature #返回节点的特征索引

'''
如果数据集已经处理了所有属性，但类标签依然不唯一，此时我们需要决定如何定该叶子节点，
这种情况我们通常会采用表决的方法决定该叶子节点的分类 。
'''
def majorityCnt(classList):
    classCount={}
    for vote in classList:
        if vote not in classCount.keys ():classCount[vote] = 0
        classCount[vote] += 1
    sortedClassCount = sorted(classCount.iteritems(), key=operator.itemgetter(1), reverse=True)
    print(sortedClassCount)
    return sortedClassCount[0][0]

#创建决策树
def createTree(dataSet, labels):
    classList = [example[-1]  for example in dataSet]
    if classList.count (classList [0] ) == len(classList): #如果所有的数据都是同一个分类，返回分类标签
        return classList[0]
    if  len(dataSet[0]) == 1: #如果数据集已经处理了所有属性，但类标签依然不唯一，执行多数表决
        return majorityCnt(classList)
    bestFeat = chooseBestFeatureToSplit(dataSet)
    bestFeatLabel = labels[bestFeat]
    myTree = {bestFeatLabel:{}}
    del(labels[bestFeat]) #删除已处理的节点名字
    featValues = [example[bestFeat] for example in dataSet]
    uniqueVals = set(featValues)
    subLabels = labels[:]
    for value in uniqueVals:
        myTree[bestFeatLabel][value] = createTree(splitDataSet(dataSet, bestFeat, value), subLabels) #递归创建树节点
    return myTree

#利用决策树进行分类
def classify(inputTree, featLabels, testVec):
    firstStr = inputTree.keys()[0]
    secondDict = inputTree[firstStr]
    featIndex = featLabels.index(firstStr)
    for key in secondDict.keys():
        if testVec[featIndex] == key:
            if type(secondDict[key]).__name__ == 'dict':
                classLabel  = classify(secondDict[key], featLabels, testVec)
            else:
                classLabel  =  secondDict[key]
    return classLabel


if __name__=="__main__":
    myData, labels = createDataSet()
    labelsBackup = labels[:]
    tree = createTree(myData, labels)
    print(tree)

    print(classify(tree, labelsBackup, ['rainy', 'mild', 'high', 'FALSE']))