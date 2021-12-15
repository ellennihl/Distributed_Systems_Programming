import threading
from threading import Timer
import time
import random
from server import *
exitFlag = 0

def hello():
    print("Hello World!")


class myThread (threading.Thread):
   # def __init__(self, threadID, name):
   #    threading.Thread.__init__(self)
   #    self.threadID = threadID
   #    self.name = name
   #
   # def run(self):
   #     print ("Starting " + self.name)
   #     count = 0
   #     #while(exitFlag == 0):
   #     while(count < 10 and exitFlag == 0):
   #         count = count + 1
   #         time.sleep(4)
   #         users = getUsers()
   #         winningNr = random.randrange(0, 255)
   #         print(str(winningNr))
   #         for user in users:
   #             print("in thred " + str(user))
   #         users.clear()

   def __init__(self, interval, f, *args, **kwargs):
       self.interval = interval
       self.f = f
       self.args = args
       self.kwargs = kwargs

       self.timer = None

   def callback(self):
       self.f(*self.args, **self.kwargs)
       self.start()

   def cancel(self):
       self.timer.cancel()

   def start(self):
       self.timer = Timer(self.interval, self.callback)
       self.timer.start()
