#-*- coding: UTF-8 -*-

#第 0008 题：一个HTML文件，找出里面的正文。

import urllib2
from bs4 import BeautifulSoup

def get_content(url):
    page = urllib2.urlopen(url).read()
    soup = BeautifulSoup(page, 'lxml')
    return soup.body.text

if __name__ == '__main__':
    print get_content('http://tech.163.com/16/1005/08/C2JNC60L00097U7S.html')
