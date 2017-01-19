#-- coding: UTF-8 --#
from math import log
import operator
import types


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
        prob = float(labelCounts[key]) / numEntries #当前数据每个分类的比例
        shannonEnt -= prob * log(prob, 2)
    return shannonEnt

#创建数据集
def createDataSet():
    dataSet = [['sunny', 85, 85, 'FALSE', 'no'], ['sunny', 80, 90, 'TRUE', 'no'], ['overcast', 83, 78, 'FALSE', 'yes'],
                ['rainy', 70, 96, 'FALSE', 'yes'], ['rainy', 68, 80, 'FALSE', 'yes'], ['rainy', 65, 70, 'TRUE', 'no'],
                ['overcast', 64, 65, 'TRUE', 'yes'], ['sunny', 72, 95, 'FALSE', 'no'], ['sunny', 69, 70, 'FALSE', 'yes'],
                ['rainy', 75, 80, 'FALSE', 'yes'], ['sunny', 75, 70, 'TRUE', 'yes'], ['overcast', 72, 90, 'TRUE', 'yes'],
                ['overcast', 81, 75, 'FALSE', 'yes'], ['rainy', 71, 80, 'TRUE', 'no']]
    labels = ['outlook', 'temperature', 'humidity', 'windy']
    return dataSet, labels

# 离散特征分割 根据特征值分割数据集，axis是属性索引，value是属性对应的值
def splitDataSet(dataSet, axis, value):
    retDataSet = []
    for featVec in dataSet:
        if featVec[axis] == value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            retDataSet.append(reducedFeatVec)
    return retDataSet

# 连续特征分割 根据特征值分割数据集，axis是属性索引，value是分割点的值
def binSplitDataSet(dataSet, axis, value):
    leftDataSet = []
    rightDataSet = []
    for featVec in dataSet:
        if featVec[axis] <= value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            leftDataSet.append(reducedFeatVec)
        else:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            rightDataSet.append(reducedFeatVec)

    return leftDataSet, rightDataSet

#根据信息增益率选择最好的属性
def chooseBestFeatureToSplit(dataSet):
    numFeatures = len(dataSet[0]) - 1
    baseEntropy = calcShannonEnt(dataSet) #信息熵
    print("baseEntropy: " + str(baseEntropy))
    bestInfoGain = 0.0
    bestFeature  = -1
    bestSplitValue = 0.0
    splitValues = []

    for i in range(numFeatures):
        newEntropy = 0.0 #条件熵
        splitEntropy = 0.0 #分裂信息熵
        fixEntropy = 0.0 #连续值修正信息增益
        print(i)
        if type(dataSet[0][i]) is types.IntType or type(dataSet[0][i]) is types.FloatType: #是否连续特征
            sortDataSet = sorted(dataSet, key=lambda x: x[i])  #按照当前特征由小到大排序
            sortFeatList = [example[i] for example in sortDataSet]
            numDatas = len(sortDataSet)
            lastCategory = ''
            splitNums = [] #只计算分类属性发生改变的分裂点
            for num in range(numDatas):
                curCategory = sortDataSet[num][-1]
                if curCategory != lastCategory and num != 0:
                    lastCategory = curCategory
                    splitNums.append(num)
            bestSubInfoGain = 0.0
            bestSubEntropy = 0.0
            bestSubSplitEntropy = 0.0
            bestSplitNum = -1
            bestSubSplitValue = 0.0
            for splitNum in splitNums: #计算每个分裂点分割数据的条件熵
                subEntropy = 0.0
                subSplitEntropy = 0.0
                value = float(sortFeatList[splitNum - 1] + sortFeatList[splitNum]) / 2       #分割值取2个值的平均值
                leftDataSet, rightDataSet = binSplitDataSet(dataSet, i, value)

                prob = len(leftDataSet) / float(len(dataSet))
                subEntropy += prob * calcShannonEnt(leftDataSet)
                subSplitEntropy -= prob * log(prob, 2)

                prob = len(rightDataSet) / float(len(dataSet))
                subEntropy += prob * calcShannonEnt(rightDataSet)
                subSplitEntropy -= prob * log(prob, 2)

                subInfoGain = baseEntropy - subEntropy
                if subInfoGain > bestSubInfoGain: #选择信息增益(InforGain)最大的分裂点作为该特征的最佳分裂点
                    bestSubInfoGain = subInfoGain
                    bestSubEntropy = subEntropy
                    bestSubSplitEntropy = subSplitEntropy
                    bestSplitNum = splitNum
                    bestSubSplitValue = value
                print("subInfoGain: " + str(subInfoGain))
                print("subSplitEntropy: " + str(subSplitEntropy))
            print("bestSplitNum: " + str(bestSplitNum))
            print("bestSubSplitValue: " + str(bestSubSplitValue))
            print("bestSubEntropy: " + str(bestSubEntropy))
            print("bestSubSplitEntropy: " + str(bestSubSplitEntropy))
            newEntropy = bestSubEntropy #最佳分裂点的条件熵
            splitEntropy = bestSubSplitEntropy
            fixEntropy = float(log(len(sortFeatList) - 2, 2)) / float(len(dataSet)) #对分裂点的信息增益进行修正：减去log2(N-1)/|D|，其中N为可能的分裂点个数，D为数据集合大小
            splitValues.append(bestSubSplitValue)
        else:
            featList = [example[i] for example in dataSet]
            uniqueVals = set(featList)
            print(uniqueVals)
            for value in uniqueVals: #计算离散值的条件熵
                subDataSet = splitDataSet(dataSet, i, value)
                prob = len(subDataSet) / float(len(dataSet))
                newEntropy += prob * calcShannonEnt(subDataSet)
                splitEntropy -= prob * log(prob, 2)
            splitValues.append(None)

        print("newEntropy: " + str(newEntropy))
        print("splitEntropy: " + str(splitEntropy))
        print("fixEntropy: " + str(fixEntropy))
        infoGain = baseEntropy - newEntropy - fixEntropy #信息增益，使用信息增益有一个缺点，那就是它偏向于具有大量值的特征，所以C4.5选用信息增益率
        infoGainRatio = infoGain / splitEntropy #信息增益率
        print("infoGain: " + str(infoGain))
        print("infoGainRatio: " + str(infoGainRatio))
        if infoGainRatio > bestInfoGain: #选择最大信息增益率的特征作为树节点
            bestInfoGain = infoGainRatio
            bestFeature = i
            bestSplitValue = splitValues[i]
    return bestFeature, bestSplitValue #返回节点的特征索引，如果是连续特征，还返回特征的分割值

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
def createTree(dataSet, labels):
    classList = [example[-1] for example in dataSet]
    if classList.count(classList[0]) == len(classList): #如果所有的数据都是同一个分类，返回分类标签
        return classList[0]
    if len(dataSet[0]) == 1: #如果数据集已经处理了所有属性，但类标签依然不唯一，执行多数表决
        return majorityCnt(classList)
    bestFeat, bestSplitValue = chooseBestFeatureToSplit(dataSet)
    bestFeatLabel = labels[bestFeat]
    myTree = {bestFeatLabel: {}}
    del(labels[bestFeat]) #删除已处理的节点名字
    if bestSplitValue is None: #是否连续特征，空则为离散特征
        featValues = [example[bestFeat] for example in dataSet]
        uniqueVals = set(featValues)
        for value in uniqueVals:
            subLabels = labels[:]
            myTree[bestFeatLabel][value] = createTree(splitDataSet(dataSet, bestFeat, value), subLabels) #递归创建树节点
    else:
        leftDataSet, rightDataSet = binSplitDataSet(dataSet, bestFeat, bestSplitValue)
        subLabels = labels[:]
        myTree[bestFeatLabel]["<=" + str(bestSplitValue)] = createTree(leftDataSet, subLabels) #递归创建树节点
        subLabels = labels[:]
        myTree[bestFeatLabel][">" + str(bestSplitValue)] = createTree(rightDataSet, subLabels) #递归创建树节点
    return myTree

#利用决策树进行分类
def classify(inputTree, featLabels, testVec):
    firstStr = inputTree.keys()[0]
    secondDict = inputTree[firstStr]
    featIndex = featLabels.index(firstStr)
    for key in secondDict.keys():
        if "<=" in key:
            value = float(key[2:])
            if testVec[featIndex] <= value:
                if type(secondDict[key]).__name__ == 'dict':
                    classLabel = classify(secondDict[key], featLabels, testVec)
                else:
                    classLabel = secondDict[key]
        elif ">" in key:
            value = float(key[1:])
            if testVec[featIndex] > value:
                if type(secondDict[key]).__name__ == 'dict':
                    classLabel = classify(secondDict[key], featLabels, testVec)
                else:
                    classLabel = secondDict[key]
        elif testVec[featIndex] == key:
            if type(secondDict[key]).__name__ == 'dict':
                classLabel = classify(secondDict[key], featLabels, testVec)
            else:
                classLabel = secondDict[key]
    return classLabel


if __name__ == "__main__":
    myData, labels = createDataSet()
    labelsBackup = labels[:]
    tree = createTree(myData, labels)
    print(tree)

    print(classify(tree, labelsBackup, ['rainy', 78, 85, 'TRUE']))
    print(classify(tree, labelsBackup, ['overcast', 89, 96, 'FALSE']))