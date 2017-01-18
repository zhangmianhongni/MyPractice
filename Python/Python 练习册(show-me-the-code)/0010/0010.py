#-*- coding: UTF-8 -*-

#第 0010 题：使用 Python 生成类似于下图中的字母验证码图片

import random
import string
from PIL import Image, ImageDraw, ImageFont, ImageFilter

IMAGE_MODE = 'RGB'
IMAGE_BG_COLOR = (255, 255, 255)
Image_Font = 'arial.ttf'

#change 噪点频率（%）
def random_color():
    color = (random.randint(32, 127), random.randint(32, 127), random.randint(32, 127))
    return color

def get_letters():
    str = ''
    for loop in range(4):
        str += random.choice(string.letters)
    return str

def create_identifying_code(text, width=400, height=200, chance=2):
    im = Image.new(IMAGE_MODE, (width, height), IMAGE_BG_COLOR)
    draw = ImageDraw.Draw(im)
    # 绘制背景噪点
    for w in xrange(width):
        for h in xrange(height):
            if chance < random.randint(1, 100):
                draw.point((w, h), fill=random_color())

    font = ImageFont.truetype(Image_Font, 80)
    font_width, font_height = font.getsize(text)
    text_len = len(text)
    x = (width - font_width) / 2
    y = (height - font_height) / 2
    # 逐个绘制文字
    for i in text:
        draw.text((x, y), i, random_color(), font)
        x += font_width / text_len
    # 模糊
    im = im.filter(ImageFilter.BLUR)
    im.save('identifying_code_pic.jpg')


if __name__ == '__main__':
    letters = get_letters()
    print '验证码：' + letters
    create_identifying_code(letters)

