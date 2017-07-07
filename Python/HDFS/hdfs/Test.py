from hdfs import *

client = Client("http://10.201.76.248:50070", root="/", session=False)
print client.list("/")
print client.list("/digtest/", status=True)

with client.read("/testdata/up1") as reader:
    print reader.read()
