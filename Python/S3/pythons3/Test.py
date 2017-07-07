import boto
import boto.s3.connection
from boto.s3.key import Key

conn = boto.connect_s3(aws_access_key_id='59FKIDBLYUW1FS0RDY8B', aws_secret_access_key='SOPK8BPbHfPNC1dcLzHSuIwmkCuPZCUiDR7iZjnH',
            host="114.67.33.18", port=82, is_secure=False, calling_format=boto.s3.connection.OrdinaryCallingFormat())

buckets = conn.get_all_buckets()
print buckets

bucket = conn.get_bucket("bingocctest2")
print bucket.get_all_keys()
key = bucket.get_key("B6C9614CB48641EB8141E7DB8CFC16DC_S_T_RDB_DA_1493387655438/aa/B6C9614CB48641EB8141E7DB8CFC16DC_S_T_RDB_DA_14933876554382017_04_29.txt")
print key.get_contents_as_string()