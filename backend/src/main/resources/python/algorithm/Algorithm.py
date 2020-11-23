import json
import random

# TESTING PURPOSES --------------

# print("Running. . .")
# # Opening JSON summary
# f = open('test_data.json',) 

# # returns JSON object as  
# # a dictionary 
# data = json.load(f) 

# # Iterating through the json 
# # list 
# for i in data['data']: 
#     print(i) 

# # print(data['data']['state'])
# # print(data['data']['job'])

# state = data['data']['state']
# print(state['stateName'])

# test_populationDiff = data['data']['job']['populationDifference']
# print(test_populationDiff)

# # Closing summary
# f.close() 

# TESTING PURPOSES --------------

# def Algorithm():

# print("TEST")
# list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
# size = len(list)
# list2 = list[0:int(size/2)]
# list3 = list[int(size/2):size]
# print(list2)
# print(list3)

def initiateAlgorithm(): # Main function
    print("Starting Algorithm. . .")

    # global state
    # global job

    # global populationDifference
    # global compactness
    # global numOfDistrictingPlans
    # global numOfDistricts
    # global precincts

    getData()

    global districts
    # districts = combinePrecincts(precincts)

    # print(precincts) # DEBUG
    # print(len(precincts)) # DEBUG
    # print(precincts[1]) # DEBUG
    # print(precincts[1]["neighbors"][0]) # DEBUG
    # print(len(precincts[1]["neighbors"])) # DEBUG

    # ranNum = random.randint(0, len(precincts)-1)
    # keys = list(precincts.keys())
    # print(keys)
    # print(keys[ranNum])
    # print(precincts["001"])

    # testList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] # DEBUG

    newList = combinePrecincts(precincts) # DEBUG
    for i in newList: # DEBUG
        print("New District--------") # DEBUG
        for j in i: # DEBUG
            print(j["precinctName"]) # DEBUG
    print(newList) # DEBUG

    # counter = 0
    # while (counter < numOfDistrictingPlans):
    #     combinePrecincts(precincts)
    #     counter = counter + 1

    # -- inputs run: Map<State, Job>


# |||||||||||||||||||||||||||||| Helper functions ||||||||||||||||||||||||||||||

def getData():

    print("Retrieving data. . .")
    # Opening JSON summary
    f = open('test_data.json',) 

    # returns JSON object as  
    # a dictionary 
    data = json.load(f) 

    global state
    state = data['data']['state']
    global job
    job = data['data']['job']

    global clientPopulationDifference
    clientPopulationDifference = data['data']['job']['populationDifference']
    global clientCompactness
    clientCompactness = data['data']['job']['clientCompactness']
    global numOfDistrictingPlans
    numOfDistrictingPlans = data['data']['job']['numOfDistrictingPlans']
    global numOfDistricts
    numOfDistricts = data['data']['job']['numOfDistricts']
    global precincts
    precincts = data['data']['state']['precincts']


    # Closing summary
    f.close() 


def combinePrecincts(precinctList): # Use Case #29
    print("Combining precincts (districts needed: " + str(numOfDistricts) + "). . .")
    counter = 0 # Counter for loop
    randomIndex = 0 # Random number used for randomization of precinct combining
    precinctKeys = list(precinctList.keys()) # List of keys to randomly select from
    subGraphs = [] # List of newly-created districts
    takenPrecincts = [] # Precincts already combined (by FIPS Code)
    precinctNeighbors = [] # List of selected precinct's neighbors
    precinct1 = None # First precinct being combined
    precinct2 = None # Second precinct being combined

    

    while (counter < numOfDistricts):
        newDistrict = [] # New district object to be created
        listSize = len(precinctList)
        print(listSize) # DEBUG

        precinctsFound = False # Checks whether two precincts chosen to combine are not already taken
        while (not precinctsFound): # While two acceptable precincts have not yet been found
            randomIndex = random.randint(0, listSize-1)
            # print("DEBUG 1") # DEBUG
            # print(randomIndex) # DEBUG
            precinctCode = precinctKeys[randomIndex]
            precinct1 = precinctList[precinctCode] # Precinct 1 chosen randomly
            # print("DEBUG 2") # DEBUG
            # print(precinct1) # DEBUG
            if (precinct1["precinctFIPSCode"] not in takenPrecincts): # If not already taken
                precinctNeighbors = precinct1["neighbors"]
                # print("DEBUG 3") # DEBUG
                # print(precinctNeighbors) # DEBUG
                randomIndex = random.randint(0, len(precinctNeighbors)-1)
                # print("DEBUG 4")
                # print(randomIndex) # DEBUG
                precinct2 = precinctNeighbors[randomIndex]
                # print("DEBUG 5")
                # print(precinct2) # DEBUG
                if (precinct2["precinctFIPSCode"] not in takenPrecincts): # If neighbor not already taken
                    takenPrecincts.append(precinct1["precinctFIPSCode"])
                    takenPrecincts.append(precinct2["precinctFIPSCode"])
                    # print("DEBUG 6") # DEBUG
                    # print(takenPrecincts) # DEBUG
                    precinctsFound = True # Two acceptable precincts found - exit while-loop

        # Note: Objects are not the same!!! Precinct 7 != Precinct6->neighbors->precinct7
        newDistrict.append(precinct1)
        newDistrict.append(precinct2)
        subGraphs.append(newDistrict)
        counter = counter + 1

    return subGraphs
    # Or globally set districts to subGraphs(result)

def checkNeighbors():
    print("Checking neighbors. . .")
    # Checks the new splitted precinct list to see if they are all neighbors




def balanceGraph():
    print("Balancing graph. . .")
    i = 0
    districts = graphOfDistricts = {
        1:{
            'neighbors': [
                2
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
        2: {
            'neighbors': [
                2
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
        }
    
    while i < 100:
        optimizeDistrictPair(districts)
        i = i + 100;

def isGraphBalanced(districts):
    print("Is graph balanced?")

    for i in districts:
        if  i["compactness"] > (clientCompactness * 1.05) | i["compactness"] < (clientCompactness * 0.95):
            return False
        elif i["populationDifference"] > (clientPopulationDifference * 1.05) | i["populationDifference"] < (clientPopulationDifference * 0.95):
            return False
        else:
            return True
#     # Check compactness is within the limit requested
#     # Check if populationDifference lies within 1/2 of user's input

def optimizeDistrictPair(districts : dict):
    pair = getRandomDistrictPair(districts) # List of two districts
    #tree = createSpanningTree(pair)
    # edgesToCut = generateEdgesToCut()
#     # Do something

def getRandomDistrictPair(districts : dict):
    """Returns list of two adjancent district. Current implementaton returns key of district dictionary. TODO: Return list of dict objects such as [{1:{...}},{2:{...}}]"""
    print("Selecting random disrict pair. . .")
    districtOne = random.randrange(1,len(districts)+1)
    districtOnesNeighbors = districts.get(districtOne)['neighbors']
    districtTwo = random.choice(districtOnesNeighbors)
    return [districtOne, districtTwo]

def createSpanningTree(districtPair : list):
    print("Creating spanning tree. . .")
    combinedPrecinct = districtPair[0]['precinct'].append(districtPair[1]['precinct'])
    return combinedPrecinct
#     # Do something
#     # -- inputs pair: District[]
#     # -- returns List<Precinct>

def generateEdgesToCut(tree, pair):
    print("Generating edges to cut. . .")
#     # Do something
#     # -- inputs tree: List<Precinct>, pair: District[]
#     # -- returns List<Precinct>

def measureCompactness(district): # Polsby-Popper
    print("Measuring compactness. . .")
#     # Do something
#     # -- inputs district: District
#     if pp value not in range of compactness we entered, rebalance it for that district (combine smaller ones or split larger ones)
#     look into polygon python libraries
#     # -- returns Double

def measurePopulationDifference(district):
    print("Measuring population difference. . .")
#     # Do something
#     # -- inputs district: District
#     # -- returns Double
# within 1.7% of ideal population
# idealPopulationOfADistrict = totalPopulation/numOfDistricts
# return ideal - actual (abs value, difference between)


# state = None # State object (includes )
# job = None # Job object (includes compactness, class attributes...)

# combinedSubgraph = None
# districts = None

# populationDiff = None
# compactness = 0.0
# numOfDistrictingPlans = 0
# numOfDistricts = None
# precincts = None

initiateAlgorithm()


# Note: precincts included in states or sent through json separately??

# Note: do we need a main function? This main function will call getData() 
# to get the state and job data objects, then pass them through to initiateAlgorithm()

# districting = None # DistrictingPlan object ?????



####################
graphOfPrecinct = {
        "000":{
            'neighbors': [
                2, 3
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
        },
        "001": {},
        }
graphOfDistricts = {
        1:{
            'neighbors': [
                2
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
        2: {
            'neighbors': [
                1, 3
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
        3: {
            'neighbors': [
                2, 4
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
        4: {
            'neighbors': [
                3
            ],
            'compactness': 0.5,
            'populationDiff': 0.3,
            'precincts': [
                "000",
                "001",
                "002"
            ]
            },
            
            
        }