import random
import math
import sys
import json
from copy import deepcopy
import argparse

num_districts = 3
num_precincts = 30
compactness_measure = ''
population_variance = 0.017
termination_limit = 5
ideal_population = 15
compactness_lower_bound = 0.1
compactness_upper_bound = 1.3
num_of_cut_edges = 0

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
precinct_neighbors = {} 
subgraphs_combined = []
graph_main = {}

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------
def getData(file):
    try:
        print("Retrieving data. . .")
        # Opening JSON file
        print(file)
        # path = '../../system/jobs/test1/AlgorithmInput.json'
        # file = open(path,'r')
        data = json.load(file)
        global state
        state = data['data']['state']
        global job
        job = data['data']['job']

        global graph_main
        graph_main = data['data']['state']['precincts']

        print("Data retrieval complete.")

        # Closing file 
        file.close()
    except:
        print("ERROR: File not found!")
        sys.exit()

def removeGhostPrecincts():
    # Ghost precincts are precincts that do not have any neighbors (e.g. ones surrounded by water that is part of a state)
    global graph_main

    ghost_precincts = []


    for precinct in graph_main:
        if graph_main[precinct]["neighbors"] == []:
            ghost_precincts.append(precinct)
    
    for precinct in ghost_precincts:
        del graph_main[precinct]
        

def updateNeighbors(found_neighbor, old_subgraph, new_subgraph):
    global neighbors, subgraphs
    # For first iteration, may be string. Convert to list.
    if (type(found_neighbor) == str):
        found_neighbor = found_neighbor.split(', ') # turn string into list (for first iteration)

    # (A) Combine random neighbor's neighbors with old_subgraph neighbors. Store in an array
        # Delete neighbor from each others list (old_subgraph and neighbors)
        # Delete repetitive edges
    # (B) Delete the old_subgraph key and random neighbor key in NEIGHBORS dictionary
    # (C) Add new key to the NEIGHBORS dictionary
    # (D) Replace all instances of old_subgraph/random neighbor key with new_subgraph key

    # (A)
    neighbor_neighbors = neighbors.get(str(found_neighbor))
    old_subgraph_neighbors = neighbors.get(str(old_subgraph))
    combined_neighbors = old_subgraph_neighbors + neighbor_neighbors

    # Remove themselves from combined neighbors
    delete_items = [] # to avoid skipping values
    for found in combined_neighbors:
        found_value = found
        if (type(found_value) == str):
            found_value = found_value.split(',') # turn string into list (for first iteration)
        if found_value == old_subgraph or found_value == found_neighbor:
            delete_items.append(found)
    for item in delete_items:
        combined_neighbors.remove(item)

    # Remove Duplicates
    remove_duplicates = []
    for i in combined_neighbors:
        if i not in remove_duplicates:
            remove_duplicates.append(i)
    combined_neighbors = remove_duplicates
    
    # (B)
    neighbors.pop(str(found_neighbor))
    neighbors.pop(str(old_subgraph))

    # (C)
    neighbors[str(new_subgraph)] = combined_neighbors

    # (D)
    for key in neighbors:
        values = neighbors.get(key)
        edited_value = 0 # 1 if we already edited
        delete_items = [] # to NOT SKIP VALUES
        for value in values: # note that it adds the value and is then forced to go into it
            found_value = value
            if (type(value) == str):
                found_value = value.split(', ') # make sure it's a list
            if found_value == old_subgraph or found_value == found_neighbor:
                delete_items.append(value)
                if edited_value == 0: # if we havent added it already
                    values.append(new_subgraph)
                    edited_value = 1
        for item in delete_items: # do this after to avoid skipping in previous for loop
            values.remove(item)
    return
    
# (1) Algorithm

def algorithmDriver(graph):
    global neighbors, subgraphs, num_districts, termination_limit
    initial_subgraphs = list(graph.keys()) # takes precinct key

    # Convert into list of lists
    for sub in initial_subgraphs:
        sub = sub.split(', ')
        subgraphs.append(sub)
        sub2 = deepcopy(sub)
        precincts.append(sub2)

    # Initialize neighbors from graph
    for i in range(len(list(graph.values()))):
        key = subgraphs[i] # get key
        value = list(graph.values())[i]['neighbors'] # get value
        value2 = list(graph.values())[i]['neighbors'] # get value
        neighbors[str(key)] = value # keys stored as strings
        precinct_neighbors[str(deepcopy(key))] = deepcopy(value2)

    print("\n--------------------------------------------------------")
    print("           INITIALIZING SEED DISTRICTS ALGORITHM          ")
    print("--------------------------------------------------------")

    # USE CASE #29 GENERATE SEED DISTRICTING
    while len(subgraphs) != num_districts: 
        for subgraph in subgraphs: 
            findCombine(graph, subgraph)
            if len(subgraphs) == num_districts:
                break

    # Print the subgraphs out
    counter = 1
    print("\n")
    for subgraph in subgraphs:
        print(("Seed District #" + str(counter) + " --> " + str(subgraph)))
        counter = counter + 1

    print("\n--------------------------------------------------------")
    print("                BEGINNING ALGORITHM                       ")
    print("--------------------------------------------------------")

    print(("Termination limit: " + str(termination_limit)))

    # 35. Repeat the steps above until you generate satisfy the termination condition (required)
    counter = 1
    for i in range(termination_limit):
        print(("\nBeginning iteration " + str(counter) + ":"))
        print(("Initial subgraphs: " + str(subgraphs)))
        algorithm(graph)
        counter = counter + 1
        print(("Revised Subgraphs: " + str(subgraphs)))

    # When we're done, let's print the subgraphs:
    counter = 1
    print("\n")
    for subgraph in subgraphs:
        print(("District " + str(counter) + " --> " + str(subgraph)))
        counter = counter + 1
    print("\n")
    return

def algorithm(graph):
    print("---------------------------------")
    print(graph)
    global subgraphs, neighbors, precincts, precinct_neighbors, subgraphs_combined

    # USE CASE #30 --> Generate a random districting satisfying constraints (required)
    # Random Subgraph, Random neighbor
    random_subgraph = random.choice(subgraphs)
    subgraph_neighbors = neighbors.get(str(random_subgraph))

    random_neighbor = random.choice(subgraph_neighbors) # get random neighbor

    # Combine both subgraphs into one

    subgraphs_combined = []
    if type(random_subgraph) == str:
        subgraphs_combined.append(random_subgraph)
    else:
        for i in random_subgraph:
            subgraphs_combined.append(i)

    if type(random_neighbor) == str:
        subgraphs_combined.append(random_neighbor)
    else:
        for j in random_neighbor:
            subgraphs_combined.append(j)

    # Let's also combine the neighbors of these subgraphs 
    updateNeighbors(random_neighbor, random_subgraph, subgraphs_combined)

    # DELETE THEM FROM THE SUBGRAPHS LIST
    if type(random_neighbor) == str:
        random_neighbor = random_neighbor.split(', ')
    if type(random_subgraph) == str:
        random_subgraph = random_subgraph.split(', ')

    neighbor_index = subgraphs.index(random_neighbor)
    subgraphs.pop(neighbor_index)
    subgraph_index = subgraphs.index(random_subgraph)
    subgraphs.pop(subgraph_index)

    # ADD COMBINED SUBGRAPHS TO THE SUBGRAPHS LIST
    subgraphs.append(subgraphs_combined)

    print(("Random Subgraph: " + str(random_subgraph)))
    print(("Random Neighbor: " + str(random_neighbor)))
    print(("Combined Subgraph: " + str(subgraphs_combined)))

    # USE CASE #31 --> Generate a spanning tree of the combined sub-graph above (required) 
    spanning_tree = generateSpanningTreeBFS()

    print(("Spanning tree edges: " + str(spanning_tree["edges"])))

    # USE CASE #32 --> Calculate the acceptability of each newly generated sub-graph (required) 
    # USE CASE #33 --> Generate a feasible set of edges in the spanning tree to cut (required) 
    acceptable_edges = checkAcceptability(spanning_tree, subgraphs_combined, graph)

    print(("Acceptable Edges List: " + str(acceptable_edges)))

    # USE CASE #34 --> Cut the edge in the combined sub-graph (required)
    target_cut = random.choice(acceptable_edges) # choose random edge to cut

    print(("Cutting at edge: " + str(target_cut)))

    return cutAcceptable(acceptable_edges, target_cut)

# def generateSpanningTreeDFS():
#     global subgraphs, neighbors, precincts, precinct_neighbors, subgraphs_combined
#     # DFS for combined subgraph (spanning tree)
#     random_start = random.choice(subgraphs_combined) # randomly select a start
#     print("------------- subgraphs_combined:")
#     print(subgraphs_combined)
#     visited = [random_start]
#     print("------------- visited:")
#     print(visited)
#     stack = [random_start]
#     print("------------- stack:")
#     print(stack)
#     edges = []
#     current_node = random_start
#     print("------------- current_node:")
#     print(current_node)
#     # while len(visited) < len(precincts):
#     while stack:
#         pop_stack = True
#         neighbors_precinct = precinct_neighbors.get(str(current_node.split(', '))) # get neighbors of currently selected precinct
#         print("------------- neighbors_precinct: ")
#         print(neighbors_precinct)
#         # current_node_neighbors = neighbors.get(str(current_node))

#         for node in subgraphs_combined:
#             print("-------------  node:")
#             print(node)
#             if node not in visited and node in neighbors_precinct:
#                 visited.append(node)
#                 stack.append(node)
#                 # Create the edge and add it 
#                 new_list = []
#                 vertex_one = current_node
#                 print("------------- vertex_one:")
#                 print(current_node)
#                 vertex_two = node
#                 print("------------- vertex_two:")
#                 print(vertex_two)
#                 if (type(vertex_one) == str):
#                     vertex_one = vertex_one.split(', ')
#                 if (type(vertex_two) == str):
#                     vertex_two = vertex_two.split(', ')
#                 new_list = vertex_one + vertex_two
#                 print("------------- new_list:")
#                 print(new_list)
#                 edges.append(new_list) # assume creates edge
#                 print("------------- edges")
#                 print(edges)
#                 current_node = node
#                 pop_stack = False
#                 break
#         if pop_stack: # go back
#             if stack:
#                 print("------------- stack")
#                 print(stack)
#                 current_node = stack[-1]
#                 stack.pop()
#     spanning_tree = { "visited": visited, "edges": edges}
#     print("------------- spanning_tree")
#     print(spanning_tree)
#     # print("\n" + str(spanning_tree))
#     return spanning_tree

def generateSpanningTreeBFS():
    global subgraphs, neighbors, precincts, precinct_neighbors, subgraphs_combined
    # DFS for combined subgraph (spanning tree)
    random_start = random.choice(subgraphs_combined) # randomly select a start
    visited = [random_start]
    queue = [random_start]
    edges = []
    current_node = random_start
    # while len(visited) < len(precincts):
    while queue:
        pop_queue = True
        neighbors_precinct = precinct_neighbors.get(str(current_node.split(', '))) # get neighbors of currently selected precinct
        # current_node_neighbors = neighbors.get(str(current_node))

        # queue.pop(0)

        for node in subgraphs_combined:
            if node not in visited and node in neighbors_precinct:
                visited.append(node)
                queue.append(node)
                # Create the edge and add it 
                new_list = []
                vertex_one = current_node
                vertex_two = node
                if (type(vertex_one) == str):
                    vertex_one = vertex_one.split(', ')
                if (type(vertex_two) == str):
                    vertex_two = vertex_two.split(', ')
                new_list = vertex_one + vertex_two
                edges.append(new_list) # assume creates edge
                current_node = node
                pop_queue = False
                break
        if pop_queue: # go back
            if queue:
                current_node = queue.pop(0)
                # stack.pop()
    spanning_tree = { "visited": visited, "edges": edges}
    # print("\n" + str(spanning_tree))
    return spanning_tree

def cutAcceptable(acceptable_edges, target_cut):
    global subgraphs_combined, subgraphs, neighbors, num_of_cut_edges

    subgraph_one = [] # New subgraph 1
    subgraph_two = [] # New subgraph 2
    total_population_one = 0 # Total population of new subgraph 1
    total_population_two = 0 # Total population of new subgraph 2
    compactness_one = 0.4 # Compactness of new subgraph 1
    compactness_two = 0.4 # Compactness of new subgraph 2

    precinct_one = target_cut[0]
    precinct_two = target_cut[1]

    # Subgraph one:

    queue = []
    visited = []
    queue.append(precinct_one)
    subgraph_one.append(precinct_one)
    while queue:
        target_node = queue.pop(0)
        # print("Target node --> " + str(target_node))
        for edge in acceptable_edges:
            first_node = edge[0]
            second_node = edge[1]
            if first_node == target_node and second_node != precinct_two and second_node not in visited:
                # print("Found node --> " + str(second_node))
                subgraph_one.append(second_node)
                queue.append(second_node)
            if second_node == target_node and first_node != precinct_two and first_node not in visited:
                # print("Found node --> " + str(first_node))
                subgraph_one.append(first_node)
                queue.append(first_node)
            visited.append(target_node)

    # print("------------------------------------")

    # Subgraph Two: 

    queue = []
    visited = []
    queue.append(precinct_two)
    subgraph_two.append(precinct_two)
    while queue:
        target_node = queue.pop(0)
        # print("Target node --> " + str(target_node))
        for edge in acceptable_edges:
            first_node = edge[0]
            second_node = edge[1]
            if first_node == target_node and second_node != precinct_one and second_node not in visited:
                # print("Found node --> " + str(second_node))
                subgraph_two.append(second_node)
                queue.append(second_node)
            if second_node == target_node and first_node != precinct_one and first_node not in visited:
                # print("Found node --> " + str(first_node))
                subgraph_two.append(first_node)
                queue.append(first_node)
        visited.append(target_node)

    combined_subgraph_index = subgraphs.index(subgraphs_combined)
    subgraphs[combined_subgraph_index] = subgraph_one # so it holds the same neighbors
    subgraphs.append(subgraph_two)
    reinitializeNeighbors()

    num_of_cut_edges = num_of_cut_edges + 1

    return subgraph_one, subgraph_two

def reinitializeNeighbors():
    global subgraphs, neighbors, precinct_neighbors
    # print("subgraphs: " + str(subgraphs))
    neighbors = {}
    for subgraph in subgraphs: # for every subgraph
        for node in subgraph: # for every node
            node_neighbors = precinct_neighbors.get(str(node.split(', '))) # node in list format
            for neighbor in node_neighbors: # for every neighbor
                if neighbor not in subgraph:
                    # find which subgraph the neighbor is in, and set neighbors
                    for subgraph2 in subgraphs:
                        if neighbor in subgraph2:
                            if str(subgraph) in neighbors and subgraph2 not in neighbors[str(subgraph)]:
                                neighbors[str(subgraph)].append(subgraph2)
                            else:
                                neighbors[str(subgraph)] = []
                                neighbors[str(subgraph)].append(subgraph2)

def checkAcceptability(spanning_tree, subgraphs_combined, graph):
    global ideal_population
    global population_variance
    global compactness_lower_bound, compactness_upper_bound

    upper_bound = ideal_population * (1 + (population_variance * 0.5)) # Population upper bound
    lower_bound = ideal_population * (1 - (population_variance * 0.5)) # Population lower bound

    list_edges = spanning_tree["edges"] # Current list of edges
    acceptable_edges = [] # Acceptable list of edges
    
    for edge in list_edges: # go through every edge, see if it's acceptable (cut)
        subgraph_one = [] # New subgraph 1
        subgraph_two = [] # New subgraph 2
        total_population_one = 0 # Total population of new subgraph 1
        total_population_two = 0 # Total population of new subgraph 2
        compactness_one = 0.4 # Compactness of new subgraph 1
        compactness_two = 0.4 # Compactness of new subgraph 2
        cut_edges = 0 # Used for compactness

        # print("Edge 0 --> " + str(edge[0]))
        # print("Edge 1 --> " + str(edge[1]))
        # print("Edges: " + str(spanning_tree["edges"]))
        precinct_one = edge[0]
        subgraph_one.append(precinct_one)
        precinct_two = edge[1]
        subgraph_two.append(precinct_two)

        # print("Precincts Neighbors: " + str(precinct_neighbors))
        # print("Target precinct: " + str(precinct_one.split(', ')))
        for i in precinct_neighbors[str(precinct_one.split(', '))]: # Adds precincts to new subgraph 1
            # print("Found precinct neighbor " + str(i))
            if i != edge[1] and i not in precinct_neighbors[str(precinct_two.split(', '))]:
                subgraph_one.append(i)
        for i in precinct_neighbors[str(precinct_two.split(', '))]: # Adds precinct to new subgraph 1
            if i != edge[0] and i not in precinct_neighbors[str(precinct_one.split(', '))]:
                subgraph_two.append(i)

        for precinct in subgraph_one: # Calculates total population & compactness of subgraph 1
            total_population_one = total_population_one + graph.get(precinct)["population"]
        for precinct in subgraph_two: # Calculates total population & compactness of subgraph 2
            total_population_two = total_population_two + graph.get(precinct)["population"]


        # print("SUPGRAPH ONE-----------------")
        # print(subgraph_one)
        
        # for i in subgraph_one:
        #     print("HERE---------------------------------")
        #     print()


        # print("Total population one --> " + str(total_population_one))
        # print("Total population two --> " + str(total_population_two))

        # Checks if population lands within specified population difference & compactness boundaries
        # if (total_population_one <= upper_bound) and (total_population_one >= lower_bound):
            # if (total_population_two <= upper_bound) and (total_population_two >= lower_bound):
                # if (compactness_one >= compactness_lower_bound) and (compactness_one <= compactness_upper_bound):
                #     if (compactness_two >= compactness_lower_bound) and (compactness_two <= compactness_upper_bound):
        acceptable_edges.append(edge)
    
    # print("Acceptable edges --> " + str(acceptable_edges))
    return acceptable_edges

def findCombine(graph, subgraph):
    global subgraphs, neighbors
    subgraph_neighbors = neighbors.get(str(subgraph))
    random_neighbor = random.choice(subgraph_neighbors) # get random neighbor

    # ADD (COMBINE) NEIGHBOR TO THE CURRENT SUBGRAPH
    old_subgraph = list(subgraph) # copy contents 
    add_neighbor = random_neighbor
    if (type(random_neighbor) == str):
        add_neighbor = add_neighbor.split(', ') # turn string into list (for first iteration)
    new_list = []
    for i in subgraph:
        new_list.append(i)
    for j in add_neighbor:
        new_list.append(j)

    subgraph_index = subgraphs.index(subgraph)
    subgraphs[subgraph_index] = new_list # add neighbor to subgraph

    # DELETE IT FROM THE SUBGRAPHS LIST
    neighbor_index = subgraphs.index(add_neighbor)
    subgraphs.pop(neighbor_index) # delete it from subgraphs dictionary

    # UPDATE THE REFERENCES OF THIS NEIGHBOR
    updateNeighbors(random_neighbor, old_subgraph, new_list) 
    return

def parser():
    parser = argparse.ArgumentParser(prog="algorithm",description='Algorithm')
    parser.add_argument('infile', help='Algo-output file', type=argparse.FileType('r'), nargs=1, default=sys.stdin)
    return parser

def convertToOutput():
    global subgraphs

    plan = {
        "planId": "",
        "type": "",
        "stateAbbrev": "",
        "numOfDistricts": 0,
        "graph_districts":{
            
      }
    }

    counter = 0
    for i in subgraphs:
        counter = counter + 1
        district = {
        "compactness": 0.0,
        "precincts": []
        }
        district.update({"precincts": i})
        plan["graph_districts"].update({str(counter): district})

    plan.update({"numOfDistricts": counter})
    print(plan)
    return plan


# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        MAIN FUNCTION
# ------------------------------------------------------------------
# ------------------------------------------------------------------



def main():
    infile = 0
    parse = parser() # Initiates parser
    infile = parse.parse_args().infile[0] # Sets up parser
    getData(infile) # Retrieves data from JSON
    removeGhostPrecincts() # Removes any ghost precincts in the list of precincts
    algorithmDriver(graph_main) # Main function
    # convertToOutput() # Converts to output format 
    

    global num_of_cut_edges
    print(num_of_cut_edges)
    global subgraphs
    print(subgraphs)
    return

if __name__ == "__main__":
    main()

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------