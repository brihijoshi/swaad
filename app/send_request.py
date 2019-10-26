from __future__ import print_function
import requests
import json
import cv2
import io

addr = 'http://localhost:5000'
test_url = addr + '/api/test'

# prepare headers for http request
content_type = 'image/jpeg'
headers = {'content-type': content_type}

img = open("/Volumes/Brihi/sdhacks-rep/object_detection/data/Grocery/testImages/WIN_20160803_11_42_36_Pro.jpg", 'rb').read()
print(len(img))
response = requests.post(test_url, data=img, headers=headers)
# decode response
print(response.text)
print(json.loads(response.text))
