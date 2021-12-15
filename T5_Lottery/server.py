from flask import Flask, jsonify
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy

from Hour import *

from User import *
from Pass import Pass
import datetime

users = [User("Eejk@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12])]),User("Eek@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12])])]
f = open("Database.txt", "w")
f.write(str(users[0].toJSON()) + "\n")
f.write(str(users[1].toJSON()) + "\n")
f.close()

app = Flask(__name__)
api = Api(app)

parser = reqparse.RequestParser()
parser.add_argument("gmail", type=str, help="No gmail", required=True)
parser.add_argument("date", type=str, help="No date", required=True)
parser.add_argument("numbers", type=int, action='append', help="No numbers", required=True)
#users = [User("Eek@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12])])]


def removeUser(user):
	with open("Database.txt", "r") as f:
		lines = f.readlines()
	with open("Database.txt", "w") as f:
		for line in lines:
			if line.strip("\n") != user.toJSON():
				f.write(line)

def getUsers():
	user = []
	f = open("Database.txt", "r")
	for x in f:
		users.append(userJSONtoObject(x))
	f.close()
	return users

def addUser(newUser):
	f = open("Database.txt", "a")
	f.write(newUser.toJSON() + "\n")
	f.close()

def abort_if_date_to_early(number):
	if(number > len(users)):
		abort(200, message = "Number not in database...")


class Server(Resource):
	#get the period of drawings between given dates
	#returns the drawn lucky number, the number of winners, and the winnings totals
	#can also return error
	def get(self,number):
		users = getUsers()
		abort_if_date_to_early(number)
		print("Id in get" + str(id(users)))
		return users[number].toJSON()

	#if the user has not betted. Add the use and add the bet apropriotly
	#if the user already exists update the bets in the user and add it to drawings
	def post(self,number):
		users = getUsers()
		args = parser.parse_args()
		date = datetime.datetime.strptime(args.date, '%Y-%m-%d %H:%M:%S')
		date = date.replace(minute=0, second=0)# + datetime.timedelta(hours=1)
		if(date > datetime.datetime.now()):
			print("time is after today")
			newBet = [Bet(date, args.numbers)]
			newUser = User(args.gmail,newBet)
			#if user exists, add new bets to user
			for user in users:
				if(user.gmail == newUser.gmail):
					removeUser(user)
					if(user.addBet(newBet) == 1):
						addUser(user)
						return "Your bets is added"
					else:
						addUser(user)
						return "Error: already betted or somthing"
			addUser(newUser)
			users.append(newUser)
		else:
			print("older time")
		return newUser.toJSON(), 201

api.add_resource(Server, "/lottery/<int:number>")

def main():
	#thread1 = myThread(1, "Drawing")
	#thread1.start()
	#app.run()

	t = myThread(10, hello)
	t.start()
	app.run(debug=True)

if __name__ == "__main__":
    main()

#global variable did not work so i have a file as a database instead
#när man gör ny bet kan man skriva in fler av samma nummer
