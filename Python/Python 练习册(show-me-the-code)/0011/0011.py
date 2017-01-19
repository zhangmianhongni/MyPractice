#!/usr/bin/env python
#-*- coding: UTF-8 -*-

#第 0011 题： 敏感词文本文件 filtered_words.txt，里面的内容为以下内容，当用户输入敏感词语时，则打印出 Freedom，否则打印出 Human Rights。

def input_check(filter_words):
    string = raw_input("Please enter word: ")
    if string in filter_words:
        print "Freedom"
    else:
        print "Human Rights"

if __name__ == '__main__':
    filter_file = open('filtered_words.txt')
    filtered_words = [line.replace('\n', '') for line in filter_file]
    input_check(filtered_words)

