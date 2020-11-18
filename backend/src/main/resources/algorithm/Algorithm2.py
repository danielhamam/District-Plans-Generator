import random
import math
import sys

number_of_districts = 3
debug_status = 3
ideal_population = 10
percentage_difference = 0.1
compactness_lower_bound = 0.33
compactness_upper_bound = 0.67
#0 no debug
#1 low-level interpretation debug
#2 mid-level interpretation debug
#3 high-level

def debug(optional, printout, *args):
    global debug_status 
    if debug_status == 0:
        return "-"
    if debug_status <= optional:
        print("[DEBUG]:", printout, *args)

def random_list_element(arr:list):
    random_node = random.choice(arr)
    return random_node

def get_dict_neighbors(graph:dict, key):
    value = graph[key]['neighbors']
    return value

def calculate_neighboring_districts(districts: dict, graph: dict):
    nodes = list(graph.keys())
    subgraphs = list(districts.keys())
    neighbors_of_precinct = [] # temp list
    neighboring_precincts_of_district = [] # temp list
    precincts_in_district = [] # temp list

    for district in subgraphs: # First iteration through the districts
        neighboring_precincts_of_district = []
        precincts_in_district = districts[district]["precincts"]
        for i in precincts_in_district: # Calculates all neighboring precincts to this district
            neighbors_of_precinct = graph[i]["neighbors"]
            for j in neighbors_of_precinct:
                if j not in neighboring_precincts_of_district:
                    neighboring_precincts_of_district.append(j)

        for k in subgraphs: # Second iteration through the districts
            if k != district:
                precincts_in_district = districts[k]["precincts"]
                for i in precincts_in_district:
                    if i in neighboring_precincts_of_district:
                        if k not in districts[district]["neighbors"]:
                            districts[district]["neighbors"].append(k)
                          

def combine_two_districts(districts: dict, graph: dict):
    new_district = {} # Only a dictionary with precincts
    new_precincts_list = []
    # new_neighbors_list = []
    rand_district_one_element = random_list_element(list(districts.keys()))
    district_one = districts[rand_district_one_element]
    district_one_neighbors = district_one['neighbors']
    rand_district_two_element = random_list_element(district_one_neighbors)
    district_two = districts[rand_district_two_element]
    debug(1, "[COMBINE-2-DISTRICTS] selected: subgraph", rand_district_one_element,
          district_one, "\t&&\tsubgraph", rand_district_two_element, district_two)
    for i in district_one["precincts"]:
        new_precincts_list.append(i)
    for i in district_two["precincts"]:
        new_precincts_list.append(i)
    debug(1, "[COMBINE-2-DISTRICTS]", new_precincts_list)
    for i in new_precincts_list:
        new_precinct = graph[i].copy()
        new_precinct_neighbors = new_precinct['neighbors']
        updated_precinct_neighbors = list(filter(
            lambda precinct: (precinct in new_precincts_list),
            new_precinct_neighbors))
        new_precinct['neighbors'] = updated_precinct_neighbors
        new_district.setdefault(i, new_precinct)
        debug(1, "old", i, graph[i]['neighbors'])
        debug(1, "new", i, updated_precinct_neighbors)
    debug(2, "[COMBINE-2-DISTRICTS] combined subgraph", new_district)
    return new_district

def check_acceptability(spanning_tree: dict, districts: dict, precincts: dict):
    global ideal_population
    global percentage_difference

    upper_bound = ideal_population * (1 + (percentage_difference * 0.5)) # Population upper bound
    lower_bound = ideal_population * (1 - (percentage_difference * 0.5)) # Population lower bound

    list_edges = spanning_tree["edges"] # Current list of edges
    acceptable_edges = [] # Acceptable list of edges
    
    for edge in list_edges:
        district_one = [] # New subgraph 1
        district_two = [] # New subgraph 2
        total_population_one = 0 # Total population of new subgraph 1
        total_population_two = 0 # Total population of new subgraph 2
        compactness_one = 0.4 # Compactness of new subgraph 1
        compactness_two = 0.4 # Compactness of new subgraph 2

        precinct_one = precincts[edge[0]]
        district_one.append(precinct_one)
        precinct_two = precincts[edge[1]]
        district_two.append(precinct_two)

        for i in precinct_one["neighbors"]: # Adds precincts to new subgraph 1
            if i != edge[1] and i not in precinct_two["neighbors"]:
                district_one.append(precincts[i])
        for i in precinct_two["neighbors"]: # Adds precinct to new subgraph 1
            if i != edge[0] and i not in precinct_one["neighbors"]:
                district_two.append(precincts[i])

        for precinct in district_one: # Calculates total population & compactness of subgraph 1
            total_population_one = total_population_one + precinct["population"]
        for precinct in district_two: # Calculates total population & compactness of subgraph 2
            total_population_two = total_population_two + precinct["population"]

        # Checks if population lands within specified population difference & compactness boundaries
        if (total_population_one <= upper_bound) and (total_population_one >= lower_bound):
            if (total_population_two <= upper_bound) and (total_population_two >= lower_bound):
                if (compactness_one >= compactness_lower_bound) and (compactness_one <= compactness_upper_bound):
                    if (compactness_two >= compactness_lower_bound) and (compactness_two <= compactness_upper_bound):
                        acceptable_edges.append(edge)
    
    return acceptable_edges

def calculate_compactness():
    print("Calculate Compactness: FUNCTION IN PROGRESS")

def create_subgraphs_from_edges(edges_list:list, graph: dict):
    districts = {}
    nodes_taken = []
    for i in range(len(edges_list)):
        struct = {'precincts':[],'neighbors':[]}
        debug(1, "[DEBUG]", edges_list[i], " len -> ", len(edges_list[i])) 
        for j in range(len(edges_list[i])):
            edge_one = edges_list[i][j][0]
            edge_two = edges_list[i][j][1]
            if not edge_one in struct['precincts'] and not edge_one in nodes_taken:
                struct['precincts'].append(edge_one)
                nodes_taken.append(edge_one)
            if not edge_two in struct['precincts']and  not edge_two in nodes_taken:
                struct['precincts'].append(edge_two)
                nodes_taken.append(edge_two)
 
        districts.setdefault(i+1, struct)
    calculate_neighboring_districts(districts, graph)
    return districts

def partition_edges(partitions:list):
    global number_of_districts
    while len(partitions) != number_of_districts:
        random_subgraph = random_list_element(partitions)
        if len(random_subgraph) == 1:
            continue
        random_subgraph_index = partitions.index(random_subgraph)
        random_index_in_element = random.randrange(len(random_subgraph)) 
        debug(1, "\t[DEBUG] random_subgraph: ", random_subgraph, "index to split at: ", random_index_in_element)
        partitions.pop(random_subgraph_index)
        new_subgraph_one = []
        new_subgraph_two = []
        if random_index_in_element == 0:
            new_subgraph_one = random_subgraph[:1]
            new_subgraph_two = random_subgraph[1:]
        else:
            new_subgraph_one = random_subgraph[:random_index_in_element]
            new_subgraph_two = random_subgraph[random_index_in_element:]
        debug(1, "\t\t[EDGE-PARTITIONING] new_subgraph_one: ", new_subgraph_one)
        debug(1, "\t\t[EDGE-PARTITIONING] new_subgraph_two: ", new_subgraph_two)
        partitions.insert(random_subgraph_index, new_subgraph_one)
        partitions.insert(random_subgraph_index+1, new_subgraph_two)
        debug(1, "\t\t[EDGE-PARTITIONING] new partitions: ", partitions)
    debug(3, "[EDGE-PARTITIONING] Final Partitions: ", partitions)
    return partitions

def dfs_partition(graph:dict):
    dfs_value = dict_dfs(graph)
    edges = dfs_value["edges"]
    visited = dfs_value["visited"]
    degrees = dfs_value["degrees"]
    debug(1, "[DFS-PARITION] edge: ", edges)
    debug(1, "[DFS-PARITION] visited: ", visited)
    debug(1, "[DFS-PARITION] degrees: ", degrees)
    path_split = []
    for index in range(len(edges)-1):
        current_edge = edges[index]
        next_edge = edges[index+1]
        if current_edge[1] != next_edge[0]:
            path_split.append(index+1)
    debug(1, "[DFS-PARITION] path_split edges: ", path_split)
    intitial_edges_partition = [] 
    starting_index = 0
    for i in path_split:
        intitial_edges_partition.append(edges[starting_index:i]) 
        starting_index = i
    intitial_edges_partition.append(edges[starting_index:]) 
    debug(2, "[DFS-PARITION] intitial_edges_partition: ", intitial_edges_partition)
    new_edges_partition = partition_edges(intitial_edges_partition)
    districts = create_subgraphs_from_edges(new_edges_partition, graph)
    debug(3, "[DFS-PARITION] districts: ", districts)
    return districts

def cal_node_degree(nodes:list,edges:list):
    dict_struct = {}
    debug(1, "[NODE_DEGREE] node", nodes, "edges", edges)
    for i in nodes:
        degree  = 0
        dict_struct.setdefault(i,degree)
    for edge in edges:
        dict_struct[edge[0]] = dict_struct[edge[0]] + 1
        dict_struct[edge[1]] = dict_struct[edge[1]] + 1
    debug(1, "[NODE_DEGREE] ", dict_struct)
    return dict_struct

def dict_dfs(graph:dict):
    nodes = list(graph.keys())
    node = random_list_element(nodes)
    visited = [node]
    stack = [node]
    edges = []
    current_node = node
    debug(1, "[DFS] nodes of the graph", nodes)
    debug(1, "[DFS] adding node", node)
    while len(visited) < len(nodes):
        pop_stack = True
        neighbors = get_dict_neighbors(graph,current_node)
        debug(1, "\t[DFS] current_node:", current_node, "\tcurrent_node-neighbors:", neighbors)
        debug(1, "\t[DFS] nodes visited", visited)
        debug(1, "\t[DFS] nodes on stack", stack)
        for node in neighbors:
            if not node in visited:
                debug(1, "[DEBUG:DFS] adding nodes", node)
                visited.append(node)
                stack.append(node)
                edges.append((current_node,node))
                current_node = node
                pop_stack = False
                break
        if pop_stack:
            if stack:
                stack.pop()
                current_node = stack[-1]
                debug(1, "[DFS] deadend returning to node", current_node)
    debug(1, "[DFS] finshed nodes on stack", stack)
    debug(2, "[DFS] finshed edges from dfs", edges)
    debug(3, "[DFS] dfs complete. nodes visited", visited)
    degrees = cal_node_degree(nodes,edges)
    values = { "visited": visited, "edges": edges,"degrees": degrees}
    return values



def main():
    graph6 = {
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
               "003"
            ],
        },
    }
    #adfs(graph5,'000', [])
    #print(dict_dfs(graph5))
    #global number_of_districts
   # print(create_n_partition(graph5,number_of_districts))
    # print(dfs_partition(graph6))
    dict_districts = dfs_partition(graph6)
    new_subgraph = combine_two_districts(dict_districts, graph6)
    new_spanning_tree = dict_dfs(new_subgraph)
    value = check_acceptability(new_spanning_tree, dict_districts, graph6)
    #combineP(graph5)

if __name__ == "__main__":

    main()




graph6 = {
        "000":{
            'neighbors': [
                2, 3
            ],
        },
        "001": {
            'neighbors': [
                2, 3
            ],
        },
        "002":{
            'neighbors': [
                2, 3
            ],
        },
        "003": {
            'neighbors': [
                2, 3
            ],
        },
        "004":{
            'neighbors': [
                2, 3
            ],
        },
        "005": {
            'neighbors': [
                2, 3
            ],
        },
        "006":{
            'neighbors': [
                2, 3
            ],
        },
        "007": {
            'neighbors': [
                2, 3
            ],
        },
        "008":{
            'neighbors': [
                2, 3
            ],
        },
        "009": {
            'neighbors': [
                2, 3
            ],
        }
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
