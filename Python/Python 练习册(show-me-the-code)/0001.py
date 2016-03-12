#-*- coding: UTF-8 -*-

# 第 0001 题：做为 Apple Store App 独立开发者，你要搞限时促销，为你的应用生成激活码（或者优惠券），
# 使用 Python 如何生成 200 个激活码（或者优惠券）？

import uuid

def create_code(number=200):
    result = []
    while True is True:
        temp = str(uuid.uuid1()).replace('-', '')
        if temp not in result:
            result.append(temp)
        if len(result) is number:
            break
    return result

if __name__ == '__main__':
    print create_code()
