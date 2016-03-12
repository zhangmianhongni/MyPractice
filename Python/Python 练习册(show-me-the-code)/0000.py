#-*- coding: UTF-8 -*-
from PIL import Image, ImageDraw, ImageFont


#第 0000 题：将你的 QQ 头像（或者微博头像）右上角加上红色的数字，类似于微信未读信息数量那种提示效果。 类似于图中效果

def add_num(image_file_path, num, fill, font_name):
    im = Image.open(image_file_path)
    xsize, ysize = im.size
    draw = ImageDraw.Draw(im)
    text = str(num)
    font = ImageFont.truetype(font_name, xsize // 3)
    draw.text((ysize // 5 * 4, 0), text, fill, font)
    im.save("resources/result.jpg")

if __name__ == '__main__':
    image_file_path = 'resources/image.png'
    num = 4
    fill = (255, 0, 255)
    font_name = "C:/windows/fonts/Arial.ttf"
    add_num(image_file_path, num, fill, font_name)
