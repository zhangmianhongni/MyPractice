#-*- coding: UTF-8 -*-
from math import log
import operator


#计算熵
def calcShannonEnt(dataSet):
    numEntries = len(dataSet)
    labelCounts = {}
    for featVec in dataSet:
        currentLabel = featVec[-1]
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] = 0
        labelCounts[currentLabel] += 1
    shannonEnt = 0.0
    for key in labelCounts:
        prob = float(labelCounts[key]) / numEntries
        shannonEnt -= prob * log(prob, 2)
    return shannonEnt

#创建数据集
def createDataSet():
    dataSet = [['sunny', 85, 85, 'FALSE', 'no'], ['sunny', 80, 90, 'TRUE', 'no'], ['overcast', 83, 86, 'FALSE', 'yes'],
                ['rainy', 70, 96, 'FALSE', 'yes'], ['rainy', 68, 80, 'FALSE', 'yes'], ['rainy', 65, 70, 'TRUE', 'no'],
                ['overcast', 64, 65, 'TRUE', 'yes'], ['sunny', 72, 95, 'FALSE', 'no'], ['sunny', 69, 70, 'FALSE', 'yes'],
                ['rainy', 75, 80, 'FALSE', 'yes'], ['sunny', 75, 70, 'TRUE', 'yes'], ['overcast', 72, 90, 'TRUE', 'yes'],
                ['overcast', 81, 75, 'FALSE', 'yes'], ['rainy', 71, 91, 'TRUE', 'no']]
    labels = ['outlook', 'temperature', 'humidity', 'windy', 'play']
    attrTypes = ['FALSE', 'TRUE', 'TRUE', 'FALSE']
    return dataSet, labels, attrTypes

#根据数据集分割数据集，axis是属性索引，value是属性对应的值
def splitDataSet(dataSet, axis, value):
    retDataSet = []
    for featVec in dataSet:
        if featVec[axis] == value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            retDataSet.append(reducedFeatVec)
    return retDataSet

def binSplitDataSet(dataSet, axis, value):
    leftDataSet = []
    rightDataSet = []
    for featVec in dataSet:
        if featVec[axis] < value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            leftDataSet.append(reducedFeatVec)
        else:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            rightDataSet.append(reducedFeatVec)

    return leftDataSet, rightDataSet

#根据信息增益选择最好的属性
def chooseBestFeatureToSplit(dataSet, attrTypes):
    numFeatures = len(dataSet[0]) - 1
    baseEntropy = calcShannonEnt(dataSet)
    print("baseEntropy: " + str(baseEntropy))
    bestInfoGain = 0.0
    bestFeature  = -1
    for i in range(numFeatures):
        featList = [example[i] for example in dataSet]
        newEntropy = 0.0
        splitEntropy = 0.0
        print(i)
        if attrTypes[i] == "TRUE":
            sortDataSet = sorted(dataSet, key=lambda x: x[i])
            sortFeatList = [example[i] for example in sortDataSet]
            numDatas = len(sortDataSet)
            lastCategory = ''
            splitNums = []
            for num in range(numDatas):
                curCategory = sortDataSet[num][-1]
                if curCategory != lastCategory and num != 0:
                    lastCategory = curCategory
                    splitNums.append(num)
            bestSubInfoGain = 0.0
            bestSubEntropy = 0.0
            bestSubSplitEntropy = 0.0
            bestSplitNum = -1
            for splitNum in splitNums:
                subEntropy = 0.0
                subSplitEntropy = 0.0
                value = float(sortFeatList[splitNum - 1] + sortFeatList[splitNum]) / 2
                leftDataSet, rightDataSet = binSplitDataSet(sortDataSet, i, value)

                prob = len(leftDataSet) / float(len(dataSet))
                subEntropy += prob * calcShannonEnt(leftDataSet)
                subSplitEntropy -= prob * log(prob, 2)

                prob = len(rightDataSet) / float(len(dataSet))
                subEntropy += prob * calcShannonEnt(rightDataSet)
                subSplitEntropy -= prob * log(prob, 2)

                subInfoGain = baseEntropy - subEntropy
                if subInfoGain > bestSubInfoGain:
                    bestSubInfoGain = subInfoGain
                    bestSubEntropy = subEntropy
                    bestSubSplitEntropy = subSplitEntropy
                    bestSplitNum = splitNum
                print("subInfoGain: " + str(subInfoGain))
                print("subSplitEntropy: " + str(subSplitEntropy))
            print("bestSplitNum: " + str(bestSplitNum))
            print("bestSubEntropy: " + str(bestSubEntropy))
            print("bestSubSplitEntropy: " + str(bestSubSplitEntropy))
            newEntropy = bestSubEntropy
            splitEntropy = bestSubSplitEntropy
        else:
            uniqueVals = set(featList)
            print(uniqueVals)
            for value in uniqueVals:
                subDataSet = splitDataSet(dataSet, i, value)
                prob = len(subDataSet) / float(len(dataSet))
                newEntropy += prob * calcShannonEnt(subDataSet)
                splitEntropy -= prob * log(prob, 2)

        print("newEntropy: " + str(newEntropy))
        print("splitEntropy: " + str(splitEntropy))
        infoGain = baseEntropy - newEntropy
        infoGainRatio = infoGain / splitEntropy
        print("infoGain: " + str(infoGain))
        print("infoGainRatio: " + str(infoGainRatio))
        if infoGainRatio > bestInfoGain:
            bestInfoGain = infoGainRatio
            bestFeature = i
    return bestFeature

'''
如果数据集已经处理了所有属性，但类标签依然不唯一，此时我们需要决定如何定该叶子节点，
这种情况我们通常会采用表决的方法决定该叶子节点的分类 。
'''
def majorityCnt(classList):
    classCount = {}
    for vote in classList:
        if vote not in classCount.keys():
            classCount[vote] = 0
        classCount[vote] += 1
    sortedClassCount = sorted(classCount.iteritems(), key=operator.itemgetter(1), reverse=True)
    print(sortedClassCount)
    return sortedClassCount[0][0]

#创建决策树
def createTree(dataSet, labels, attrTypes):
    classList = [example[-1] for example in dataSet]
    if classList.count(classList[0]) == len(classList):
        return classList[0]
    if len(dataSet[0]) == 1:
        return majorityCnt(classList)
    bestFeat = chooseBestFeatureToSplit(dataSet, attrTypes)
    bestFeatLabel = labels[bestFeat]
    myTree = {bestFeatLabel: {}}
    del(labels[bestFeat])
    del(attrTypes[bestFeat])
    featValues = [example[bestFeat] for example in dataSet]
    uniqueVals = set(featValues)
    for value in uniqueVals:
        subLabels = labels[:]
        subAttrTypes = attrTypes[:]
        myTree[bestFeatLabel][value] = createTree(splitDataSet(dataSet, bestFeat, value), subLabels, subAttrTypes)
    return myTree

#利用决策树进行分类
def classify(inputTree, featLabels, testVec):
    firstStr = inputTree.keys()[0]
    secondDict = inputTree[firstStr]
    featIndex = featLabels.index(firstStr)
    for key in secondDict.keys():
        if testVec[featIndex] == key:
            if type(secondDict[key]).__name__ == 'dict':
                classLabel = classify(secondDict[key], featLabels, testVec)
            else:
                classLabel = secondDict[key]
    return classLabel


if __name__ == "__main__":
    myData, labels, attrTypes = createDataSet()
    labelsBackup = labels[:]
    tree = createTree(myData, labels, attrTypes)
    print(tree)

    print(classify(tree, labelsBackup, ['rainy', 'mild', 'high', 'FALSE']))