import random
import math
import sys
from copy import deepcopy

numDistricts = 2
numPrecincts = 30
compactnessMeasure = ''
populationVariance = 0.017
terminationLimit = 1

# (1) combine subgraph with random one of its neighbors 
# (2) generate spanning tree of combined subgraph
# (3) go through each edge and see if resulting subgraphs would be acceptable
	# - population limit fits
	# - compactness limit fits
# (4) store the acceptable (or better) edges in a dictionary, choose random one and cut to form two subgraphs. 
# (5) repeat until termination condition reached (10,000 reasonable after testing)

# // Global Variables
subgraphs = [] # holds precincts in it. also holds neighbors with other subgraphs. make name of it the first precinct
neighbors = {} # dictionary of keys that have neighbors. initialize via graph, update as we go on
precincts = [] # initial list of precinct neighbors for use later.
precinctsNeighbors = {} 

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------
def updateNeighbors(foundNeighbor, oldSubgraph, newSubgraph):
    global neighbors, subgraphs
    # For first iteration, may be string. Convert to list.
    if (type(foundNeighbor) == str):
        foundNeighbor = foundNeighbor.split(', ') # turn string into list (for first iteration)

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

    # Remove themselves from combined neighbors
    deleteItems = [] # to avoid skipping values
    for found in combinedNeighbors:
        foundValue = found
        if (type(foundValue) == str):
            foundValue = foundValue.split(',') # turn string into list (for first iteration)
        if foundValue == oldSubgraph or foundValue == foundNeighbor:
            deleteItems.append(found)
    for item in deleteItems:
        combinedNeighbors.remove(item)

    # Remove Duplicates
    removeDuplicates = []
    for i in combinedNeighbors:
        if i not in removeDuplicates:
            removeDuplicates.append(i)
    combinedNeighbors = removeDuplicates
    
    # (B)
    neighbors.pop(str(foundNeighbor))
    neighbors.pop(str(oldSubgraph))

    # (C)
    neighbors[str(newSubgraph)] = combinedNeighbors

    # (D)
    for key in neighbors:
        values = neighbors.get(key)
        editedValue = 0 # 1 if we already edited
        deleteItems = [] # to NOT SKIP VALUES
        for value in values: # note that it adds the value and is then forced to go into it
            foundValue = value
            if (type(value) == str):
                foundValue = value.split(', ') # make sure it's a list
            if foundValue == oldSubgraph or foundValue == foundNeighbor:
                deleteItems.append(value)
                if editedValue == 0: # if we havent added it already
                    values.append(newSubgraph)
                    editedValue = 1
        for item in deleteItems: # do this after to avoid skipping in previous for loop
            values.remove(item)
    return

# (1) Find Combine Algorithm

def findCombine_driver(graph):
    global neighbors, subgraphs, numDistricts, terminationLimit
    initialSubgraphs = list(graph.keys()) # takes precinct key

    # Convert into list of lists
    for sub in initialSubgraphs:
        sub = sub.split(', ')
        subgraphs.append(sub)
        sub2 = deepcopy(sub)
        precincts.append(sub2)

    # Initialize neighbors from graph
    for i in range(len(graph.values())):
        key = subgraphs[i] # get key
        value = graph.values()[i]['neighbors'] # get value
        value2 = graph.values()[i]['neighbors'] # get value
        neighbors[str(key)] = value # keys stored as strings
        precinctsNeighbors[str(deepcopy(key))] = deepcopy(value2)

    # USE CASE #29 GENERATE SEED DISTRICTING
    while len(subgraphs) != numDistricts: 
        for subgraph in subgraphs: # for each precinct, randomly select neighbors
            findCombine(graph, subgraph)
            if len(subgraphs) == numDistricts:
                break

    # 35. Repeat the steps above until you generate satisfy the termination condition (required)
    print("\n")
    print("Precincts: " + str(precincts))
    print("Precincts Neighbors " + str(precinctsNeighbors))
    for i in range(terminationLimit):
        algorithm()

    # When we're done, let's print the subgraphs:
    counter = 1
    print("Neighbors: " + str(neighbors))
    print("\n")
    for subgraph in subgraphs:
        print("District " + str(counter) + " --> " + str(subgraph))
        counter = counter + 1
    print("\n")
    return

def algorithm():
    global subgraphs, neighbors, precincts, precinctsNeighbors

    # USE CASE #30 --> Generate a random districting satisfying constraints (required)

    # Random Subgraph, Random neighbor
    randomSubgraph = random.choice(subgraphs)
    subgraphNeighbors = neighbors.get(str(randomSubgraph))

    randomNeighbor = random.choice(subgraphNeighbors) # get random neighbor

    # Combine both subgraphs into one

    subgraphsCombined = []
    if type(randomSubgraph) == str:
        subgraphsCombined.append(randomSubgraph)
    else:
        for i in randomSubgraph:
            subgraphsCombined.append(i)

    if type(randomNeighbor) == str:
        subgraphsCombined.append(randomNeighbor)
    else:
        for j in randomNeighbor:
            subgraphsCombined.append(j)
    print(str(subgraphsCombined))

    # USE CASE #31 --> Generate a spanning tree of the combined sub-graph above (required) 
    # DFS for combined subgraph (spanning tree)
    randomStart = random.choice(subgraphsCombined) # randomly select a start
    visited = [randomStart]
    stack = [randomStart]
    edges = []
    currentPrecinct = randomStart
    while len(visited) < len(precincts):
        pop_stack = True
        neighborsPrecinct = precinctsNeighbors.get(str(currentPrecinct.split(', '))) # get neighbors of precinct
        for precinct in neighborsPrecinct:
            if precinct not in visited:
                visited.append(precinct)
                stack.append(precinct)
                # Create the edge and add it 
                newList = []
                vertexOne = currentPrecinct
                vertexTwo = precinct
                if (type(vertexOne) == str):
                    vertexOne = vertexOne.split(', ')
                if (type(vertexTwo) == str):
                    vertexTwo = vertexTwo.split(', ')
                newList = vertexOne + vertexTwo
                edges.append(newList) # assume creates edge
                currentPrecinct = precinct
                pop_stack = False
                break
        if pop_stack: # go back
            if stack:
                stack.pop()
                currentPrecinct = stack[-1]
    values = { "visited": visited, "edges": edges}
    print(str(values))

    # USE CASE #32 --> Calculate the acceptability of each newly generated sub-graph (required) 
    # USE CASE #33 --> Generate a feasible set of edges in the spanning tree to cut (required) 
    # USE CASE #34 --> Cut the edge in the combined sub-graph (required)

def findCombine(graph, subgraph):
    global subgraphs, neighbors
    subgraphNeighbors = neighbors.get(str(subgraph))
    randomNeighbor = random.choice(subgraphNeighbors) # get random neighbor

    # ADD (COMBINE) NEIGHBOR TO THE CURRENT SUBGRAPH
    oldSubgraph = list(subgraph) # copy contents 
    addNeighbor = randomNeighbor
    if (type(randomNeighbor) == str):
        addNeighbor = addNeighbor.split(', ') # turn string into list (for first iteration)
    newList = []
    for i in subgraph:
        newList.append(i)
    for j in addNeighbor:
        newList.append(j)

    subgraphIndex = subgraphs.index(subgraph)
    subgraphs[subgraphIndex] = newList # add neighbor to subgraph

    # DELETE IT FROM THE SUBGRAPHS LIST
    neighborIndex = subgraphs.index(addNeighbor)
    subgraphs.pop(neighborIndex) # delete it from subgraphs dictionary

    # UPDATE THE REFERENCES OF THIS NEIGHBOR
    updateNeighbors(randomNeighbor, oldSubgraph, newList) 
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
    findCombine_driver(graph5)
    return

if __name__ == "__main__":
    main()

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------