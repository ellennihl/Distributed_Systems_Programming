import requests
import json
import datetime

BASE = "http://127.0.0.1:5000/"

date1 = datetime.datetime(2021, 12, 21, 0, 58).isoformat(timespec='minutes')
date2 = datetime.datetime(2021, 12, 21, 0, 59).isoformat(timespec='minutes')
date3 = datetime.datetime(2021, 12, 21, 1, 0).isoformat(timespec='minutes')
date4 = datetime.datetime(2021, 12, 21, 1, 1).isoformat(timespec='minutes')

bet1 = [{"date": date1, "nums": [7,4,3]},
        {"date": date2, "nums": [8,9,10]},
        {"date": date3, "nums": [1,2]},
        {"date": date4, "nums": [1,2,3,4,5,6,7]}]
bet2 = [{"date": date1, "nums": [1,2,3,4,5,6,7]},
        {"date": date2, "nums": [8]},
        {"date": date3, "nums": [3,7]},
        {"date": date4, "nums": [1,2,3,4,5,6,7]}]

#these are tempotary emails, you can get one at: https://temp-mail.org/
response = requests.post(BASE + "lottery/leif", data = json.dumps({"email" : 'nazaqibe@onekisspresave.com', "bets": bet1}))
response = requests.post(BASE + "lottery/ellen", data = json.dumps({"email" : 'garigax148@wolfpat.com', "bets": bet2}))

#response = requests.post(BASE + "lottery/leif", data = json.dumps({"email" : 'nazaqibe@onekisspresave.com', "numbers": nums3, "date": date4}))
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums1, "date": date1})#, "date": str(datetime.datetime.now())})
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums2, "date": date2})
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums1, "date": date3})

#print(response.json())
