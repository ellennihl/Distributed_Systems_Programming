import datetime
import json
from types import SimpleNamespace as Namespace

#-------------------------------------------
# A user signes upp with a gmail. The gmail is the unique identifier
# Every user can make bets specifing date, time and what numbers they bet on
# The bets are stored in a list sorted by date
#-------------------------------------------
class User():

    def __init__(self, gmail, bets):
        self.gmail = gmail #the mail is also the unique identifier
        self.bets = bets #A list of bets that the user has made
        self.bets.sort(key=lambda x: x.date)

    #adds a bet. The bet has one date and can have manny numbers
    def addBet(self, newBets):
        for bet in self.bets:
            for newBet in newBets:
                if(str(bet.date) == str(newBet.date)):
                    #take away duplicates
                    nodup = list(dict.fromkeys(newBet.numbers))
                    print(nodup)
                    if(1 == bet.addNrs(nodup)):
                        return 1 #succesfully inserted bet
                    else:
                        return -1 # failed inset
        self.bets.append(newBet)
        self.bets.sort(key=lambda x: x.date)
        return 1 #succesfully inserted bet

    #removeDate will remove all bets set on a specifik date
    def removeDate(self, date):
        for bet in self.bets:
            if(bet.date == date):
                self.bets.remove(bet)
                return 1
        return -1

    #toJason converts the user to jason format
    def toJSON(self):
        ret = '{"gmail": "' + self.gmail + '", "bets": ['

        for x in range(len(self.bets)):
            ret = ret + self.bets[x].toJSON()
            if(x+1!=len(self.bets)):
                ret = ret + ', '
        ret = ret + ']}'
        return ret
        #example
        #{"gmail": "Jhon@gmail.com", "bets": [{"date": "2020-05-17 13:00:00", "numbers": [9, 10, 11, 12]}, {"date": "2020-05-17 14:00:00", "numbers": [9, 1]}]}

    def __str__(self):
        ret = self.gmail + " "
        for bet in self.bets:
            ret = ret + str(bet) + " "
        return ret

#-------------------------------------------
# The Bet class conatins
# date: the date and time that is betted on
# numbers: is a list of numbers that is betted on
#-------------------------------------------
class Bet():
    def __init__(self, date, numbers):
        self.date = date
        self.numbers = numbers

    def __str__(self):
        return "Bet " + str(self.date) + " " + str(self.numbers)

    def __cmp__(self,other):
        return cmp(self.date,other.date)

    #returns a datetime object of the date
    def getDatetime(self):
        return datetime.datetime.strptime(self.date, "%Y-%m-%d %H:%M:%S")

        #toJason converts the Bet to json string
    def toJSON(self):
        #return json.dumps(self, default=lambda o: o.__dict__)
        #no duplicates
        nodup = list(dict.fromkeys(self.numbers))
        return '{"date": "' + str(self.date) + '", "numbers": ' +str(nodup) + '}'
        #{"date": "2020-05-17 13:00:00", "numbers": [10, 100]}

    #addNrs takes in a list uf numbers and adds these numbers
    #The numbers will be added if all newNr are in between 0 and 255 and
    #the numbers does not exist.
    #returns 1 if added succesfully and -1 of not
    def addNrs(self,newNrs):
        for nr in self.numbers:
            for newNr in newNrs:
                if(nr == newNr or newNr >255 or newNr<0):
                    return -1 #number exists/not between 255 and 0
        self.numbers.extend(newNrs)
        return 1 #succesfull

#betJSONtoObject accepts a json string and converts this to a Bet object
# returns a Bet object
def betJSONtoObject(jsonStr):
    item = json.loads(jsonStr, object_hook=lambda d: Namespace(**d))
    return Bet(item.date,item.numbers)

#userJSONtoObject converts a json string to a User object
#returns a User
def userJSONtoObject(jsonStr):
    item2 = json.loads(jsonStr, object_hook=lambda d: Namespace(**d))
    bets = []
    for bet in item2.bets:
        test = bet.date
        test = datetime.datetime.strptime(test, '%Y-%m-%d %H:%M:%S')
        test = test.replace(minute=0, second=0)
        bets.append(Bet(test,bet.numbers))
    return User(item2.gmail,bets)

def main():
    #bets = [Bet(datetime.datetime(2020, 5, 17, 13), [9,10,11,12]),Bet(datetime.datetime(2020, 5, 17, 14), 54)]
    test = User("Eek@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 13), [9,10,11,12])])
    #test = User("Eek@gmail.com",[Bet("2020-05-17 13:00:00", [9,10,11,12]),Bet("2020-05-17 14:00:00", [9,1])])
    #print(test)

    newBet = Bet(datetime.datetime(2020, 5, 17, 14), [1, 100])
    newBet2 = Bet(datetime.datetime(2020, 5, 17, 13), [1, 100])
    test.addBet([newBet])
    test.addBet([newBet2])
    print(test)
    test.removeDate(datetime.datetime(2020, 5, 17, 13))
    print(test)

    # newBet = Bet(datetime.datetime(2020, 5, 17, 13), [10, 100])
    # jsonStr = newBet.toJSON()
    # print(jsonStr)
    # newBet = betJSONtoObject(jsonStr)
    # print(newBet.getDatetime())
    # print(newBet)
    #
    # newUser = User("Jhon@gmail.com",[Bet(datetime.datetime(2020, 5, 17, 14), [9,10,11,12]),Bet(datetime.datetime(2020, 5, 17, 13), [9,1])])
    # userjsonStr = newUser.toJSON()
    # print(userjsonStr)
    # newUser = userJSONtoObject(userjsonStr)
    # print(newUser)

if __name__ == "__main__":
    main()
