#-*- coding: UTF-8 -*-

#第 0003 题：将 0001 题生成的 200 个激活码（或者优惠券）保存到 Redis 非关系型数据库中。

import uuid
import redis

redis_server = redis.StrictRedis(host='localhost', port=6379, db=0)

def create_code(number=200):
    code_result = []
    while True is True:
        temp = str(uuid.uuid1()).replace('-', '')
        if temp not in code_result:
            code_result.append(temp)
        if len(code_result) is number:
            break
    return code_result

def clean_up(prefix='showmethecode'):
    keys = redis_server.keys('%s_*' % prefix)
    for key in keys:
        redis_server.delete(key)


def insert_code(code, prefix='showmethecode'):
    redis_server.set('%s_%s' % (prefix, code), code)


def select_codes(prefix='showmethecode'):
    keys = redis_server.keys('%s_*' % prefix)
    select_result = []
    for key in keys:
        select_result.append(redis_server.get(key))
    return select_result

if __name__ == '__main__':
    clean_up()
    codes = create_code()
    for c in codes:
        insert_code(c)
    result = select_codes()
    print result
