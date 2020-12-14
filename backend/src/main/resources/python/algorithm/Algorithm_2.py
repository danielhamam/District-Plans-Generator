import random
import math
import sys
import json
from copy import deepcopy
import argparse
import cProfile
import re

# // Global Variables

# General job info
num_districts = 10
num_precincts = 0
state_abbreviation = ""
termination_limit = 100

# Population variables
ideal_population = 0.0
population_variance = 0.0
population_lower_bound = 0.0
population_upper_bound = 0.0
average_population = 0.0

# Compactness variables
compactness = ""
ideal_compactness = 0.0
compactness_lower_bound = 0.3
compactness_upper_bound = 0.9
average_compactness = 0.0
num_of_border_edges = 0

# Other variables
total_acceptable_edges = 0
total_improved_edges = 0
old_compactness_one = 0.0
old_compactness_two = 0.0
old_population_one = 0
old_population_two = 0

subgraphs = [] # holds precincts in it. also holds neighbors with other subgraphs. make name of it the first precinct
neighbors = {} # dictionary of keys that have neighbors. initialize via graph, update as we go on
precincts = [] # initial list of precinct neighbors for use later.
precinct_neighbors = {} # Neighbors of precincts
subgraphs_combined = [] # Used to generate spanning trees - is combination of two subgraphs, or is just one whole subgraph
ghost_districts = [] # Keeps track of ghost-districts (defined below, in function)
graph_main = {} # Stores initial list of precincts/graph

# (1) combine subgraph with random one of its neighbors 
# (2) generate spanning tree of combined subgraph
# (3) go through each edge and see if resulting subgraphs would be acceptable
	# - population limit fits
	# - compactness limit fits
# (4) store the acceptable (or better) edges in a dictionary, choose random one and cut to form two subgraphs. 
# (5) repeat until termination condition reached (10,000 reasonable after testing)

# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        HELPER FUNCTIONS
# ------------------------------------------------------------------
# ------------------------------------------------------------------


# Reads and stores data from input JSON file
def getData(file):
    try:
        print("Retrieving data. . .")
        # Opening JSON file
        print(file)
        # path = '../../system/jobs/test1/AlgorithmInput.json'
        # file = open(path,'r')
        data = json.load(file)
        # print("File Loaded. . .")

        global state
        state = data['data']['state']
        # print("State Data loaded: " + str(state) + "\n")

        global state_abbreviation
        state_abbreviation = state["nameAbbrev"]
        # print("State Abbreviation Data Loaded: " + str(state_abbreviation) + "\n")

        global job
        job = data['data']['job']
        # print("Job Data Loaded: " + str(job) + "\n")

        global graph_main
        graph_main = data['data']['state']['precincts']
        # print("Graph Data Loaded: " + str(graph_main) + "\n")

        global num_districts
        num_districts = job["districtsAmount"]
        # print("Number of Districts Data Loaded: " + str(num_districts) + "\n")

        global population_variance, population_lower_bound, population_upper_bound, ideal_population
        ideal_population = round(state["totalPopulation"]/num_districts, 2)
        population_variance = job["populationDifference"]
        population_lower_bound = ideal_population * (1 - (population_variance * 0.5)) # Population lower bound
        population_upper_bound = ideal_population * (1 + (population_variance * 0.5)) # Population upper bound
        # print("Population Variance Data Loaded: "  + str(population_variance) + "\n")

        global compactness, compactness_lower_bound, compactness_upper_bound, ideal_compactness
        compactness = job["compactness"]
        if compactness == "LOW":
            # # Border-Node Compactness Bounds
            # compactness_lower_bound = 0.0
            # compactness_upper_bound = 0.3
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            # Cut-Edge Compactness Bounds
            # compactness_lower_bound = 0.0
            # compactness_upper_bound = 3.0
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            compactness_lower_bound = 0.0
            compactness_upper_bound = 1.0
            ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

        if compactness == "MEDIUM":
            # # Border-Node Compactness Bounds
            # compactness_lower_bound = 0.3
            # compactness_upper_bound = 0.6
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            # Cut-Edge Compactness Bounds
            # compactness_lower_bound = 3.1
            # compactness_upper_bound = 6.0
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            compactness_lower_bound = 0.7
            compactness_upper_bound = 2.3
            ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2
        if compactness == "HIGH":
            # # Border-Node Compactness Bounds
            # compactness_lower_bound = 0.6
            # compactness_upper_bound = 6.0
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            # Cut-Edge Compactness Bounds
            # compactness_lower_bound = 6.1
            # compactness_upper_bound = 10.0
            # ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2

            compactness_lower_bound = 2.0
            compactness_upper_bound = 10.0
            ideal_compactness = (compactness_upper_bound+compactness_lower_bound)/2
        # print("Compactness Data Loaded: " + compactness + "\n")

        print("Data retrieval complete.")

        # Closing file 
        file.close()
    except:
        print("ERROR: File not found!")
        sys.exit()


# Removes ghost precincts from the main graph before algorithm begins
def removeGhostPrecincts():
    # Ghost precincts are precincts that do not have any neighbors (e.g. ones surrounded by water that is part of a state)
    global graph_main

    ghost_precincts = []
    counter = 0
    ghost_precincts_2 = []


    for precinct in graph_main:
        if graph_main[precinct]["neighbors"] == []:
            ghost_precincts.append(precinct)
    
    for precinct in ghost_precincts: # For each ghost precinct recorded
        del graph_main[precinct]
        counter = counter + 1

    for precinct in ghost_precincts:
        for i in graph_main:
            if precinct in graph_main[i]["neighbors"]: # If reference to this ghost precinct found
                graph_main[i]["neighbors"].remove(precinct)
                if graph_main[i]["neighbors"] == []: # Recheck if it is empty
                    ghost_precincts_2.append(i)
    
    for precinct in ghost_precincts_2:
        del graph_main[precinct]
        counter = counter + 1

    print("GHOST PRECINCTS REMOVED: " + str(counter))

    return


# Updates the list that keeps track of subgraph's neighbors
def updateNeighbors(found_neighbor, old_subgraph, new_subgraph):
    global neighbors, subgraphs

    if (type(found_neighbor) == str):
        found_neighbor = found_neighbor.split(', ') # turn string into list (for first iteration)

    neighbor_neighbors = neighbors.get(str(found_neighbor))
    old_subgraph_neighbors = neighbors.get(str(old_subgraph))

    # Combine the neighbors of both subgraphs
    combined_neighbors = []
    for neighbor in neighbor_neighbors:
        if neighbor not in combined_neighbors and neighbor != found_neighbor and neighbor != old_subgraph and neighbor not in found_neighbor and neighbor not in old_subgraph:
            combined_neighbors.append(neighbor)
    for neighbor in old_subgraph_neighbors:
        if neighbor not in combined_neighbors and neighbor != found_neighbor and neighbor != old_subgraph and neighbor not in found_neighbor and neighbor not in old_subgraph:
            combined_neighbors.append(neighbor)
    
    # Remove current subgraphs from list neighbors
    neighbors.pop(str(found_neighbor))
    neighbors.pop(str(old_subgraph))

    # Add new subgraph to list neighbors
    neighbors[str(new_subgraph)] = combined_neighbors
    
    for key in neighbors:
        values = neighbors.get(key)
        edited_value = 0 # 1 if we already edited
        delete_items = [] # to NOT SKIP VALUES
        for value in values: # note that it adds the value and is then forced to go into it
            value_list = value
            if (type(value) == str):
                value_list = value_list.split(', ') # Convert it to list
            if value_list == old_subgraph or value_list == found_neighbor:
                delete_items.append(value)
                if edited_value == 0: # if we haven't added it already
                    values.append(new_subgraph)
                    edited_value = 1
        for item in delete_items: # do this after to avoid skipping in previous for loop
            values.remove(item)

    return


# Reinitializes the list that keeps track of the subgraph's neighbors
def reinitializeNeighbors():
    global subgraphs, neighbors, precinct_neighbors

    neighbors = {}
    for subgraph in subgraphs:
        neighbors[str(subgraph)] = []

    for subgraph in subgraphs: # for every subgraph
        for node in subgraph: # for every node
            node_neighbors = precinct_neighbors.get(str(node.split(', '))) # node in list format
            for neighbor in node_neighbors: # for every neighbor
                if neighbor not in subgraph:
                    # find which subgraph the neighbor is in, and set neighbors
                    for subgraph2 in subgraphs:
                        
                        if neighbor in subgraph2:
                            if subgraph2 not in neighbors[str(subgraph)]:
                                neighbors[str(subgraph)].append(subgraph2)
                                if subgraph not in neighbors[str(subgraph2)]:
                                    neighbors[str(subgraph2)].append(subgraph)

    checkNeighbors() # Perform a check on neighbors list (checks for any empty lists)

    return   


# Checks if any subgraphs has a list of empty neighbors and temporarily removes them
def checkNeighbors(): # Perform a check on neighbors list (checks for any empty lists)
    global subgraphs, neighbors, ghost_districts
    
    for subgraph in subgraphs:
        subgraph_neighbors = neighbors.get(str(subgraph))
        if subgraph_neighbors == []:
            ghost_districts.append(subgraph)
            subgraphs.remove(subgraph)

    return


# (1) Algorithm

# Main algorithm driver/initiation
def algorithmDriver(graph):
    global neighbors, subgraphs, num_districts, termination_limit, ghost_districts
    initial_subgraphs = list(graph.keys()) # takes precinct key
    # Convert into list of lists
    for sub in initial_subgraphs:
        sub = sub.split(', ')
        subgraphs.append(sub)
        sub2 = deepcopy(sub)
        precincts.append(sub2)

    # Initialize neighbors from graph
    
    # print(graph.values())
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

    # --Switch-- To activate restart-iteration
    counter = 1
    attempts = 0
    while counter < termination_limit + 1:
        attempts = attempts + 1
        print(("\nBeginning iteration " + str(counter) + ":"))
        value = algorithm(graph)

        if value:
            counter = counter + 1
        if not value:
            continue
    
    counter = counter -1
    # counter = attempts
     # --Switch--

    # --Switch-- To deactivate restart-iteration
    # counter = 0
    # for i in range(termination_limit): # --Switch--
    #     counter = counter + 1 # --Switch--
    #     print(("\nBeginning iteration " + str(counter) + ":"))
    #     value = algorithm(graph)
    # --Switch--

    # USE CASE #47 - Calculate and display edge cut performance (optional) 
    total_unacceptable_edges = counter - total_acceptable_edges
    percent_of_acceptable_edges = round(100 * (total_acceptable_edges/counter), 2)
    percent_of_improved_edges = round(100 * (total_improved_edges/counter), 2)
    
    print('\n')
    print("Acceptable edges found --> " + str(percent_of_acceptable_edges) + "%" + " of edges are acceptable (" +
        str(total_acceptable_edges) + "/" + str(counter) + ")")
    print("Improved edges found --> " + str(percent_of_improved_edges) + "%" + " of edges made an improved subgraph (" +
        str(total_improved_edges) + "/" + str(counter) + ")")
    print("Not acceptable edges found --> " + str(100 - percent_of_acceptable_edges) + "%" + " of edges are not acceptable (" +
        str(total_unacceptable_edges) + "/" + str(counter) + ")")

    # Re-insert ghost districts
    for subgraph in ghost_districts:
        subgraphs.append(subgraph)
    
    # When we're done, let's print the subgraphs:
    counter = 1
    print("\n")
    for subgraph in subgraphs:
        print(("District " + str(counter) + " --> " + str(subgraph)))
        counter = counter + 1
    print("\n")

    return

# Main step-by-step algorithm
def algorithm(graph):
    global subgraphs, neighbors, precincts, precinct_neighbors, subgraphs_combined
    global old_population_one, old_population_two, old_compactness_one, old_compactness_two

    # USE CASE #30 --> Generate a random districting satisfying constraints (required)
    # Random Subgraph, Random neighbor
    random_subgraph = random.choice(subgraphs)
    subgraph_neighbors = neighbors.get(str(random_subgraph))
    # print("Random subgraph chosen --> " + str(random_subgraph))

    if subgraph_neighbors == []:
        return False

    random_neighbor = random.choice(subgraph_neighbors) # get random neighbor
    # print("Random subgraph-neighbor chosen --> " + str(random_subgraph))
    
    # --------------------------------------------------------------------
    # Calculates stats for pre-cut - used to check for improvements
    # Calculates total population of random subgraph (old_subgraph_one)
    for p in random_subgraph:
        precinct = graph.get(p)
        if precinct["demographic"] != {}:
            old_population_one = old_population_one + precinct["demographic"]["total"]
    
    # Calculates total population of random neighbor (old_subgraph_two)
    for p in random_neighbor:
        precinct = graph.get(p)
        if precinct["demographic"] != {}:
            old_population_two = old_population_two + precinct["demographic"]["total"]
    
    # Calculates compactness of random subgraph (old_subgraph_one) using Cut-Edge Compactness
    total_edges_one = len(random_subgraph) - 1
    border_nodes_one = 0
    if total_edges_one == 0:
        total_edges_one = 1

    for precinct in random_subgraph:
        for neighbor in precinct_neighbors[str(precinct.split(', '))]:
            if neighbor not in random_subgraph:
                border_nodes_one = border_nodes_one + 1
    old_compactness_one = abs(1 - (border_nodes_one/total_edges_one))

    # Calculates compactness of random neighbor (old_subgraph_two) using Cut-Edge Compactness
    total_edges_two = len(random_neighbor) - 1
    border_nodes_two = 0
    if total_edges_two == 0:
        total_edges_two = 1

    for precinct in random_neighbor:
        for neighbor in precinct_neighbors[str(precinct.split(', '))]:
            if neighbor not in random_neighbor:
                border_nodes_two = border_nodes_two + 1
    old_compactness_two = abs(1 - (border_nodes_two/total_edges_two))

    # # Calculates compactness of random subgraph (old_subgraph_one) using Border-Node Compactness
    # border_nodes_one = 0
    # already_checked = []
    # for precinct in random_subgraph:
    #     for neighbor in precinct_neighbors[str(precinct.split(', '))]:
    #         if neighbor not in random_subgraph and neighbor not in already_checked:
    #             border_nodes_one = border_nodes_one + 1
    #             already_checked.append(neighbor)
    # old_compactness_one = abs(1 - (border_nodes_one/len(random_subgraph)))

    # # Calculates compactness of random subgraph (old_subgraph_two) using Border-Node Compactness
    # border_nodes_two = 0
    # already_checked = []
    # for precinct in random_neighbor:
    #     for neighbor in precinct_neighbors[str(precinct.split(', '))]:
    #         if neighbor not in random_neighbor and neighbor not in already_checked:
    #             border_nodes_two = border_nodes_two + 1
    #             already_checked.append(neighbor)
    # old_compactness_two = abs(1 - (border_nodes_two/len(random_neighbor)))
    # --------------------------------------------------------------------

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

    # USE CASE #31 --> Generate a spanning tree of the combined sub-graph above (required) 
    spanning_tree = generateSpanningTreeBFS()
    # print("Spanning tree generation complete --> " + str(spanning_tree))

    # USE CASE #32 --> Calculate the acceptability of each newly generated sub-graph (required)
    # USE CASE #33 --> Generate a feasible set of edges in the spanning tree to cut (required) 
    value = checkAcceptability(spanning_tree, subgraphs_combined, graph) # Returns edge if acceptable found, False otherwise
    # print("Resulting subgraphs --> " + str(value))

    if not value: # If false, revert combination
        subgraphs_combined_index = subgraphs.index(subgraphs_combined)
        subgraphs.pop(subgraphs_combined_index)
        subgraphs.append(random_subgraph)
        subgraphs.append(random_neighbor)
        reinitializeNeighbors()
        return False
    else: # If true, accept changes
        subgraph_one = value[0]
        subgraph_two = value[1]
        combined_subgraph_index = subgraphs.index(subgraphs_combined)
        subgraphs[combined_subgraph_index] = subgraph_one # so it holds the same neighbors
        subgraphs.append(subgraph_two)
        reinitializeNeighbors()

    return True

# Generates and returns spanning tree for subgraph provided through global variable 'subgraphs_combined'
def generateSpanningTreeBFS():
    global precinct_neighbors, subgraphs_combined
    # BFS for combined subgraph (spanning tree)
    random_start = random.choice(subgraphs_combined) # randomly select a start
    visited = [random_start]
    queue = [random_start]
    edges = []
    current_node = random_start
    while queue:
        current_node = queue.pop(0)
        neighbors_precinct = precinct_neighbors.get(str(current_node.split(', '))) # get neighbors of currently selected precinct

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

    spanning_tree = { "visited": visited, "edges": edges}
    # print("\n" + str(spanning_tree))

    return spanning_tree


# Returns two resulting subgraphs that would result IF the target edge was cut. Note: edge is not cut here
def preCutSubgraphs(edges, target_cut):
    global subgraphs_combined, subgraphs, neighbors, num_of_border_edges
    
    subgraph_one = [] # New subgraph 1
    subgraph_two = [] # New subgraph 2

    precinct_one = target_cut[0]
    precinct_two = target_cut[1]

    # Subgraph one:

    queue = []
    visited = []
    queue.append(precinct_one)
    subgraph_one.append(precinct_one)
    while queue:
        target_node = queue.pop(0)
        for edge in edges:
            first_node = edge[0]
            second_node = edge[1]
            if first_node == target_node and second_node != precinct_two and second_node not in visited:
                subgraph_one.append(second_node)
                queue.append(second_node)
            if second_node == target_node and first_node != precinct_two and first_node not in visited:
                subgraph_one.append(first_node)
                queue.append(first_node)
            visited.append(target_node)

    # Subgraph Two: 

    queue = []
    visited = []
    queue.append(precinct_two)
    subgraph_two.append(precinct_two)
    while queue:
        target_node = queue.pop(0)
        for edge in edges:
            first_node = edge[0]
            second_node = edge[1]
            if first_node == target_node and second_node != precinct_one and second_node not in visited:
                subgraph_two.append(second_node)
                queue.append(second_node)
            if second_node == target_node and first_node != precinct_one and first_node not in visited:
                subgraph_two.append(first_node)
                queue.append(first_node)
        visited.append(target_node)

    return subgraph_one, subgraph_two


# Checks which edges are acceptable to be cut (satisfies boundaries). Returns list of acceptable edges
def checkAcceptability(spanning_tree, subgraphs_pair, graph):
    global population_lower_bound, population_upper_bound
    global compactness_lower_bound, compactness_upper_bound
    global ideal_population, ideal_compactness
    global old_population_one, old_population_two, old_compactness_one, old_compactness_two
    global total_acceptable_edges, total_improved_edges

    list_edges = spanning_tree["edges"] # Current list of edges
    
    random_edge = random.choice(list_edges)
    subgraph_one = [] # New subgraph 1
    subgraph_two = [] # New subgraph 2
    total_population_one = 0 # Total population of new subgraph 1
    total_population_two = 0 # Total population of new subgraph 2
    compactness_one = 0.5 # Compactness of new subgraph 1
    compactness_two = 0.5 # Compactness of new subgraph 2
    border_nodes_one = 0 # Used for compactness
    border_nodes_two = 0 # Used for compactness


    subgraph_one, subgraph_two = preCutSubgraphs(list_edges, random_edge) # Returns two subgraphs

    total_edges_one = len(subgraph_one) - 1 # Total edges in subgraph one - used for compactness
    total_edges_two = len(subgraph_two) - 1 # Total edges in subgraph one - used for compactness

    if total_edges_one == 0 or total_edges_two == 0: # Meaning district only has one precinct
        return False # Do not proceed with the rest of the loop, go to next iteration
    if total_edges_one == 0:
        total_edges_one = 1
    if total_edges_two == 0:
        total_edges_two = 1

    # Calculates total population of subgraph 1
    for p in subgraph_one:
        precinct = graph.get(p)
        if precinct["demographic"] != {}:
            total_population_one = total_population_one + precinct["demographic"]["total"]
    
    # Calculates total population of subgraph 2
    for p in subgraph_two:
        precinct = graph.get(p)
        if precinct["demographic"] != {}:
            total_population_two = total_population_two + precinct["demographic"]["total"]

    # Calculates compactness of subgraph 1 using Cut-Edge Compactness    
    for precinct in subgraph_one:
        for neighbor in precinct_neighbors[str(precinct.split(', '))]:
            if neighbor not in subgraph_one:
                border_nodes_one = border_nodes_one + 1
    compactness_one = abs(1 - (border_nodes_one/total_edges_one))

    # Calculates compactness of subgraph 2 using Cut-Edge Compactness
    for precinct in subgraph_two:
        for neighbor in precinct_neighbors[str(precinct.split(', '))]:
            if neighbor not in subgraph_two:
                border_nodes_two = border_nodes_two + 1
    compactness_two = abs(1 - (border_nodes_two/total_edges_two))

    # # Calculates compactness of subgraph 2 using Border-Node Compactness
    # already_checked = []
    # for precinct in subgraph_one:
    #     for neighbor in precinct_neighbors[str(precinct.split(', '))]:
    #         if neighbor not in subgraph_one and neighbor not in already_checked:
    #             border_nodes_one = border_nodes_one + 1
    #             already_checked.append(neighbor)
    # compactness_one = abs(1 - (border_nodes_one/len(subgraph_one)))

    # # Calculates compactness of subgraph 2 using Border-Node Compactness
    # already_checked = []
    # for precinct in subgraph_two:
    #     for neighbor in precinct_neighbors[str(precinct.split(', '))]:
    #         if neighbor not in subgraph_two and neighbor not in already_checked:
    #             border_nodes_two = border_nodes_two + 1
    #             already_checked.append(neighbor)
    # compactness_two = abs(1 - (border_nodes_two/len(subgraph_two)))

    print("Compactness One --> " + str(compactness_one))
    print("Compactness Two --> " + str(compactness_two))

    # Checks if population lands within specified population difference & compactness boundaries
    if (total_population_one <= population_upper_bound) and (total_population_one >= population_lower_bound):
        if (total_population_two <= population_upper_bound) and (total_population_two >= population_lower_bound):
            if (compactness_one >= compactness_lower_bound) and (compactness_one <= compactness_upper_bound):
                if (compactness_two >= compactness_lower_bound) and (compactness_two <= compactness_upper_bound):
                    total_acceptable_edges = total_acceptable_edges + 1
                    print("Edge cut at: " + str(random_edge))
                    return subgraph_one, subgraph_two

    # --------------------------------------------------------------------
    # If edge does not satisfy any of the boundaries: save it just in case
    # Calculates offset for each - used later on to calculate best/improved edge
    p_one_offset = abs(ideal_population-total_population_one)/ideal_population
    p_two_offset = abs(ideal_population-total_population_two)/ideal_population
    c_one_offset = abs(ideal_compactness-compactness_one)/ideal_compactness
    c_two_offset = abs(ideal_compactness-compactness_two)/ideal_compactness
    p_one_old_offset = abs(ideal_population-old_population_one)/ideal_population
    p_two_old_offset = abs(ideal_population-old_population_two)/ideal_population
    c_one_old_offset = abs(ideal_compactness-old_compactness_one)/ideal_compactness
    c_two_old_offset = abs(ideal_compactness-old_compactness_two)/ideal_compactness

    points = 0 # Calculates points to see if it is an improvement or not
    if p_one_offset < p_one_old_offset:
        points = points + 1
    if p_two_offset < p_two_old_offset:
        points = points + 1
    if c_one_offset < c_one_old_offset:
        points = points + 1
    if c_two_offset < c_two_old_offset:
        points = points + 1

    if points == 2: # Best and current are equally sufficient - choose one or the other
        if random.randint(0, 1) == 1:
            print("Equally Sufficient. Edge cut at: " + str(random_edge))
            return subgraph_one, subgraph_two
    if points < 2: # Current is not an improved cut edge
        print("Insufficient edge. Edge: " + str(random_edge))
        return False
    if points > 2: # Current is an improved edge
        total_improved_edges = total_improved_edges + 1
        print("Improved edge found! Edge cut at: " + str(random_edge))
        return subgraph_one, subgraph_two
    # --------------------------------------------------------------------

    return False

# Finds random subgraphs and combines them
def findCombine(graph, subgraph):
    global subgraphs, neighbors
    subgraph_neighbors = neighbors.get(str(subgraph))

    if len(subgraph_neighbors) == 0:
        return
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

# Calculates average population amongst all finalized districts
def calculateAveragePopulation():
    global subgraphs, graph_main, average_population

    total_population = 0
    counter = 0
    
    for subgraph in subgraphs:
        counter = counter + 1
        for precinct in subgraph:
            if graph_main[precinct]["demographic"] != {}:
                total_population = total_population + graph_main[precinct]["demographic"]["total"]

    average_population = round(total_population/counter, 2)   

    return     

# Calculates average compactness amongst all finalized districts
def calculateAverageCompactness():
    global subgraphs, graph_main, precinct_neighbors, subgraphs_combined, average_compactness

    counter = 0
    compactness = 0
    total_compactness = 0

    global compactness_lower_bound, compactness_upper_bound
    # within_boundary = 0 # DEBUG

    for subgraph in subgraphs:
        counter = counter + 1
        border_nodes = 0
        subgraphs_combined = subgraph
        spanning_tree = generateSpanningTreeBFS()
        total_edges = len(spanning_tree["edges"])

        for precinct in subgraph:
            for neighbor in precinct_neighbors[str(precinct.split(', '))]:
                if neighbor not in subgraph:
                    border_nodes = border_nodes + 1

        if total_edges != 0:
            compactness = abs(1 - (border_nodes/ total_edges))
        total_compactness = total_compactness + compactness

    average_compactness = round(total_compactness/counter, 2)

    return

# Parser for input file and output file
def parser():
    parser = argparse.ArgumentParser(prog="algorithm",description='Algorithm')
    parser.add_argument('infile', help='The input json file to make the python code run', type=argparse.FileType('r'), nargs=1, default=sys.stdin)
    parser.add_argument('output_directory', help='This is the path for the output directory', type=str, nargs=1)
    parser.add_argument('output_file_name', help='This is the name of output file', type=str, nargs=1)

    return parser

# Converts output (districts) produced by algo into a plan format. Writes to file and returns plan
def convertToOutput(directory_path:str, filename:str):
    global subgraphs, state_abbreviation, num_districts
    plan = {
        "stateAbbreviation": state_abbreviation,
        "numberOfDistricts": num_districts,
        "isPlanEnacted": False,
        "averageDistrictPopulation": average_population,
        "averageDistrictCompactness": average_compactness,
        "algorithmData": {
            
        }
    }

    counter = 0
    for i in subgraphs:
        counter = counter + 1
        district = {
        "precincts": []
        }
        district.update({"precincts": i})
        plan["algorithmData"].update({str(counter): district})

    plan.update({"numberOfDistricts": counter})
    newjsonfile = json.dumps(plan, indent=4)
    outfile = open(directory_path + "output" + filename + ".json", 'w')
    outfile.write(newjsonfile)

    return plan


# ------------------------------------------------------------------
# ------------------------------------------------------------------
#                        MAIN FUNCTION
# ------------------------------------------------------------------
# ------------------------------------------------------------------


def main():
    global graph_main, neighbors, subgraphs

    infile = 0
    parse = parser() # Initiates parser
    infile = parse.parse_args().infile[0] # Sets up parser
    getData(infile) # Retrieves data from JSON
    removeGhostPrecincts() # Removes any ghost precincts in the list of precincts
    cProfile.run("algorithmDriver(graph_main)")
    #algorithmDriver(graph_main) # Main function
    calculateAveragePopulation() # Calculates average population amongst all districts
    calculateAverageCompactness() # Calculates average compactness amongst all districts
    convertToOutput(parse.parse_args().output_directory[0], parse.parse_args().output_file_name[0]) # Converts to output format

    return

if __name__ == "__main__":
    main()

