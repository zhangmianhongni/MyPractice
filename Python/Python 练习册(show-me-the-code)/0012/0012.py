#!/usr/bin/env python
#-*- coding: UTF-8 -*-

#第 0012 题： 敏感词文本文件 filtered_words.txt，里面的内容 和 0011题一样，当用户输入敏感词语，则用 星号 * 替换，例如当用户输入「北京是个好城市」，则变成「**是个好城市」。

import re

#生成对应的正则匹配规则
def gen_pattern(filter_words):
    pattern = ''
    for string in filter_words:
        pattern += string + '|'
    return pattern[:-1]

def input_check(pattern):
    sentence = raw_input("Please enter word: ")
    print re.sub(pattern, '**', sentence)

if __name__ == '__main__':
    filter_file = open('filtered_words.txt')
    filtered_words = [line.replace('\n', '') for line in filter_file]
    words_pattern = gen_pattern(filtered_words)
    input_check(words_pattern)

