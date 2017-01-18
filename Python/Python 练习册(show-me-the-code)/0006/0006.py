#-*- coding: UTF-8 -*-

#第 0006 题：你有一个目录，放了你一个月的日记，都是 txt，为了避免分词的问题，假设内容都是英文，请统计出你认为每篇日记最重要的词。

import os
import re

def walk_dir(path):
    file_path_list = []
    for root, dirs, files in os.walk(path):
        for f in files:
            if f.lower().endswith('txt'):
                file_path_list.append(os.path.join(root, f))
    return file_path_list

def find_key_word(txt_file_path):
    word_dic = {}
    filename = os.path.basename(txt_file_path)
    with open(txt_file_path) as f:
        text = f.read()
        words_list = re.findall(r'[A-Za-z0-9]+', text.lower())
        for w in words_list:
            if w in word_dic:
                word_dic[w] += 1
            else:
                word_dic[w] = 1
        sorted_word_list = sorted(word_dic.items(), key=lambda d: d[1], reverse=True)
        print u"在%s文件中，%s为关键词，共出现了%s次" % (filename, sorted_word_list[0][0], sorted_word_list[0][1])


if __name__ == "__main__":
    for file_path in walk_dir(os.getcwd()):
        find_key_word(file_path)

