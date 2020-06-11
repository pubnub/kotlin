#!/usr/bin/python

import hashlib
import hmac
import base64
import sys
import subprocess

secret = sys.argv[1]
data = sys.argv[2].replace("###", "\n")
is_v2_signature = sys.argv[3]

message = "{0}".format(data)

hash = hmac.new(secret, message, hashlib.sha256)

# to lowercase hexits
hash.hexdigest()

# to base64
result = base64.b64encode(hash.digest())

result = result.replace('+','-')
result = result.replace('/','_')
result = result.replace('/n','')

if is_v2_signature == 'true':
    while result.endswith('='):
	    result = result[:-1]
    signature = 'v2.'+result
else:
    signature = result

print(signature)