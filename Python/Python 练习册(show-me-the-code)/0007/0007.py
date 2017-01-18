#-*- coding: UTF-8 -*-

#第 0007 题：有个目录，里面是你自己写过的程序，统计一下你写过多少行代码。包括空行和注释，但是要分别列出来。

"""
第 0007 题：有个目录，里面是你自己写过的程序，统计一下你写过多少行代码。包括空行和注释，但是要分别列出来。
"""

import os

def walk_dir(root_path):
    file_path = []
    for root, dirs, files in os.walk(root_path):
        for ff in files:
            if ff.lower().endswith('.py'):
                file_path.append(os.path.join(root, ff))
    return file_path

def count_the_code(path):
    comment_flag = False
    code_blank_line_count = 0
    code_comment_line_count = 0
    with open(path, mode='r') as fp:
        lines = fp.readlines()
        for line in lines:
            if line.strip().startswith('\"\"\"') and not comment_flag:
                comment_flag = True
                code_comment_line_count += 1
                continue

            if line.strip().startswith('\"\"\"'):
                comment_flag = False
                code_comment_line_count += 1

            if line.strip().startswith('#') or comment_flag:
                code_comment_line_count += 1

            if line == '\n':
                code_blank_line_count += 1
    return len(lines), code_comment_line_count, code_blank_line_count


if __name__ == "__main__":
    n1 = n2 = n3 = 0
    for f in walk_dir(os.getcwd()):
        r1, r2, r3 = count_the_code(f)
        n1 += r1
        n2 += r2
        n3 += r3
    print '文件夹中的文件总行数为' + str(n1) + '行'
    print '文件夹中的注释总行数为' + str(n2) + '行'
    print '文件夹中的空行总行数为' + str(n3) + '行'
    print '文件夹中的代码总行数为' + str(n1 - n2 - n3) + '行'
