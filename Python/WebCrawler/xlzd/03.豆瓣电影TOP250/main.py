#!/usr/bin/env python
# encoding=utf-8

"""
爬取豆瓣电影TOP250 - 完整示例代码

我们已经得到的信息有如下：

1.每页有25条电影，共有10页。
2.电影列表在页面上的位置为一个class属性为grid_view的ol标签中。
3.每条电影信息放在这个ol标签的一个li标签里。
"""

import codecs
import requests
from bs4 import BeautifulSoup

DOWNLOAD_URL = 'http://movie.douban.com/top250/'


def download_page(url):
    """
    如果不加'User-Agent'就会产生403的，原因一般可能是因为需要登录的网站没有登录或者被服务器认为是爬虫而拒绝访问，
    这里很显然属于第二种情况。一般，浏览器在向服务器发送请求的时候，会有一个请求头——User-Agent，
    它用来标识浏览器的类型.当我们使用requests来发送请求的时候，
    默认的User-Agent是python-requests/2.8.1（后面的数字可能不同，表示版本号）

    服务器通过校验请求的U-A来识别爬虫，这算是最简单的一种反爬虫机制了，通过模拟浏览器的U-A，能够很轻松地绕过这个问题。
    """
    headers = {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36'
    }
    data = requests.get(url, headers=headers).content
    return data


"""
我们创建了一个BeautifulSoup对象，然后紧接着在第7行使用刚刚创建的对象搜索这篇html文档中查找那个class为grid_view的ol标签，
接着通过find_all方法，我们得到了电影的集合，通过对它迭代取出每一个电影的名字，打印出来。
至于for循环之间的内容，其实就是在解析每个li标签。你可以很简单的在刚才的浏览器窗口通过开发者工具查看li中的网页结构。
我们找到了下一页的链接放置在一个span标签中，这个span标签的class为next。
具体链接则在这个span的a标签中，到了最后一页之后，这个span中的a标签消失了，就不需要再翻页了。
"""
def parse_html(html):
    soup = BeautifulSoup(html, "lxml")
    movie_list_soup = soup.find('ol', attrs={'class': 'grid_view'})

    movie_list = []

    for movie_li in movie_list_soup.find_all('li'):
        detail = movie_li.find('div', attrs={'class': 'hd'})
        pic = movie_li.find('div', attrs={'class': 'pic'})
        rank = pic.find('em').getText()
        name = detail.find('span', attrs={'class': 'title'}).getText()

        movie = {'name': name, 'rank': rank}

        movie_list.append(movie)

    next_page = soup.find('span', attrs={'class': 'next'}).find('a')
    if next_page:
        return movie_list, DOWNLOAD_URL + next_page['href']
    return movie_list, None


def main():
    url = DOWNLOAD_URL

    with codecs.open('movies', 'wb', encoding='utf-8') as fp:
        while url:
            html = download_page(url)
            movies, url = parse_html(html)
            for movie in movies:
                fp.write(u'{rank} {name}\n'.format(rank=movie['rank'], name=movie['name']))


if __name__ == '__main__':
    main()
