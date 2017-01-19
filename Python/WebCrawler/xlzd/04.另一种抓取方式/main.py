#!/usr/bin/env python
# encoding=utf-8

"""
目的是抓取拉勾网Python分类下广州到目前为止展示出来的所有招聘信息，首先在浏览器点击进去看看吧。
如果你足够小心或者网速比较慢，那么你会发现，在点击Python分类之后跳到的新页面上，招聘信息出现时间是晚于页面框架出现时间的。
"""

import codecs
import requests
import json

DOWNLOAD_URL = 'http://www.lagou.com/jobs/positionAjax.json?city=%E5%B9%BF%E5%B7%9E&needAddtionalResult=false'

def download_page(url, payload):
    json_data = requests.post(url, data=payload).content
    data = json.loads(json_data)
    return data['content']['positionResult']['result']

def parse_data(data):
    return data, len(data) > 0

def main():
    has_next = True
    url = DOWNLOAD_URL

    with codecs.open('jobs', 'wb', encoding='utf-8') as fp:
        pn = 0
        while has_next:
            pn += 1
            payload = {'first': False, 'pn': pn, 'kd': 'Python'}
            print u'捉取第{no}页...'.format(no=pn)
            data = download_page(url, payload)
            jobs, has_next = parse_data(data)
            for job in jobs:
                business_zones = job['businessZones'][0] if job['businessZones'] else job['city']
                fp.write(u'{positionName}[{businessZones}] {formatCreateTime}\n'.format(positionName=job['positionName'], businessZones=business_zones,
                            formatCreateTime=job['formatCreateTime']))
                fp.write(u'{companyName} {companyShortName} {jobNature}\n'.format(companyName=job['companyName'], companyShortName=job['companyShortName'],
                            jobNature=job['jobNature']))
                fp.write(u'{salary} 经验{workYear}/{education}  {industryField}/{financeStage}\n'.format(salary=job['salary'], workYear=job['workYear'],
                            education=job['education'], industryField=job['industryField'], financeStage=job['financeStage']))
                fp.write(u'{positionAdvantage}\n\n'.format(positionAdvantage=job['positionAdvantage']))


if __name__ == '__main__':
    main()
