import requests
import datetime
import random
from User import User, Bet

def makeBetts(gmail,start,end,numbers):
    while(start != end):
        user = User(gmail,[Bet(start, numbers)])
        struser = {"gmail": user.gmail, "date": str(user.bets[0].date), "numbers": user.bets[0].numbers}
        print(struser)
        start = start + datetime.timedelta(hours=1)
        response = requests.post(BASE + "lottery/2", struser)
        print(response.json())
    user = User(gmail,[Bet(start, numbers)])
    struser = {"gmail": user.gmail, "date": str(user.bets[0].date), "numbers": user.bets[0].numbers}
    response = requests.post(BASE + "lottery/1", struser)
    print(response.json())
BASE = "http://127.0.0.1:5000/"

def buchOfrandom():
    numbers = []
    for x in range(255):
        numbers.append(random.randrange(0, 255))
    numbers = list(dict.fromkeys(numbers))
    print(len(numbers))
    return numbers

if __name__ == "__main__":
    gmail = "ekekekskrap@gmail.com"
    #startDate = datetime.datetime(2021, 12, 17, 2)
    #endDate = datetime.datetime(2021, 12, 17, 2)
    startDate = datetime.datetime.now()
    endDate = datetime.datetime.now() + datetime.timedelta(hours=1)
    numbers = buchOfrandom()
    makeBetts(gmail,startDate,endDate,numbers)

    gmail2 = "Eejk@gmail.com"
    numbers = buchOfrandom()
    makeBetts(gmail2,startDate,endDate,numbers)
