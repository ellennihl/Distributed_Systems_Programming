import datetime
import json
from types import SimpleNamespace as Namespace

class User():

    def __init__(self, gmail, bets):
        self.gmail = gmail
        self.bets = bets
        self.bets.sort(key=lambda x: x.date)

    #adds a bet. The bet has just ine date can have manny numbers
    def addBet(self, newBets):
        for bet in self.bets:
            for newBet in newBets:
                if(bet.date == newBet.date):
                    if(1 == bet.addNrs(newBet.numbers)):
                        return 1 #succesfully inserted bet
                    else:
                        return -1 # failed inset
        self.bets.append(newBet)
        self.bets.sort(key=lambda x: x.date)
        return 1

    def removeDate(self, date):
        for bet in self.bets:
            if(bet.date == date):
                self.bets.remove(bet)
                return 1
        return -1

    def toJSON(self):
        #return json.dumps(self, default=lambda o: o.__dict__)
        ret = '{"gmail": "' + self.gmail + '", "bets": ['

        for x in range(len(self.bets)):
            ret = ret + self.bets[x].toJSON()
            if(x+1!=len(self.bets)):
                ret = ret + ', '
        ret = ret + ']}'
        return ret
        #{"gmail": "Jhon@gmail.com", "bets": [{"date": "2020-05-17 13:00:00", "numbers": [9, 10, 11, 12]}, {"date": "2020-05-17 14:00:00", "numbers": [9, 1]}]}

    def __str__(self):
        ret = self.gmail + " "
        for bet in self.bets:
            ret = ret + str(bet) + " "
        return ret

class Bet():
    def __init__(self, date, numbers):
        self.date = date
        self.numbers = numbers

    def __str__(self):
        return "Bet " + str(self.date) + " " + str(self.numbers)

    def __cmp__(self,other):
        return cmp(self.date,other.date)

    def getDatetime(self):
        return datetime.datetime.strptime(self.date, "%Y-%m-%d %H:%M:%S")

    def toJSON(self):
        #return json.dumps(self, default=lambda o: o.__dict__)
        return '{"date": "' + str(self.date) + '", "numbers": ' +str(self.numbers) + '}'
        #{"date": "2020-05-17 13:00:00", "numbers": [10, 100]}

    def addNrs(self,newNrs):
        for nr in self.numbers:
            for newNr in newNrs:
                if(nr == newNr):
                    return -1 #number exists
        self.numbers.extend(newNrs)
        return 1 #succesfull

def betJSONtoObject(jsonStr):
    item = json.loads(jsonStr, object_hook=lambda d: Namespace(**d))
    return Bet(item.date,item.numbers)

def userJSONtoObject(jsonStr):
    item2 = json.loads(jsonStr, object_hook=lambda d: Namespace(**d))
    bets = []
    for bet in item2.bets:
        bets.append(Bet(bet.date,bet.numbers))
    return User(item2.gmail,bets)

def main():
    #bets = [Bet(datetime.datetime(2020, 5, 17, 13), [9,10,11,12]),Bet(datetime.datetime(2020, 5, 17, 14), 54)]
    test = User("Eek@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 13), [9,10,11,12])])
    #test = User("Eek@gmail.com",[Bet("2020-05-17 13:00:00", [9,10,11,12]),Bet("2020-05-17 14:00:00", [9,1])])
    #print(test)

    newBet = Bet(datetime.datetime(2020, 5, 17, 13), [1, 100])
    test.addBet([newBet])
    print(test)
    test.removeDate(datetime.datetime(2020, 5, 17, 13))
    print(test)

    newBet = Bet(datetime.datetime(2020, 5, 17, 13), [10, 100])
    jsonStr = newBet.toJSON()
    print(jsonStr)
    newBet = betJSONtoObject(jsonStr)
    print(newBet.getDatetime())
    print(newBet)

    newUser = User("Jhon@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12]),Bet(datetime.datetime(2020, 5, 17, 13), [9,1])])
    userjsonStr = newUser.toJSON()
    print(userjsonStr)
    newUser = userJSONtoObject(userjsonStr)
    print(newUser)

if __name__ == "__main__":
    main()
