from json import load, dumps
import json
import argparse
import sys
import os

state_abbreviation = ""
num_of_districts = 0
is_plan_enacted = False
average_district_population = 0.0
average_district_compactness = 0.0
algorithm_data = None
list_of_plans = []


def getData(file):
    try:
        print("Retrieving data. . .")
        # Opening JSON file
        print(file)
        file = open(file, 'r')
        data = json.load(file)
        # print("File Loaded. . .")


        global state_abbreviation
        state_abbreviation = data["stateAbbreviation"]
        # print("State Abbreviation Loaded: " + str(state_abbreviation) + "\n")

        global num_of_districts
        num_of_districts = data["numberOfDistricts"]
        # print("Number of Districts Loaded: " + str(num_districts) + "\n")

        global is_plan_enacted
        is_plan_enacted = data["isPlanEnacted"]
        # print("Is Plan Enacted boolean Loaded: " + str(is_plan_enacted) + "\n")

        global average_district_population
        average_district_population = data['averageDistrictPopulation']
        # print("Average District Population loaded: " + str(average_district_population) + "\n")

        global average_district_compactness
        average_district_compactness = data['averageDistrictCompactness']
        # print("Average District Compactness loaded: " + str(average_district_compactness) + "\n")

        global algorithm_data
        algorithm_data = data["algorithmData"]
        # print("Algorithm Data Loaded: " + str(algorithm_data) + "\n")

        print("Data retrieval complete.")

        # Closing file 
        file.close()
    except Exception as error:
        print("ERROR: File not found!")
        print(error)
        sys.exit()

def produceOutput(number):
    global state_abbreviation, num_of_districts, is_plan_enacted, average_district_population, average_district_compactness, algorithm_data

    plan = {
      "planId": str(number),
      "type": "",
      "stateAbbrev": state_abbreviation,
      "numOfDistricts": num_of_districts,
      "isPlanEnacted": is_plan_enacted,
      "averageDistrictPopulation": average_district_population,
      "averageDistrictCompactness": average_district_compactness,
      "graph_districts": algorithm_data
    }

    return plan


def parser():
    parser = argparse.ArgumentParser(prog="Merge files from the output", description='Algorithm')
    parser.add_argument('directory', help='directory where files are located', type=str, nargs=1)
    return parser


def main(args):
    global list_of_plans

    directory_path = args.directory[0]
    directory = os.fsencode(directory_path)
    print(directory)
    print(os.listdir(directory))
    
    counter = 0
    for file in os.listdir(directory): # Iterate through each file in directory
        filename = os.fsdecode(file)
        if filename.endswith(".json"):
            counter = counter + 1 # Plan number
            getData(directory_path + filename) # Extracts data from file
            list_of_plans.append(produceOutput(counter)) # Formats data into plan and adds it to list of plans
            continue

    # Write list of plans to file
    newjsonfile = json.dumps(dict(plans=list_of_plans), indent=4)
    outfile = open(directory_path + "AlgorithmOutput.json", 'w')
    outfile.write(newjsonfile)


if __name__ == "__main__":
    parse = parser()
    main(parse.parse_args())

