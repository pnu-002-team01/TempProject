import json
import math
import matplotlib.pyplot as plt
from collections import defaultdict

DATA_PATH = "C:\\Users\\illak\\git\\TempProject\\data\\problems\\"
JSON_TARGET = ["percentCorrect", "correctNumber", "submittedNumber", "solvedPeopleNumber", "problemNumber"]
AVERAGE_CORRECT = 54.405

problemData = defaultdict(list)
problemData['percentCorrect'] = []
problemData['correctNumber'] = []
problemData['submittedNumber'] = []
problemData['solvedPeopleNumber'] = []
problemData['problemNumber'] = []

def transform(target,result):
    ret = result
    if target == "percentCorrect":
        ret = ret[:-1]
        ret = float(ret)

    elif target == "correctNumber":
        ret = int(ret)

    elif target == "submittedNumber":
        ret = int(ret)
    
    elif target == "solvedPeopleNumber":
        ret = float(ret)
        if ret >= 100: 
            ret = 10000/(math.log2(ret))
            ret = int(ret)
        else:
            ret = -1

    return ret
    


def collectAllFromJSON():
    for i in range(1000,17300):
        problemJson = str(i)+".json"
        jsonFilePath = DATA_PATH+problemJson
        fileContent = ""
        try:
            f = open(jsonFilePath,"r", encoding="utf-8") 
            fileContent = f.read()
            jsonDict = json.loads(fileContent)
            for key in JSON_TARGET:
                problemData[key].append(jsonDict[key])
        except FileNotFoundError:
            continue

def collectAllFromRecord():
    for target in JSON_TARGET:
        collectFromRecord(target)

def collectFromRecord(target):
    with open(target+".txt","r",encoding="utf-8") as f:
        lines = f.readlines();
        for line in lines:
            result = line[:-1]
            problemData[target].append(result)

def showHist(target):
    plt.hist(target,bins=1650,range=(450,2100))
    plt.show()
    
def record():
    for target in JSON_TARGET:
        with open(target+".txt","w",encoding="utf-8") as f:
            result = ""
            for x in problemData[target]:
                result += x+"\n"
            f.write(result)

def scrap():
    rating = [0] * 13835
    for i in range(0,13835):
        rating[i] = transform("solvedPeopleNumber",problemData["solvedPeopleNumber"][i])
        if rating[i] != -1:
            rating[i] = int(rating[i] * (100 - transform("percentCorrect",problemData["percentCorrect"][i]) + AVERAGE_CORRECT) / 100)
    with open("ratings.txt","w",encoding="utf-8") as f:
        result = ""
        for i in range(0,13835):
            result += problemData["problemNumber"][i] + ":" + str(rating[i]) + "\n"
        f.write(result)
    rating.sort()
    print(rating)
    showHist(rating)

def main():
    collectAllFromRecord()
    scrap()


if __name__ == '__main__':
    main()

