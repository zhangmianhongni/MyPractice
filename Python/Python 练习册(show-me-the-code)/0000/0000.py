#-*- coding: UTF-8 -*-
from PIL import Image, ImageDraw, ImageFont


#第 0000 题：将你的 QQ 头像（或者微博头像）右上角加上红色的数字，类似于微信未读信息数量那种提示效果。 类似于图中效果

def add_num(image_file_path, num, fill, font_name):
    im = Image.open(image_file_path)
    x_size, y_size = im.size
    draw = ImageDraw.Draw(im)
    text = str(num)
    font = ImageFont.truetype(font_name, x_size // 3)
    draw.text((y_size // 5 * 4, 0), text, fill, font)
    im.save("result.jpg")

if __name__ == '__main__':
    image_path = 'image.png'
    fill_num = 3
    fill_color = (255, 0, 255)
    fill_font = "C:/windows/fonts/Arial.ttf"
    add_num(image_path, fill_num, fill_color, fill_font)
