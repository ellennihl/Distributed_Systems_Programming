import requests
import random

def isPrime(number):
    if(number == 1 or number == 2):
        return 1
    if(number%2 == 0):
        return 0
    for i in range(2, int(number/2)+1):
        if(number % i == 0):
            return 0
    return 1


BASE = "http://127.0.0.1:5000/"
number = 100;
response = requests.get(BASE + "prime/" + str(number))
if(response.status_code == 404):
    temp = isPrime(number)
    print("Add new item")
    response = requests.put(BASE + "prime/" + str(number), {'isPrime' : temp})
print(response.json())
