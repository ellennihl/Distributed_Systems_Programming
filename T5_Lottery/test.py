import requests
import datetime
from User import User, Bet

BASE = "http://127.0.0.1:5000/"

response = requests.get(BASE + "lottery/" + str(0))
user = response.json()
print(user)
date = datetime.datetime(2022, 5, 17, 13, 20)
dateStr = str(date)
print(dateStr)
user = {"gmail": "hennas@gmail.com","date":dateStr,"numbers":[3]}
response = requests.post(BASE + "lottery/" + str(1), user)
print(response.json())
