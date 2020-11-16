import random
import math
import sys

number_of_districts = 3
debug_status = 2
#0 no debug
#1 low-level interpretation debug
#2 mid-level interpretation debug
#3 high-level

def debug(optional, printout, *args):
    global debug_status 
    if debug_status == 0:
        return "-"
    if debug_status <= optional:
        print(printout, *args)

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

def combine_two_districts(districts: dict, graph: dict):
    new_district = {} # Only a dictionary with precincts
    new_precincts_list = []
    # new_neighbors_list = []
    rand1 = random.randint(1, len(districts))
    district_one = districts[rand1]
    rand2 = district_one["neighbors"][random.randint(0, len(district_one["neighbors"])-1)]
    district_two = districts[rand2]

    for i in district_one["precincts"]:
        new_precincts_list.append(i)
    for i in district_two["precincts"]:
        new_precincts_list.append(i)
    # for i in district_one["neighbors"]:
    #     if i != rand2:
    #         new_neighbors_list.append(i)
    # for i in district_two["neighbors"]:
    #     if i != rand1:
    #         new_neighbors_list.append(i)

    for i in new_precincts_list:
        new_district.setdefault(i, graph[i])
    return new_district

# def check_acceptability(spanning_tree: dict):



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
        debug(1, "\t\t[DEBUG] new_subgraph_one: ", new_subgraph_one)
        debug(1, "\t\t[DEBUG] new_subgraph_two: ", new_subgraph_two)
        partitions.insert(random_subgraph_index, new_subgraph_one)
        partitions.insert(random_subgraph_index+1, new_subgraph_two)
        debug(1, "\t\t[DEBUG] new partitions: ", partitions)
    debug(2, "[DEBUG:EDGE-PARTITIONING] Final Partitions: ", partitions)
    return partitions

def dfs_partition(graph:dict):
    dfs_value = dict_dfs(graph)
    edges = dfs_value["edges"]
    visited = dfs_value["visited"]
    degrees = dfs_value["degrees"]
    debug(2, "[DEBUG] edge: ", edges)
    debug(2, "[DEBUG] visited: ", visited)
    debug(1, "[DEBUG] degrees: ", degrees)
    path_split = []
    for index in range(len(edges)-1):
        current_edge = edges[index]
        next_edge = edges[index+1]
        if current_edge[1] != next_edge[0]:
            path_split.append(index+1)
    debug(2, "[DEBUG] path_split edges: ", path_split) 
    intitial_edges_partition = [] 
    starting_index = 0
    for i in path_split:
        intitial_edges_partition.append(edges[starting_index:i]) 
        starting_index = i
    intitial_edges_partition.append(edges[starting_index:]) 
    debug(2, "[DEBUG] intitial_edges_partition: ", intitial_edges_partition) 
    new_edges_partition = partition_edges(intitial_edges_partition)
    districts = create_subgraphs_from_edges(new_edges_partition, graph)
    debug(2, "[DEBUG:DFS-PARITION] districts: ", districts) 
    return districts

def cal_node_degree(nodes:list,edges:list):
    dict_struct = {}
    debug(1,"\n" * 2)
    debug(1, "~"*60 + "\n\tSTARTING CALCULATING EACH NODES DEGREE\n" + "~"*60)
    debug(1, "[DEBUG:NODE_DEGREE] node", nodes, "edges", edges)
    for i in nodes:
        degree  = 0
        dict_struct.setdefault(i,degree)
    for edge in edges:
        dict_struct[edge[0]] = dict_struct[edge[0]] + 1
        dict_struct[edge[1]] = dict_struct[edge[1]] + 1
    debug(1,(("<"*20 + " [DEBUG:DFS-COMPLETE] Completed " + ">"*20) + "\n") * 3)
    debug(2, "[DEBUG:NODE_DEGREE] ", dict_struct)
    debug(1, "+"*60 + "\n\tENDING CALCULATING EACH NODES DEGREE\n" + "+"*60)
    return dict_struct


def dict_dfs(graph:dict):
    nodes = list(graph.keys())
    node = random_list_element(nodes)
    visited = [node]
    stack = [node]
    edges = []
    current_node = node
    debug(1, "~"*60 + "\n\t\tSTARTING DFS ALGORITHM\n" + "~"*60)
    debug(1, "[DEBUG:DFS] nodes of the graph", nodes)
    debug(1, "[DEBUG:DFS] adding node", node)
    while len(visited) < len(nodes):
        pop_stack = True
        neighbors = get_dict_neighbors(graph,current_node)
        debug(1, "\t[DEBUG:DFS] current_node:", current_node, "\tcurrent_node-neighbors:", neighbors)
        debug(1, "\t[DEBUG:DFS] nodes visited", visited)
        debug(1, "\t[DEBUG:DFS] nodes on stack", stack)
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
                debug(1, "[DEBUG:DFS] deadend returning to node", current_node)
    debug(1, "[DEBUG:DFS-COMPLETE] finshed nodes on stack", stack)
    debug(1, "[DEBUG:DFS-COMPLETE] finshed edges from dfs", edges)
    debug(2, "[DEBUG:DFS-COMPLETE] finshed nodes visited", visited)
    degrees = cal_node_degree(nodes,edges)
    values = { "visited": visited, "edges": edges,"degrees": degrees}
    debug(1, "+"*60 + "\n\tENDING DFS ALGORITHM\n" + "+"*60)
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
    value = check_acceptability(new_spanning_tree)
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