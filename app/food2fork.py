import requests

key = "cd2098965f07ca87a249d52b79be617c"


response = requests.get("https://www.food2fork.com/api/search?key="+key+"&q=mango%20kiwi")

print(response.json())
