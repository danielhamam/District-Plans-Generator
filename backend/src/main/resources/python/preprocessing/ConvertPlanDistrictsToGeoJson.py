import geopandas
import json
from shapely.ops import unary_union
import argparse
import collections


States = {
    'MD' : {
        "Name": "Maryland",
        "Abbrev": "MD",
        "FIPS": "24",
        "PrecinctFile": "backend/src/main/resources/system/states/md/Precincts.json",
        "CountyFile": "backend/src/main/resources/system/states/md/Counties.json"
    },
    "PA": {
        "Name": "Pennsylvania",
        "Abbrev": "PA",
        "FIPS": "42",
        "PrecinctFile": "backend/src/main/resources/system/states/pa/Precincts.json",
        "CountyFile": "backend/src/main/resources/system/states/pa/Counties.json"
    },
    "GA" :{
        "Name": "Georgia",
        "Abbrev": "GA",
        "FIPS": "13",
        "PrecinctFile": "backend/src/main/resources/system/states/ga/Precincts.json",
        "CountyFile": "backend/src/main/resources/system/states/ga/Counties.json"
    }
}
def main(args):
    infile = args.infile[0]
    output_directory = args.output_directory[0]


    #Use this method for reading an planDistrict file
    readPlanDistrictFile(infile, output_directory)

    # districtFile = 'backend/src/main/resources/system/states/ga/EnactedDistricts.json'
    #
    # #this method creates a dictionary where the {key -> district number, value -> array of counties that overlapped
    # # the district boundary}
    # districtCounties = getDistrictCounties(districtFile, States['GA']["CountyFile"])
    #
    # #this method takes the created dictionary of district and list of county pairs and
    # #provides a new dictionary with the number of counties found to be within a district
    # #{key -> district number, value -> number of counties}
    # countedDistrictCounties = countNumberCountiesInDistricts(districtCounties)

   # print(countedDistrictCounties)

def readPlanDistrictFile(file, output_directory):

    with open(file) as f:
            planDistrict = json.load(f)

            plans = planDistrict['plans']
            state = plans[0]['stateAbbrev']

            #Tranverse through each plan within a job
            for plan in plans:

                #Create the district boundaries based on the precincts said to be within a district
                #and Create the associate new District GeoJson File
                createNewDistrictBoundaries(States[state]["PrecinctFile"], plan, States[state]["FIPS"], output_directory)

def createNewDistrictBoundaries(file, plan, stateFips, output_directory):

    file = open(file)

    #feed the precinct data into geopandas
    dataFrames = geopandas.read_file(file)

    #Set dataFrame indicies to be State, County, VTD
    dataFrames = dataFrames.set_index(['STATE', 'COUNTY','VTD'])

    #Sort indexes to improve performance 
    dataFrames = dataFrames.sort_index()
    
    #get the graph districts 
    graph_district = plan['graph_districts']

    districts_dataFrame = geopandas.GeoDataFrame()

    #Tranverse through each district within the plan
    for district in graph_district: 
        
        precinctArray = graph_district[district]['precincts']

        precinctGeometries = []

        #Tranverse through each precinct within the district
        #and get their corresponding precinct feature data 
        for precinct in precinctArray:

           
            #Parse these to be able to look up an precinct based 
            #on their state, county and vtd value
            state = precinct[:2]
            county = precinct[2:5]
            vtd = precinct[5:]

            #Get the corresponding precinct feature data
            getPrecinctData = dataFrames.loc[state, county, vtd]

            #Get the precinct data frame's geometry data as a shapely geometries
            getPrecinctGeometry = getPrecinctData.geometry.iloc[0]

            precinctGeometries.append(getPrecinctGeometry)

        #Union the precincts to form a new district
        precinctUnion = unary_union(precinctGeometries)

        #Re-construct a geoseries 
        newDistrictGeometry = geopandas.GeoSeries(precinctUnion)

        newDistrictFormat = {
            "geometry": newDistrictGeometry,
            "STATE": stateFips,
            "CD": district.zfill(2),
            "LSAD":"C2",
            "GEO_ID": stateFips + district.zfill(2),
            "LSAD_TRANS":"Congressional District"  
        }
        
        #Create GeoDataFrame Object
        newDistrictDataFrame = geopandas.GeoDataFrame(newDistrictFormat)

        #Append the new District into an Districts Data frame
        districts_dataFrame = districts_dataFrame.append(newDistrictDataFrame, ignore_index=True)

    #Write the data to GeoJson File
    filepath = output_directory + plan['type'] + 'District.json'
    districts_dataFrame.to_file(filepath, driver='GeoJSON')

def getDistrictCounties(districtFile, countyFile):

    #Open files 
    d = open(districtFile)
    c = open(countyFile)

    #Feed districts and counties into geopandas
    districtDataFrame = geopandas.read_file(d)
    countyDataFrame = geopandas.read_file(c)

    #Dictionary that stores districts and their counties
    districtCounty = {}

    #Tranverse through the district features in dataframe
    for index, district in districtDataFrame.iterrows():

        #Tranverse through the county features in dataframe
        for index, county in countyDataFrame.iterrows():

            #Extract the geometry as shapely object
            getDistrictGeom = district.geometry
            getCountyGeom = county.geometry

            #Check if the district and county overlap, if so 
            #add the county into the district's county array
            if getDistrictGeom.overlaps(getCountyGeom):
                
                if district['CD'] not in districtCounty:
                    districtCounty[district['CD']] = []
                
                districtCounty[district['CD']].append(county['NAME'])

    #Order the dictionary by key      
    districtCounty = collections.OrderedDict(sorted(districtCounty.items()))

    return districtCounty
    
def countNumberCountiesInDistricts(districtDict):

    newDict = {}

    for district in districtDict:
        newDict["District " + district] = len(districtDict[district])
    
    return newDict



def parser():
    parser = argparse.ArgumentParser(prog="CovertPlanDistrictToGeojson",description='CovertPlanDistrictToGeojson')
    parser.add_argument('infile', help='The input json file to make the python code run', type=argparse.FileType('r'), nargs=1, default=sys.stdin)
    parser.add_argument('output_directory', help='This is the path for the output directory', type=str, nargs=1)
    return parser


    
  
if __name__ == "__main__":
    parser = parser()
    main(parser.parse_args())