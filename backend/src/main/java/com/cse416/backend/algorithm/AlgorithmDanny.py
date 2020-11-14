import random
import math
import sys

numDistricts = 3
numPrecincts = 30
compactnessMeasure = ''
populationVariance = 0.017

# precinctList = []

# (1) combine subgraph with random one of its neighbors 
# (2) generate spanning tree of combined subgraph
# (3) go through each edge and see if resulting subgraphs would be acceptable
	# - population limit fits
	# - compactness limit fits
# (4) store the acceptable (or better) edges in a dictionary, choose random one and cut to form two subgraphs. 
# (5) repeat until termination condition reached (10,000 reasonable after testing)

# // Global Variables
subgraphs = [] # holds precincts in it. also holds neighbors with other subgraphs. make name of it the first precinct
stack = []
initializeNeighbors = 0
neighbors = {} # dictionary of keys that have neighbors. initialize via graph, update as we go on

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------
def updateNeighbors(foundNeighbor, oldSubgraph, newSubgraph):

    # print("BEFORE:")
    # print(neighbors)
    # For first iteration, may be string. Convert to list.
    if (type(oldSubgraph) == str):
        oldSubgraph = oldSubgraph.split(', ') # turn string into list (for first iteration)

    # (A) Combine random neighbor's neighbors with oldSubgraph neighbors. Store in an array
        # Delete neighbor from each others list (oldSubgraph and neighbors)
        # Delete repetitive edges
    # (B) Delete the oldSubgraph key and random neighbor key in NEIGHBORS dictionary
    # (C) Add new key to the NEIGHBORS dictionary
    # (D) Replace all instances of oldSubgraph/random neighbor key with newSubgraph key

    # (A)
    neighborNeighbors = neighbors.get(str(foundNeighbor))
    oldSubgraphNeighbors = neighbors.get(str(oldSubgraph))
    combinedNeighbors = oldSubgraphNeighbors + neighborNeighbors

    print("Found Neighbor: " + str(foundNeighbor) + " with neighbors " + str(neighborNeighbors))
    print("Old Subgraph: " + str(oldSubgraph) + " with neighbors " + str(oldSubgraphNeighbors))

    # Remove them from combined neighbors
    for found in combinedNeighbors:
        foundValue = found
        if (type(found) == str):
            foundValue = found.split(', ') # turn string into list (for first iteration)
        if foundValue == oldSubgraph or foundValue == foundNeighbor:
            combinedNeighbors.remove(found)

            # print("Removing " + str(found) + " from " + str(combinedNeighbors)) 
            # print("FoundValue: " + str(foundValue) + " and found: " + str(found))

    # Remove Duplicates
    removeDuplicates = []
    for i in combinedNeighbors:
        if i not in removeDuplicates:
            removeDuplicates.append(i)
    combinedNeighbors = removeDuplicates
    print("Result: " + str(combinedNeighbors))
    print("\n")
    
    # (B)
    neighbors.pop(str(foundNeighbor))
    neighbors.pop(str(oldSubgraph))

    # (C)
    neighbors[str(newSubgraph)] = combinedNeighbors

    # (D)
    print("Neighbors: " + str(neighbors))
    for key in neighbors:
        values = neighbors.get(key)
        editedValue = 0 # 1 if we already edited
        print("We are currently in key: " + str(key))
        for value in values: # note that it adds the value and is then forced to go into it
            foundValue = value
            if (type(value) == str):
                foundValue = value.split(', ') # make sure it's a list
            print("Value: " + str(foundValue))
            if foundValue == oldSubgraph or foundValue == foundNeighbor:
                print("Removing element " + str(value))
                values.remove(value)
                if editedValue == 0: # if we havent added it already
                    print("Adding element " + str(newSubgraph))
                    values.append(newSubgraph)
                    editedValue = 1
    return


# def getNeighbors():

# (1) Depth-First Search

def findCombine_driver(graph):
    global neighbors, subgraphs
    initialSubgraphs = list(graph.keys()) # takes precinct key

    # Convert into list of lists
    for sub in initialSubgraphs:
        sub = sub.split(', ')
        subgraphs.append(sub)

    # Initialize neighbors from graph
    for i in range(len(graph.values())):
        key = subgraphs[i] # get key
        value = graph.values()[i]['neighbors'] # get value
        neighbors[str(key)] = value # keys stored as strings
    # print(neighbors)

    while len(subgraphs) != numDistricts: 
        for subgraph in subgraphs: # for each node, randomly select neighbors 
            findCombine(graph, subgraph)
    # When we're done, let's print the subgraphs:
    print("\n")
    counter = 0
    for subgraph in subgraphs:
        print("District " + str(counter) + " --> " + str(subgraph))
        counter = counter + 1

def findCombine(graph, subgraph):
    global subgraphs
    subgraphNeighbors = neighbors.get(str(subgraph))
    # print("\n")
    # print(subgraphs)
    # print("Subgraph : " + str(subgraph))
    # print("Subgraph neighbors : " + str(subgraphNeighbors))
    randomNeighbor = random.choice(subgraphNeighbors) # get random neighbor
    # print("Chosen neighbor: " + randomNeighbor + "\n")

    # ADD (COMBINE) NEIGHBOR TO THE CURRENT SUBGRAPH
    oldSubgraph = list(subgraph) # copy contents 
    subgraph.append(randomNeighbor) # add neighbor to subgraph
    # DELETE IT FROM THE SUBGRAPHS LIST
    if (type(randomNeighbor) == str):
        randomNeighbor = randomNeighbor.split(', ') # turn string into list (for first iteration)
    neighborIndex = subgraphs.index(randomNeighbor)
    subgraphs.pop(neighborIndex) # delete it from subgraphs dictionary

    # UPDATE THE REFERENCES OF THIS NEIGHBOR
    updateNeighbors(randomNeighbor, oldSubgraph, subgraph) 
    return

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        MAIN FUNCTION
# ------------------------------------------------------------------
# ------------------------------------------------------------------

def main():
    graph5 = {
        "000":{
            "population":10,
            'neighbors': [
                "001", "002"
            ],
        },
        "001": {
            "population":10,
            'neighbors': [
                "000", "003"
            ],
        },
        "002":{
            "population":10,
            'neighbors': [
                "000", "003"
            ],
        },
        "003": {
            "population":10,
            'neighbors': [
                "001", "002", "004"
            ],
        },
        "004":{
            "population":10,
            'neighbors': [
               "003", "005"
            ],
        },
         "005":{
            "population":10,
            'neighbors': [
               "004"
            ],
        }
    }
    print(findCombine_driver(graph5))


if __name__ == "__main__":
    main()

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------

def combineClusters(cluster1, cluster2):
    print("Combining clusters " + cluster1 + " and " + cluster2)
    if n == 0:
        return graph
    dfs_value = dfs(graph)
    edges = dfs_value["edges"]
    visited = dfs_value["visited"]
    degrees = dfs_value["degrees"]
    dict_district_one = updateGraphNeigbors(graph,district_one)
    dict_district_two = updateGraphNeigbors(graph,district_two)

# def create_district_struct(num_of_districts:int):
#     district_struct = {}
#     for i in range(1,num_of_districts+1):
#         struct = {"neighbors":[],"precincts":[]}
#         district_struct.setdefault(str(i),struct)
#     return district_struct

# def getRandomListElement(arr:list):
#     random_node = random.choice(arr)
#     return random_node