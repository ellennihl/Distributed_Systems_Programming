#Created by Eek de Bruijckere and Ellen Nihl 16-12-2021
#inspired by https://www.youtube.com/watch?v=GMppyAPbLYk
#the email part is inspired by https://pythonbasics.org/flask-mail/
#the user request is inspired by https://jdhao.github.io/2021/04/08/send_complex_data_in_python_requests/

from flask import Flask, request
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy
from flask_mail import Mail, Message
import requests

import time
import threading
import random
import datetime
import json

participants = {}
pool = {}
winners = {}
finishDateStr = ""

app = Flask(__name__)
api = Api(app)
BASE = "http://127.0.0.1:5000/"

app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'group6assignment5@gmail.com'#the email we send from
app.config['MAIL_PASSWORD'] = 'asgmt5grp6'#asgmt5grp6
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_MAX_EMAILS'] = 5
mail = Mail(app)

class Mail(Resource):

    def get(self,name):
        global winners
        msg = Message('WINNER', sender = 'group6assignment5@gmail.com', recipients = [winners[name]["email"]])
        msg.body = "Congratulations! You won " + str(winners[name][finishDateStr]["pool"]) + " kr! \nKind regards,\nEllen and Eek"
        mail.send(msg)
        return "Sent"

class Lottery(Resource):

    def get(self,name):
        global winners, participants
        if(name=="winners"):
            return winners
        if(name=="all"):
            return participants
        else:
            return participants[name]

    def post(self,name):
        global participants, pool
        user_request = json.loads(request.data)
        for bets in user_request['bets']:
            for nums in bets["nums"]:
                if(nums not in range(1,256)):
                    return "Numbers must be within range", 405
            if datetime.datetime.strptime(bets["date"], '%Y-%m-%dT%H:%M') < datetime.datetime.now():
                return "Date must be in the future", 405
        participants[name] = user_request
        for bets in user_request["bets"]:
            date = bets["date"]
            if(date in pool.keys()):
                pool[date]["amount"] += len(bets["nums"])*100 #add 100 kr for every number
            else:
                pool[date] = {"amount": len(bets["nums"])*100}
        return participants[name], 201


api.add_resource(Lottery, "/lottery/<string:name>")
api.add_resource(Mail, "/mail/<string:name>")

#Draw() handles the distribution of prices. It draws a winning number and checks if any participant has betted on that number
#and that the date is not expired. If we have a winner, a GET request is sent to the Server
#and the server sends an email to the winner.
def draw():
    while(True):#TODO FIXA SÅ ATT MINUTERNA BLIR TIMMMAR SEN SÅ DEN INTE HÅLLER PÅ O SÄGER MINUT 68LIKSOM DÅ ÄR DET JU TIMME IST
        global participants, winners, pool, BASE, finishDateStr
        finishDate = updateTime()
        finishDateStr = finishDate.strftime('%Y-%m-%dT%H:%M')
        winningNr = random.randrange(1, 7) #change here for the range of numbers
        winnersAmount = 0
        print("winning number: " + str(winningNr))
        for key in participants:
            for bets in participants[key]["bets"]:
                date = datetime.datetime.strptime(bets["date"], '%Y-%m-%dT%H:%M') #retrieve the application date
                if(date == finishDate): #if the betting date is now
                    for num in bets["nums"]:
                        if(num == winningNr): #check for winners
                            if(key not in winners):
                                winners[key] = {}
                                winners[key]["email"] = participants[key]["email"]
                            winners[key][finishDateStr] = {"winningNr": winningNr, "pool": 0}
                            winnersAmount = winnersAmount+1
        if(winnersAmount>0): #if we have any winners(and ensure no div by 0)
            poolPart = pool[finishDateStr]["amount"]/winnersAmount
            print(winners)
            for key in winners:
                winners[key][finishDateStr]["pool"] = poolPart #add their price
                requests.get(BASE + "mail/" + str(key)) #send mail
        else: #if no one won, add the current pool to the next
            nextDateStr = nextDate(finishDate)
            print(pool)
            if(nextDateStr in pool.keys()): #TODO gör detta snyggare
                if(finishDateStr in pool.keys()):
                    pool[nextDateStr]["amount"] += pool[finishDateStr]["amount"]
                #else: dont need to add anything
            elif(finishDateStr in pool.keys()):
                pool[nextDateStr] = {"amount": pool[finishDateStr]["amount"]}
            else:
                pool[nextDateStr] = {"amount": 0}
            if(finishDateStr in pool.keys()):
                pool[finishDateStr]["amount"] = 0
            print(pool)
        print("winners: " + str(winners))
        print("participants" + str(participants))

#waits for the new drawing time
def updateTime():
    finishDate = datetime.datetime.now()
    finishDate += datetime.timedelta(minutes=1)
    finishDate = finishDate.replace(second=0, microsecond=0)#hour=11, minute=59)#draw in an hour
    print(finishDate)
    while(datetime.datetime.now() < finishDate): #thread is busy waiting
        pass
    return finishDate

#retrieves the next drawing time
def nextDate(finishDate):
    nextDate = finishDate + datetime.timedelta(minutes=1)
    nextDateStr = nextDate.strftime('%Y-%m-%dT%H:%M')
    return nextDateStr

if __name__ == "__main__":
    t = threading.Thread(target=draw) #spawn a thread which calls draw()
    t.start()
    app.run(debug=False)
    t.join()
