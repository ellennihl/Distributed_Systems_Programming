#Created by Eek de Bruijckere and Ellen Nihl 16-12-2021
#inspired by https://www.youtube.com/watch?v=GMppyAPbLYk
#the email part is inspired by https://pythonbasics.org/flask-mail/

from flask import Flask
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy
from flask_mail import Mail, Message
import requests

import time
import threading
import random
import datetime

participants = {}
pool = 0#dela upp pools så att det är en pool per datum TOOOOOOOOOOOOOOOOOOOOOOOODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
winners = {}

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

#@app.route("/mail/<string:winner>")
#def index():
#    global winners
#    msg = Message('Hello', sender = 'group6assignment5@gmail.com', recipients = [winners[winner]["email"]])
#    msg.body = "Hello Flask message sent from Flask-Mail"
#    mail.send(msg)
#    return "Sent"

partic_post_args = reqparse.RequestParser()
partic_post_args.add_argument("email", type=str, help="Email of participant", required=True)
partic_post_args.add_argument("numbers", type=int, action='append', help="Enter your numbers", required=True)#testa att ha kvar int när jag skickar lista
partic_post_args.add_argument("date", type=str, help="Enter your numbers", required=True)#testa att ha kvar int när jag skickar lista
#OMG DET VAR APPEND SOM FIXADE DET...........................................................
#vanlig lista [] i post då
#fixa 100kr för varje bet
#flera datum. inte lista av datum. ett post för varje datum. men flera nummer för varje post.
#retrieve historic periodic data
#you are only allowed to post a specific number ONCE
#man ska kunna skicka ett datum i framtiden som det gäller för

class Mail(Resource):

    def get(self,name):
        global winners
        msg = Message('WINNER', sender = 'group6assignment5@gmail.com', recipients = [winners[name]["email"]])
        msg.body = "Congratulations! You won " + str(winners[name]["pool"]) + " kr! \nKind regards,\nEllen and Eek"
        mail.send(msg)
        return "Sent"

class Lottery(Resource):

    def get(self,name):
        global winners, participants
        if(name=="winners"):
            return winners
        else:
            return participants[name]

    def post(self,name):
        global participants, pool
        args = partic_post_args.parse_args()
        date = datetime.datetime.strptime(args.date, '%Y-%m-%dT%H:%M')#retrieve the application date
        if(date > datetime.datetime.now()):#and talet måste vara inom rangen
            participants[name] = args
            pool = pool + len(args.numbers)*100 #add 100 kr for every number
            return participants[name], 201
        else:
            return "Date must be in the future", 405

api.add_resource(Lottery, "/lottery/<string:name>")
api.add_resource(Mail, "/mail/<string:name>")

#Draw() handles the distribution of prices. It draws a winning number and checks if any participant has betted on that number
#and that the date is not expired. If we have a winner, a GET request is sent to the Server
#and the server sends an email to the winner.
def draw():
    time = datetime.datetime.now() #datetime.datetime(2021, 5, 17, 14)  dt.replace(hour=11, minute=59)
    min = time.time().minute
    while(True):
        min = min + 1#every hour
        time = time.replace(minute=min)#hour=11, minute=59)#draw in an hour
        while(datetime.datetime.now() < time):#(time + datetime.timedelta(seconds=30))):#thread is busy waiting
            pass
        finishDate = time#time.replace(second=time.time().second()+1)#datetime.datetime.now() + datetime.timedelta(seconds=30)#change here for time between pricesFIXAAAAAAAAAAA DEN SKA INTE UPPDATEAS
        winningNr = random.randrange(1, 7)#change here for the range of numbers
        winnersAmount = 0
        global participants, winners, pool
        print("winning number: ")
        print(winningNr)
        expired = []
        for key in participants:
            date = datetime.datetime.strptime(participants[key]["date"], '%Y-%m-%dT%H:%M')#retrieve the application date
            if(date == finishDate):#if the betting date is now
                print("date is ok")
                for num in participants[key]["numbers"]:
                    print(num)
                    if(num == winningNr):#check for winners
                        winners[key] = participants[key]
                        winners[key]["date"] = str(finishDate)
                        winnersAmount = winnersAmount+1
            elif (date < finishDate):#date is expired, remove the participant maybe send mail? TODO
                expired.append(key)
        if(winnersAmount>0):#if we have any winners(and ensure no div by 0)
            pool = pool/winnersAmount
            for key in winners:
                del participants[key]#now that the participant won, moved to winners instead
                winners[key]["pool"] = pool#add their price
                global BASE
                requests.get(BASE + "mail/" + str(key))#send mail
            pool = 0
            print("winners: " + str(winners))
        print("participants" + str(participants))
        for key in expired:
            print("ta bort " + str(participant[key]))
            del participants[key] #remove expired participants
        #time.sleep(10)#maybe busy wait instead?

if __name__ == "__main__":
    t = threading.Thread(target=draw)#spawn a thread which calls draw()
    t.start()
    app.run(debug=False)
    t.join()
    print("kommer du hit")
