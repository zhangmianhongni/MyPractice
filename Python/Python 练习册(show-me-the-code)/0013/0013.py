#!/usr/bin/env python
#-*- coding: UTF-8 -*-

#第 0013 题： 用 Python 写一个爬图片的程序，爬 这个链接里的日本妹子图片 :-)

import os
import urllib
from bs4 import BeautifulSoup
from urlparse import urlsplit


def download_pic(src):
    image_content = urllib.urlopen(src).read()
    print urlsplit(src)

    file_name = os.path.basename(urlsplit(src).path)
    output = open("images\\" + file_name, 'wb')
    output.write(image_content)
    output.close()


def catch_tieba_pics(url):
    content = urllib.urlopen(url).read()
    soup = BeautifulSoup(content, 'lxml')
    for b in soup.find_all('img', {"class", "BDE_Image"}):
        download_pic(b['src'])
        

if __name__ == '__main__':
    catch_tieba_pics('http://tieba.baidu.com/p/2166231880')
