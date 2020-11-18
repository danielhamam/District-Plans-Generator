import random
import math
import sys
from copy import deepcopy

numDistricts = 3
numPrecincts = 30
compactnessMeasure = ''
populationVariance = 0.017
terminationLimit = 2
ideal_population = 15
compactness_lower_bound = 0.1
compactness_upper_bound = 1.3


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
subgraphsCombined = []

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
    
# (1) Algorithm

def algorithm_driver(graph):
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
        for subgraph in subgraphs: 
            findCombine(graph, subgraph)
            if len(subgraphs) == numDistricts:
                break

    # 35. Repeat the steps above until you generate satisfy the termination condition (required)
    for i in range(terminationLimit):
        algorithm(graph)

    # When we're done, let's print the subgraphs:
    counter = 1
    print("\n")
    for subgraph in subgraphs:
        print("District " + str(counter) + " --> " + str(subgraph))
        counter = counter + 1
    print("\n")
    return

def algorithm(graph):
    global subgraphs, neighbors, precincts, precinctsNeighbors, subgraphsCombined

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
    print("Subgraphs: " + str(subgraphs))
    print("Combined Subgraph: " + str(subgraphsCombined)) 

    # Let's also combine the neighbors of these subgraphs 
    updateNeighbors(randomNeighbor, randomSubgraph, subgraphsCombined)
    print("Neighbors: " + str(neighbors))

    # DELETE THEM FROM THE SUBGRAPHS LIST
    if type(randomNeighbor) == str:
        randomNeighbor = randomNeighbor.split(', ')
    if type(randomSubgraph) == str:
        randomSubgraph = randomSubgraph.split(', ')

    neighborIndex = subgraphs.index(randomNeighbor)
    subgraphs.pop(neighborIndex)
    subgraphIndex = subgraphs.index(randomSubgraph)
    subgraphs.pop(subgraphIndex)

    # ADD COMBINED SUBGRAPHS TO THE SUBGRAPHS LIST
    subgraphs.append(subgraphsCombined)

    # USE CASE #31 --> Generate a spanning tree of the combined sub-graph above (required) 
    spanning_tree = generate_spanning_tree()

    # USE CASE #32 --> Calculate the acceptability of each newly generated sub-graph (required) 
    # USE CASE #33 --> Generate a feasible set of edges in the spanning tree to cut (required) 
    acceptableEdges = check_acceptability(spanning_tree, subgraphsCombined, graph)

    # USE CASE #34 --> Cut the edge in the combined sub-graph (required)
    targetCut = random.choice(acceptableEdges) # choose random edge to cut
    return cut_acceptable(acceptableEdges, targetCut)

def generate_spanning_tree():
    global subgraphs, neighbors, precincts, precinctsNeighbors, subgraphsCombined
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
    spanning_tree = { "visited": visited, "edges": edges}
    print("\n" + str(spanning_tree))
    return spanning_tree

def cut_acceptable(spanning_tree, targetCut):
    global subgraphsCombined

    subgraph_one = [] # New subgraph 1
    subgraph_two = [] # New subgraph 2
    total_population_one = 0 # Total population of new subgraph 1
    total_population_two = 0 # Total population of new subgraph 2
    compactness_one = 0.4 # Compactness of new subgraph 1
    compactness_two = 0.4 # Compactness of new subgraph 2

    precinct_one = targetCut[0]
    subgraph_one.append(precinct_one)
    precinct_two = targetCut[1]
    subgraph_two.append(precinct_two)

    # Look into these more
    for i in precinctsNeighbors[str(precinct_one.split(', '))]: # Adds precincts to new subgraph 1
        if i != targetCut[1] and i not in precinctsNeighbors[str(precinct_two.split(', '))]:
            subgraph_one.append(i)
    for i in precinctsNeighbors[str(precinct_two.split(', '))]: # Adds precinct to new subgraph 1
        if i != targetCut[0] and i not in precinctsNeighbors[str(precinct_one.split(', '))]:
            subgraph_two.append(i)

    # Attach precincts to subgraph 1 (start --> first precinct in cut)
    # Attach precincts to subgraph 2 (second precinct in cut --> end)
    intoSecond = 0
    for i in subgraphsCombined:
        if intoSecond == 0:
            subgraph_one.append(i)
        elif intoSecond == 1:
            subgraph_two.append(i)
        if i == targetCut[0]:
            intoSecond = 1

    print("Cut edge --> " + str(targetCut))
    print("Combined Subgraph: " + str(subgraphsCombined))
    print("Combined Subgraph Neighbors: " + str(neighbors.get(str(subgraphsCombined))))
    print("Subgraph one --> " + str(subgraph_one))
    print("Subgraph two --> " + str(subgraph_two))

    exit()

    # Let's add these subgraphs to the subgraphs list, and update their neighbors
    # print("Combining")
    # subgraphs.append(newSubgraphs[0]) # add subgraph1 to subgraphs
    # subgraphs.append(newSubgraphs[1]) # add subgraph2 to subgraphs
    # updateNeighbors(  ,newSubgraphs[0])

    return subgraph_one, subgraph_two

def check_acceptability(spanning_tree, subgraphsCombined, graph):
    global ideal_population
    global populationVariance
    global compactness_lower_bound, compactness_upper_bound

    upper_bound = ideal_population * (1 + (populationVariance * 0.5)) # Population upper bound
    lower_bound = ideal_population * (1 - (populationVariance * 0.5)) # Population lower bound

    list_edges = spanning_tree["edges"] # Current list of edges
    acceptable_edges = [] # Acceptable list of edges
    
    for edge in list_edges: # go through every edge, see if it's acceptable (cut)
        subgraph_one = [] # New subgraph 1
        subgraph_two = [] # New subgraph 2
        total_population_one = 0 # Total population of new subgraph 1
        total_population_two = 0 # Total population of new subgraph 2
        compactness_one = 0.4 # Compactness of new subgraph 1
        compactness_two = 0.4 # Compactness of new subgraph 2

        # print("Edge 0 --> " + str(edge[0]))
        # print("Edge 1 --> " + str(edge[1]))
        # print("Edges: " + str(spanning_tree["edges"]))
        precinct_one = edge[0]
        subgraph_one.append(precinct_one)
        precinct_two = edge[1]
        subgraph_two.append(precinct_two)

        # print("Precincts Neighbors: " + str(precinctsNeighbors))
        # print("Target precinct: " + str(precinct_one.split(', ')))
        for i in precinctsNeighbors[str(precinct_one.split(', '))]: # Adds precincts to new subgraph 1
            # print("Found precinct neighbor " + str(i))
            if i != edge[1] and i not in precinctsNeighbors[str(precinct_two.split(', '))]:
                subgraph_one.append(i)
        for i in precinctsNeighbors[str(precinct_two.split(', '))]: # Adds precinct to new subgraph 1
            if i != edge[0] and i not in precinctsNeighbors[str(precinct_one.split(', '))]:
                subgraph_two.append(i)

        for precinct in subgraph_one: # Calculates total population & compactness of subgraph 1
            total_population_one = total_population_one + graph.get(precinct)["population"]
        for precinct in subgraph_two: # Calculates total population & compactness of subgraph 2
            total_population_two = total_population_two + graph.get(precinct)["population"]

        # print("Total population one --> " + str(total_population_one))
        # print("Total population two --> " + str(total_population_two))

        # Checks if population lands within specified population difference & compactness boundaries
        # if (total_population_one <= upper_bound) and (total_population_one >= lower_bound):
            # if (total_population_two <= upper_bound) and (total_population_two >= lower_bound):
                # if (compactness_one >= compactness_lower_bound) and (compactness_one <= compactness_upper_bound):
                #     if (compactness_two >= compactness_lower_bound) and (compactness_two <= compactness_upper_bound):
        acceptable_edges.append(edge)
    
    print("Acceptable edges --> " + str(acceptable_edges))
    return acceptable_edges

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
    algorithm_driver(graph5)
    return

if __name__ == "__main__":
    main()

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------