from flask import Flask#, jsonify
from flask_restful import Api, Resource, reqparse #, abort, fields, marshal_with
#from flask_sqlalchemy import SQLAlchemy
from flask_apscheduler import APScheduler
from flask_mail import Mail, Message
import random
import json
from User import *
import datetime
import requests

#users = []
#just for testing. When booting clears the Database file
f = open("Database.txt", "w")
f.write("")
f.close()

app = Flask(__name__)
api = Api(app)
pool = 0 #the total amount of monny in the pool

app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'group6assignment5@gmail.com' #temporary mail created for this
app.config['MAIL_PASSWORD'] = 'asgmt5grp6'#asgmt5grp6
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_MAX_EMAILS'] = 5
mail = Mail(app)

#With a get comand to /mail will send a gmail to a user
#with this a gmail address and the monny won is sent
@app.route("/mail/<gmail>/<float:pool>")
def index(gmail,pool):
	msg = Message('Hello', sender = 'group6assignment5@gmail.com', recipients = [gmail])
	msg.body = "Hello Flask message sent from Flask-Mail \n"
	msg.body = msg.body + "You have won " + str(pool) + "SEK in the lottery"
	mail.send(msg)
	return "Sent"

#scheduleTask runs onece every hour.
#it generates a random number. checks if there have been anny bets on this
#	time and date. For every bet made 100kr will be added to the pool.
#	If there is a winner/winners(bet same number as generated) then the monny
#	in the pool will be splitt and a mail is sent to the winners. The pool
#	resets to 0 as well. If there is no winner nothing happens.
#	All bets on this time will be removed.
scheduler = APScheduler()
def scheduleTask():
	global pool
	users = getUsers()
	winningNr = random.randrange(0, 255)
	winners = []
	date = datetime.datetime.now()
	date = date.replace(minute=0, second=0, microsecond=0)

	print(winningNr)
	#check if there is a winner/winners
	for user in users:
		if(str(user.bets[0].date) == str(date)):
			for nr in user.bets[0].numbers:
				if(nr == winningNr):
					winners.append(user.gmail)
				pool = pool + 100
			removeUser(user)
			user.removeDate(user.bets[0].date)
			if(user.bets):
				addUser(user)

	#if there is/are winner/winners devide the monny and send a mail
	print("winners are:" + str(winners))
	if(winners):
		for w in winners:
			print(w + " has won +" + str(pool/len(winners)))
	print("Date " + str(date) + " Monney " + str(pool) + "Sek")

	saveJson = '{"Date": "' + str(date) + '", "Pool":' + str(pool) + ', "Winners": ['
	if(winners):
		for x in range(len(winners)):
			requests.get("http://127.0.0.1:5000/mail/"+str(winners[x])+"/" + str(pool/len(winners)))
			saveJson = saveJson + '"' +str(winners[x]) + '"'
			if(x+1!=len(winners)):
				saveJson = saveJson + ', '
			else:
				pool = 0
				saveJson = saveJson + ']} \n'
	else:
		saveJson = saveJson + ']} \n'
	f = open("Winners.txt", "a")
	f.write(saveJson)
	f.close()

	users.clear()
	winners.clear()
	return{1:"yes"}

#desiding what needs to be sent in the post
parser = reqparse.RequestParser()
parser.add_argument("gmail", type=str, help="No gmail", required=True)
parser.add_argument("date", type=str, help="No date", required=True)
parser.add_argument("numbers", type=int, action='append', help="No numbers", required=True)
#users = [User("Eek@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12])])]

#remove a user from the datebase
def removeUser(user):
	with open("Database.txt", "r") as f:
		lines = f.readlines()
	with open("Database.txt", "w") as f:
		for line in lines:
			if line.strip("\n") != user.toJSON():
				f.write(line)
#get a user from the database
def getUsers():
	user = []
	f = open("Database.txt", "r")
	for x in f:
		users.append(userJSONtoObject(x))
	f.close()
	return users
#adds a user to the database
def addUser(newUser):
	f = open("Database.txt", "a")
	f.write(newUser.toJSON() + "\n")
	f.close()

def abort_if_date_to_early(number):
	if(number > len(users)):
		abort(200, message = "Number not in database...")

#The server is running 24/7 and can add bets with the post and the client can
#the result of every hour.
class Server(Resource):
	#get the period of drawings between given dates
	#returns the drawn lucky number, the number of winners, and the winnings totals
	#can also return error
	def get(self,date1):
		print("get here")
		if "." in date1:
			date = datetime.datetime.strptime(date1, '%Y-%m-%d %H:%M:%S.%f')
			date = date.replace(minute=0, second=0, microsecond = 0) # + datetime.timedelta(hours=1)
		else:
			date = datetime.datetime.strptime(date1, '%Y-%m-%d %H:%M:%S')
			date = date.replace(minute=0, second=0)
		f = open("Winners.txt", "r")
		for x in f:
			item = json.loads(x, object_hook=lambda d: Namespace(**d))
			if(str(date) == str(item.Date)):
				return {"date":item.Date, "pool":item.Pool,"winners":item.Winners}
		f.close()
		return {date1:"hello world"}
	#if the user has not betted. Add the use and add the bet apropriotly
	#if the user already exists update the bets in the user and add it to drawings
	def post(self,date1):
		users = getUsers()
		args = parser.parse_args()
		print(args.date)
		if "." in args.date:
			date = datetime.datetime.strptime(args.date, '%Y-%m-%d %H:%M:%S.%f')
			date = date.replace(minute=0, second=0, microsecond = 0) # + datetime.timedelta(hours=1)
		else:
			date = datetime.datetime.strptime(args.date, '%Y-%m-%d %H:%M:%S')
			date = date.replace(minute=0, second=0)
		newBet = [Bet(date, args.numbers)]
		newUser = User(args.gmail,newBet)
		if(date > (datetime.datetime.now() - datetime.timedelta(hours=1))):
			print("time is after today")
			#if user exists, add new bets to user
			for user in users:
				if(user.gmail == newUser.gmail):
					removeUser(user)
					if(user.addBet(newBet) == 1):
						addUser(user)
						users.clear()
						return {1:"Your bets is added"}
					else:
						addUser(user)
						users.clear()
						return {2: "already betted or somthing"}
			addUser(newUser)
			users.append(newUser)
		else:
			users.clear()
			return {1:"to old"}
		users.clear()
		return {2: "dont"}

api.add_resource(Server, "/lottery/<date1>")

def main():
	scheduler.add_job(id = 'Scheduled Task', func=scheduleTask, trigger="interval", seconds=5)
	scheduler.start()
	app.run(use_reloader=False)

if __name__ == "__main__":
    main()

#göra så att man kan skicka in en lista av datum
#göra så att man kan välja flera datum som man kan välja från och...
#kolla vad som är den tidifaste tiden som man kan önska sig
