import requests
import json
import datetime

BASE = "http://127.0.0.1:5000/"
name = "ellen"
#response = requests.get(BASE + "lottery/" + name)
#if(response.status_code == 404):
    #temp = isPrime(number)
#    print("Add new item")
#{"ellen": {"email": "garigax148@wolfpat.com", "numbers": {10,9,8,7,6,5,4}}
nums1 = [1,2,3,4,5]
nums2 = [6,7]
#date = datetime.datetime.now().isoformat(timespec='seconds')
date1 = datetime.datetime(2021, 12, 19, 16, 47).isoformat(timespec='minutes')
date2 = datetime.datetime(2021, 12, 19, 16, 48).isoformat(timespec='minutes')
response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums1, "date": date1})#, "date": str(datetime.datetime.now())})
#response = requests.post(BASE + "lottery/pelle", data={"email" : "pelle.gmai", "numbers": 4})
response = requests.post(BASE + "lottery/ellen", data={"email" : 'garigax148@wolfpat.com', "numbers": nums2, "date": date2})


#print(response.json())
#if(response.json()[str(number)]["isPrime"] == 1):
#    print(str(number) + " is prime")
#else:
#    print(str(number) + " is not prime")
