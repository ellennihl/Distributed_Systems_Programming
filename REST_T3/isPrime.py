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

for x in range(100):
    print(str(isPrime(x)) + " " + str(x))
