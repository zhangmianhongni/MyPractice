#-*- coding: UTF-8 -*-

#第 0009 题：一个HTML文件，找出里面的链接。

import urllib2
from bs4 import BeautifulSoup

def get_links(url):
    page = urllib2.urlopen(url).read()
    soup = BeautifulSoup(page, 'lxml')
    return soup.findAll('a')

if __name__ == '__main__':
    links = get_links('http://tech.163.com/16/1005/08/C2JNC60L00097U7S.html')
    for link in links:
        print(link['href'])
    print '链接数量：' + str(len(links))
