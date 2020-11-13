import random
import math
import sys

numberOfDistricts = 3
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
        # sys.stdout.write(format % args)
        # print()
    return
    # new_debug_status = debug_status+optional
    # new_debug_status == 3 or new_debug_status == 4
    # if new_debug_status == 2:
    #     return func
    # if new_debug_status == 4:
    #     return func
    #1 high debug statement
    #2 mid debug statement

def printf(format, *args):
    sys.stdout.write(format % args)

def getRandomListElement(arr:list):
    random_node = random.choice(arr)
    return random_node

def popListElement(arr:list,element):
    arr.pop(arr.index(element))



def create_district_struct(num_of_districts:int):
    district_struct = {}
    for i in range(1,num_of_districts+1):
        struct = {"neighbors":[],"precincts":[]}
        district_struct.setdefault(str(i),struct)
        
    return district_struct

def combineP(graph : dict):
    global numberOfDistricts, debug
    taken_nodes = []
    available_nodes = list(graph.keys())
    min_nodes_in_district = len(available_nodes)//numberOfDistricts
    districts_struct = create_district_struct(numberOfDistricts)
    debug(print("[DEBUG] Created districts...", list(districts_struct.keys())))
    debug(print("[DEBUG] The minimum amount of nodes for each districts is", min_nodes_in_district))
    min_unfilled_districts = list(districts_struct.keys())
    while len(taken_nodes) <= len(available_nodes):
        district = getRandomListElement(min_unfilled_districts)
        random_node = getRandomListElement(available_nodes)
        debug(printf("[DEBUG] Filling districts %s with precincts\n", district))
        for i in range(min_nodes_in_district):
            debug(printf("\t[DEBUG] ...added precincts %s\n", random_node))
            districts_struct[district]["precincts"].append(random_node)
            popListElement(available_nodes, random_node) 
            potential_node = getRandomListElement(graph[random_node]["neighbors"])
            while not(potential_node in available_nodes) and i != min_nodes_in_district-1:
                pass
            random_node = potential_node
        popListElement(min_unfilled_districts, district)
        debug(printf("[DEBUG] District %s filled!\n", district))
        debug(print("[DEBUG] Districts lefted", min_unfilled_districts, available_nodes))


        #taken_nodes.append(1)
        

            
            
            
        # min_filled_districts.append(district)
        # print(random_node,available_nodes)

def peak(arr:list):
    value = graph[key]['neighbors']
    return value

def get_dict_neighbors(graph:dict, key):
    value = graph[key]['neighbors']
    return value


def updateGraphNeigbors(original_graph:dict, new_nodes:list):
    new_graph = {}
    for node in new_nodes:
        struct = original_graph[node]
        neighbors = get_dict_neighbors(original_graph,node)
        for neighbor in neighbors:
            if not neighbor in new_nodes:
                struct['neighbors'].remove(neighbor)  
        new_graph.setdefault(node,struct)
    return new_graph



def combinePRec(graph:dict, n:int):
    global numberOfDistricts, debug
    debug(1,"\n" * 2);
    debug(1, "~"*60 + "\n\tSTARTING RECURSIVE IB\n" + "~"*60)
    if n == 0:
        debug(2, "[DEBUG] graph: ", graph)
    counter = numberOfDistricts
    #districts_struct = create_district_struct(numberOfDistricts)
    #debug(2, "[DEBUG] Created districts...", list(districts_struct.keys()))
    dfs_value = dict_dfs(graph)
    edges = dfs_value["edges"]
    visited = dfs_value["visited"]
    degrees = dfs_value["degrees"]
    debug(2, "[DEBUG] edge: ", edges)
    debug(2, "[DEBUG] visited: ", visited)
    debug(1, "[DEBUG] degrees: ", degrees)
    pivot_index = len(edges)//2
    pivot_edge = edges[pivot_index]
    debug(2, "\t[DEBUG] pivot_edge: ", pivot_edge, "visisted_pivot_index", pivot_index)
    pivot_index = visited.index(pivot_edge[1])
    district_one = visited[:pivot_index]
    district_two = visited[pivot_index:]
    debug(2, "\t[DEBUG] district_one: ", district_one)
    debug(2, "\t[DEBUG] district_two: ", district_two)
    dict_district_one = updateGraphNeigbors(graph,district_one)
    dict_district_two = updateGraphNeigbors(graph,district_two)
    debug(2, "\t[DEBUG] dict_district_one: ", dict_district_one)
    debug(2, "\t[DEBUG] dict_district_two: ", dict_district_two)
    combinePRec(dict_district_one, n-1)
    combinePRec(dict_district_two, n-1)
    debug(1, "+"*60 + "\n\tENDING RECURSIVE IB\n" + "+"*60)


    #combinePRec(dict_district_two, n-1)
    # if len(dict_district_one.keys()) > 2:
    #     combinePDFS(dict_district_one)
    #combinePDFS(dict_district_two)



def combinePDFS(graph:dict):
    global numberOfDistricts, debug
    debug(1,"\n" * 2);
    debug(1, "~"*60 + "\n\tSTARTING RECURSIVE IB\n" + "~"*60)
    if n == 0:
        return graph
    counter = numberOfDistricts
    #districts_struct = create_district_struct(numberOfDistricts)
    #debug(2, "[DEBUG] Created districts...", list(districts_struct.keys()))
    dfs_value = dict_dfs(graph)
    edges = dfs_value["edges"]
    visited = dfs_value["visited"]
    degrees = dfs_value["degrees"]
    pivot_edge = edges[pivot_index]
    # edges_one = edges[:pivot_index]
    # edges_two = edges[pivot_index+1:]
    pivot_index = visited.index(pivot_edge[1])
    district_one = visited[:pivot_index]
    district_two = visited[pivot_index:]
    debug(2, "[DEBUG] edge: ", edges)
    debug(2, "[DEBUG] visited: ", visited)
    debug(2, "[DEBUG] degrees: ", degrees)
    debug(2, "\t[DEBUG] pivot_edge: ", pivot_edge, "visisted_pivot_index", pivot_index)
    debug(2, "\t[DEBUG] district_one: ", district_one)
    debug(2, "\t[DEBUG] district_two: ", district_two)
    dict_district_one = updateGraphNeigbors(graph,district_one)
    dict_district_two = updateGraphNeigbors(graph,district_two)
    debug(2, "\t[DEBUG] dict_district_one: ", dict_district_one)
    debug(2, "\t[DEBUG] dict_district_two: ", dict_district_two)
    debug(1, "+"*60 + "\n\tENDING RECURSIVE IB\n" + "+"*60)
    # if len(dict_district_one.keys()) > 2:
    #     combinePDFS(dict_district_one)
    #combinePDFS(dict_district_two)

    
  





def create_dict_struct(num_of_districts:int):
    district_struct = {}
    for i in range(1,num_of_districts+1):
        struct = {"neighbors":[],"precincts":[]}
        district_struct.setdefault(str(i),struct)

def cal_node_degree(nodes:list,edges:list):
    dict_struct = {}
    debug(1,"\n" * 2);
    debug(1, "~"*60 + "\n\tSTARTING CALCULATING EACH NODES DEGREE\n" + "~"*60)
    debug(1, "[DEBUG:NODE_DEGREE] node", nodes, "edges", edges)
    for i in nodes:
        degree  = 0
        dict_struct.setdefault(i,degree)

    for edge in edges:
        dict_struct[edge[0]] = dict_struct[edge[0]] + 1
        dict_struct[edge[1]] = dict_struct[edge[1]] + 1

    debug(1,(("<"*20 + " [DEBUG:DFS-COMPLETE] Completed " + ">"*20) + "\n") * 3)
    debug(1, "[DEBUG:NODE_DEGREE] ", dict_struct)
    debug(1, "+"*60 + "\n\tENDING CALCULATING EACH NODES DEGREE\n" + "+"*60)
    return dict_struct


def dict_dfs(graph:dict):
    visited = []
    nodes = list(graph.keys())
    node = getRandomListElement(nodes)
    visited = [node]
    stack = [node]
    edges = []
    current_node = node
    debug(1,"\n" * 2);
    debug(1, "~"*60 + "\n\t\tSTARTING DFS ALGORITHM\n" + "~"*60)
    debug(1, "[DEBUG:DFS] nodes of the graph", nodes)
    debug(1, "[DEBUG:DFS] nodes visited", visited)
    debug(1, "[DEBUG:DFS] nodes on stack", stack)
    debug(1, "[DEBUG:DFS] adding node", node)
    while len(visited) < len(nodes):
        pop_stack = True
        neighbors = get_dict_neighbors(graph,current_node)
        debug(1, "\t[DEBUG:DFS] current_node:", current_node, "\tcurrent_node-neighbors:", neighbors)
        debug(1, "\t[DEBUG:DFS] nodes visited", visited)
        debug(1, "\t[DEBUG:DFS] nodes on stack", stack)
        for node in neighbors:
            ##debug(1, "stack", stack))
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

    debug(1,(("<"*20 + " [DEBUG:DFS-COMPLETE] DFS Complete " + ">"*20) + "\n") * 3)
    debug(1, "\n[DEBUG:DFS-COMPLETE] finshed nodes visited", visited)
    debug(1, "[DEBUG:DFS-COMPLETE] finshed edges from dfs", edges)
    debug(1, "[DEBUG:DFS-COMPLETE] finshed nodes on stack", stack)

    degrees = cal_node_degree(nodes,edges)
    values = {
        "visited": visited,
        "edges": edges,
        "degrees": degrees,
    }
    debug(1, "+"*60 + "\n\tENDING DFS ALGORITHM\n" + "+"*60)
    return values





graph1 = {
    'A' : ['B','S'],
    'B' : ['A'],
    'C' : ['D','E','F','S'],
    'D' : ['C'],
    'E' : ['C','H'],
    'F' : ['C','G'],
    'G' : ['F','S'],
    'H' : ['E','G'],
    'S' : ['A','C','G']
}

def adfs(graph,node,visited):
    #visited = contianer[visited]  
    if node not in visited:
        visited.append(node)
        for n in get_dict_neighbors(graph, node):
            adfs(graph,n,visited)
    if len(visited) == len(graph.keys()):
        return visited


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
    #adfs(graph5,'000', [])
    print(dict_dfs(graph5))
    #global numberOfDistricts
    #print(combinePRec(graph5,numberOfDistricts-1))
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
