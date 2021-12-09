#inspiuterd by https://www.youtube.com/watch?v=GMppyAPbLYk

from flask import Flask
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
api = Api(app)

number_put_args = reqparse.RequestParser()
number_put_args.add_argument("isPrime", type=int, help="No isPrime", required=True)

numbers = {}

def abort_if_number_not(number):
	if number not in numbers:
		abort(404, message = "Number not in database...")

class Primes(Resource):
	def get(self,number):
		abort_if_number_not(number)
		return {number : numbers[number]}

	def put(self,number):
		args = number_put_args.parse_args()
		numbers[number] = args
		return {number : numbers[number]}, 201

api.add_resource(Primes, "/prime/<int:number>")

if __name__ == "__main__":
	app.run(debug=True)
