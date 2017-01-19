# -*- coding: utf-8 -*-
import sys
import csv
import codecs
import jieba.analyse
reload(sys)
sys.setdefaultencoding("utf-8")

#stopkeyword = [line.strip() for line in open('stop.txt').readlines()]  #将停止词文件保存到列表
text = open(r"Text\All.txt", "r").read()
zidian = {}
fenci = jieba.cut(text)

for fc in fenci:
    if fc in zidian:
        zidian[fc] += 1
    else:
        zidian[fc] = 1

sorted_zidian = sorted(zidian.items(), key=lambda d: d[1], reverse=True)

print sorted_zidian

with open('Text\words.csv', 'wb') as csvfile:
    csvfile.write(codecs.BOM_UTF8)
    for word, freq in sorted_zidian:
        spamWriter = csv.writer(csvfile, dialect='excel')
        spamWriter.writerow([word, freq])

#tfidf = jieba.analyse.extract_tags(text, topK=300, withWeight=True)

#for word_weight in tfidf:
    #if word_weight in stopkeyword:
        #pass
    #else:
        #print word_weight[0], zidian.get(word_weight[0], 'not found'), str(int(word_weight[1] * 100)) + '%'
        #output.write('%s,%s,%s\n' % (word_weight[0], zidian.get(word_weight[0], 'not found'), str(int(word_weight[1] * 100)) + '%'))

