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
nums3 = [5,6,7]
nums4 = [1,2,3,4]

#date = datetime.datetime.now().isoformat(timespec='seconds')
date1 = datetime.datetime(2021, 12, 20, 17, 53).isoformat(timespec='minutes')
date2 = datetime.datetime(2021, 12, 20, 17, 54).isoformat(timespec='minutes')
date3 = datetime.datetime(2021, 12, 20, 17, 55).isoformat(timespec='minutes')
date4 = datetime.datetime(2021, 12, 20, 17, 56).isoformat(timespec='minutes')

dates1 = {"date": date1, "nums": nums1}
dates2 = {"date": date2, "nums": nums2}

bet1 = {"bets": [dates1, dates2]}
bet2 = {"bets": [{"date": date1, "nums": [1,2]},
                {"date": date2, "nums": [3,4]},
                {"date": date3, "nums": [5,6,7]}]}

#print(bet2)
#print(bet2["bets"][0]["date"])#get the first date
#print(bet2["bets"][1]["nums"])
#for betss in bet2["bets"]:
#    print("date: " +str(betss["date"]) + "    nums: " + str(betss["nums"]))
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "bets": bet1})
#response = requests.post(BASE + "lottery/ellen", data = {"email" : 'garigax148@wolfpat.com', "bets": bet2})
response = requests.post(BASE + "lottery/leif", data = json.dumps({"email" : 'nazaqibe@onekisspresave.com', "numbers": nums3, "date": date4}))
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums1, "date": date1})#, "date": str(datetime.datetime.now())})
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums2, "date": date2})
#response = requests.post(BASE + "lottery/leif", data = {"email" : 'nazaqibe@onekisspresave.com', "numbers": nums1, "date": date3})

#response = requests.post(BASE + "lottery/pelle", data={"email" : "pelle.gmai", "numbers": 4})
#response = requests.post(BASE + "lottery/ellen", data={"email" : 'garigax148@wolfpat.com', "numbers": nums2, "date": date1})
#response = requests.post(BASE + "lottery/ellen", data={"email" : 'garigax148@wolfpat.com', "numbers": nums2, "date": date3})

#"bet": {"date": {"numbers": []}}}

#print(response.json())
#if(response.json()[str(number)]["isPrime"] == 1):
#    print(str(number) + " is prime")
#else:
#    print(str(number) + " is not prime")
