import requests
import datetime
from User import User, Bet

BASE = "http://127.0.0.1:5000/"

#response = requests.get(BASE + "lottery/"+str(datetime.datetime.now()))
response = requests.get(BASE + "lottery/" + str(datetime.datetime.now()))
user = response.json()
print(user)
