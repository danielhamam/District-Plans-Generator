
import csv
import json
import math
import geopandas
from shapely.geometry import Polygon, Point
from shapely.strtree import STRtree


States = {
    'MD' : {
        "Name": "Maryland",
        "Abbrev": "MD",
        "DistrictFile": "client/src/json/MARYLAND/md_congressionalDistrict.json",
        "PrecinctFile": "client/src/json/MARYLAND/md_precincts.json",
        "CountyFile": "client/src/json/MARYLAND/md_county.json",
        "PrecinctDemographicFile": "client/src/json/MARYLAND/md_precincts_dem.json",
        "DistrictCounties": {
            "District 1":["Caroline", "Cecil", "Dorchester", "Kent", "Queen Anne's", "Somerset", "Talbot", "Wicomico", "Worcester"],
            "District 2":["Anne Arundel", "Harford"],
            "District 3":["Baltimore City"],
            "District 4":["Prince George's"],
            "District 5":["Calvert", "Charles", "St. Mary's"],
            "District 6":["Allegany", "Frederick", "Garrett", "Washington", "Carroll"],
            "District 7":["Howard", "Baltimore"],
            "District 8":["Montgomery"],
        }
    },
    "PA": {
        "Name": "Pennsylvania",
        "Abbrev": "PA",
        "DistrictFile": "client/src/json/PENNSYLVANIA/pa_congressionalDistrict.json",
        "PrecinctFile": "client/src/json/PENNSYLVANIA/pa_precincts.json",
        "CountyFile": "client/src/json/PENNSYLVANIA/pa_county.json",
        "PrecinctDemographicFile": "client/src/json/PENNSYLVANIA/pa_precincts_dem.json",
         "DistrictCounties":{
            "District 1":["Delaware"],
            "District 2": ["Philadelphia"],
            "District 3":["Butler", "Armstrong", "Mercer", "Crawford","Lawrence"],
            "District 4":["Adams", "York"],
            "District 5":["Erie","Bradford", "Potter", "McKean", "Warren", "Elk", "Forest", "Venango", "Cameron","Clinton","Clarion", "Jefferson", "Clearfield", "Centre","Huntingdon"],
            "District 6":["Berks"],
            "District 7":["Chester"],
            "District 8":["Bucks"],
            "District 9":["Indiana", "Blair","Bedford", "Franklin", "Fulton", "Fayette"],
            "District 10":["Monroe", "Tioga", "Wayne", "Susquehanna", "Pike","Lycoming", "Sullivan", "Union", "Snyder", "Mifflin","Juniata","Perry"],
            "District 11":["Montour","Cumberland", "Dauphin", "Wyoming", "Luzerne", "Columbia", "Northumberland"],
            "District 12":["Beaver", "Cambria","Somerset"],
            "District 13":["Montgomery"],
            "District 14": ["Allegheny"],
            "District 15": ["Lehigh", "Northampton", "Lebanon"],
            "District 16":["Lancaster"],
            "District 17":["Carbon", "Lackawanna", "Schuylkill"],
            "District 18":["Westmoreland", "Washington", "Greene"],
            "District 19": []
        }
    },
    "GA" :{
        "Name": "Georgia",
        "Abbrev": "GA",
        "DistrictFile": "client/src/json/GEORGIA/ga_congressionalDistrict.json",
        "PrecinctFile": "client/src/json/GEORGIA/ga_precincts.json",
        "CountyFile": "client/src/json/GEORGIA/ga_county.json",
        "PrecinctDemographicFile": "client/src/json/GEORGIA/ga_precincts_dem.json",
        "DistrictCounties":{
            "District 1":["Bryan", "Chatham","Liberty", "Long", "McIntosh", "Wayne", "Bacon", "Pierce", "Glynn",
                "Camden", "Charlton", "Brantley", "Ware", "Clinch", "Echols", "Appling", "Jeff Davis",
                "Wheeler", "Telfair", "Coffee","Atkinson", "Berrien", "Cook", "Lanier", "Lowndes"],
            "District 2":["Talbot", "Taylor", "Crawford", "Peach",  "Macon", "Dooly", "Crisp", "Marion",
                "Chattahoochee", "Schley", "Stewart", "Webster", "Sumter", "Quitman", "Randolph", "Terrell", "Lee",
                "Clay", "Calhoun", "Dougherty", "Early", "Baker", "Mitchell", "Miller", "Seminole", "Decatur", "Grady", "Muscogee",
                "Worth", "Thomas", "Brooks"],
            "District 3":["Carroll", "Heard", "Coweta", "Fayette", "Spalding","Troup", "Meriwether", "Pike", "Lamar", "Harris", "Upson"],
            "District 4":["Newton", "Rockdale"],
            "District 5":["Fulton", "DeKalb"],
            "District 6":[],
            "District 7":["Gwinnett", "Forsyth"],
            "District 8":["Colquitt", "Tift", "Turner", "Irwin", "Ben Hill", "Wilcox", "Dodge", "Pulaski", "Bleckley", "Laurens", "Houston",
                "Twiggs", "Wilkinson", "Bibb", "Jones", "Monroe", "Jasper", "Butts"],
            "District 9":["Dade", "Walker", "Catoosa", "Whitfield", "Murray", "Pickens", "Gilmer",
                "Union", "Fannin", "White", "Lumpkin", "Dawson", "Hall"],
            "District 10":["Towns", "Rabun", "Habersham", "Stephens", "Banks", "Franklin", "Hart", "Jackson", "Madison", 
                "Elbert", "Clarke", "Barrow", "Oglethorpe", "Wilkes", "Lincoln", "Oconee", "Morgan", "Walton", "Greene", "Putnam"],
            "District 11":["Chattooga", "Gordon", "Floyd", "Bartow", "Cherokee", "Polk", "Paulding", 
                "Haralson", "Cobb"],
            "District 12":["Taliaferro", "Warren", "McDuffie", "Columbia", "Hancock","Baldwin", "Washington", "Glascock", "Richmond", "Jefferson",
                "Johnson", "Burke", "Jenkins", "Emanuel", "Screven", "Treutlen", "Montgomery", "Tattnall", "Toombs", "Candler", "Evans", "Bulloch", "Effingham"],
            "District 13":["Douglas", "Clayton", "Henry"],
        }
    }
}

def main():
    writeHeatMapFilesToStates()
    pass


def writeHeatMapFilesToStates():
    ethnicity = ["white","black","american_indian","asian","hispanic","native_hawaiian","other"]
    for state in States:
        demographic = formatPrecinctDemographicData(States[state])
        for e in ethnicity:
            backendpath = 'backend/src/main/resources/system/states/' + state.lower() + '/heatmap/' + e + ".json"
            total_ethincity_population = 0
            total_state_population = 0
            for d in demographic:
                total_state_population = demographic[d]["total"] + total_state_population
                if e == "other":
                    total_ethincity_population = demographic[d]["other"] + total_ethincity_population
                    total_ethincity_population = demographic[d]["multiple"] + total_ethincity_population
                else:
                    total_ethincity_population = demographic[d][e] + total_ethincity_population
            print(state + ": " + e + " population: " + str(total_ethincity_population))
            f = open(States[state]["PrecinctFile"])
            data = json.load(f)
            num_of_precinct = len(list(data['features']))
            average_population = total_ethincity_population//num_of_precinct
            for feature in data['features']:
                properties = feature['properties']
                try:
                    fips = properties['STATE'] + properties['COUNTY'] + properties['VTD']
                    precinct_demographic = 0
                    if e == "other":
                       precinct_demographic = demographic[fips]["other"] + demographic[fips]["multiple"]
                    else:
                        precinct_demographic = demographic[fips][e]
                except Exception:
                    precinct_demographic = 0
                properties.setdefault('fillColor', fillcolor_heatmap(precinct_demographic, average_population)) 
            writeToFile(data, backendpath)
            
def fillcolor_heatmap(precinct_demographic, average_population):
    fillColor = "hsl(180, 5%, 64%)"   
    if (precinct_demographic <= average_population * 0.09): 
        fillColor = "hsl(180, 5%, 64%)"   
    elif (precinct_demographic >= average_population * 0.1 and precinct_demographic < average_population * 0.2):
        fillColor = "hsl(180, 10%, 64%)"
    elif (precinct_demographic >= average_population * 0.2 and precinct_demographic < average_population * 0.3):
        fillColor = "hsl(180, 16%, 64%)"
    elif (precinct_demographic >= average_population * 0.3 and precinct_demographic < average_population * 0.4):
        fillColor = "hsl(180, 22%, 64%)"
    elif (precinct_demographic >= average_population * 0.4 and precinct_demographic < average_population * 0.5):
        fillColor = "hsl(180, 27%, 64%)"
    elif (precinct_demographic >= average_population * 0.5 and precinct_demographic < average_population * 0.6):
        fillColor = "hsl(180, 32%, 64%)"
    elif (precinct_demographic >= average_population * 0.6 and precinct_demographic < average_population * 0.7):
        fillColor = "hsl(180, 38%, 64%)"
    elif (precinct_demographic >= average_population * 0.7 and precinct_demographic < average_population * 0.8):
        fillColor = "hsl(180, 43%, 64%)"
    elif (precinct_demographic >= average_population * 0.8 and precinct_demographic < average_population * 0.95):
        fillColor = "hsl(180, 49%, 64%)"
    elif (precinct_demographic >= average_population * 0.95 and precinct_demographic <= average_population * 1.05):
        fillColor ="hsl(180, 50%, 64%)"
    elif (precinct_demographic >= average_population * 1.06 and precinct_demographic < average_population * 1.1):
        fillColor = "hsl(180, 55%, 64%)"
    elif (precinct_demographic >= average_population * 1.1 and precinct_demographic < average_population * 1.2):
        fillColor = "hsl(180, 60%, 64%)"
    elif (precinct_demographic >= average_population * 1.2 and precinct_demographic < average_population * 1.3):
        fillColor = "hsl(180, 66%, 64%)"
    elif (precinct_demographic >= average_population * 1.3 and precinct_demographic < average_population * 1.4):
        fillColor = "hsl(180, 72%, 64%)"
    elif (precinct_demographic >= average_population * 1.4 and precinct_demographic < average_population * 1.5):
        fillColor = "hsl(180, 77%, 64%)"
    elif (precinct_demographic >= average_population * 1.5 and precinct_demographic < average_population * 1.6):
        fillColor = "hsl(180, 82%, 64%)"
    elif (precinct_demographic >= average_population * 1.6 and precinct_demographic < average_population * 1.7):
        fillColor = "hsl(180, 88%, 64%)"
    elif (precinct_demographic >= average_population * 1.7 and precinct_demographic < average_population * 1.8):
        fillColor = "hsl(180, 93%, 64%)"
    elif (precinct_demographic >= average_population * 1.8 and precinct_demographic < average_population * 1.9):
        fillColor = "hsl(180, 97%, 64%)"
    elif (precinct_demographic >= average_population * 1.9): fillColor = "hsl(180, 100%, 64%)" 
    return fillColor

def writeAlgorithmFormatToStatesDir():
    for state in States:
        backendpath = 'backend/src/main/resources/system/states/' + state.lower() + '/AlgorithmPrecincts.json'
        neighbor = computePrecinctNeighbors(States[state])
        demographic = formatPrecinctDemographicData(States[state])
        formatFile = formatAlgoritmFile(neighbor, demographic)
        
        writeToFile(formatFile, backendpath)

def formatAlgoritmFile(dictPrecinctNeighbors, dictPrecinctDemographicData):
    algorithmDict = {}
    included = []
    for precinctKey in dictPrecinctNeighbors:
        try:
            p_neighbors = dictPrecinctNeighbors[precinctKey]
        except Exception as e:
            print(str(e) + " ERROR: p_neighbors") 
            p_neighbors = {}
    
        try:
            p_demographic = dictPrecinctDemographicData[precinctKey]
        except Exception as e:
            print(str(e) + " ERROR: p_demographic")
            p_demographic = {}

        
        struct = dict(neighbors=p_neighbors, demographic=p_demographic)
        algorithmDict.setdefault(precinctKey,struct)

    #print(included)
    return algorithmDict

def formatDistrictData(state):
    newDict = {}

    f = open(state["DistrictFile"])

    data = json.load(f)

    # print(data['features'][0]['properties'])
    
    for feature in data['features']:
        properties = feature['properties']
    
        temp = {
            'districtNumber': int(properties['CD']),
            'stateId': state["Abbrev"]
        }

        newDict[properties['CD']] = temp
    

    return newDict

def formatCountyData(state):
    newDict = {}

    f = open(state["CountyFile"])
    data = json.load(f)

    for feature in data['features']:

        properties = feature['properties']
       
        temp = {
            'countyFIPSCode' : properties['STATE'] + properties['COUNTY'],
            'stateId': state["Abbrev"]
        }

        newDict[properties['NAME']] = temp
   
    return newDict

def formatPrecinctData(state):

    newDict = {}
    countyDict = {}
    f = open(state["PrecinctFile"])
    data = json.load(f)

    if state['Abbrev'] == 'GA':
        countyDict = formatGACountyDistrictData(state)
    elif state['Abbrev'] == 'MD':
        countyDict = formatMDCountyDistrictData(state)
    else:
        countyDict = formatPACountyDistrictData(state)

    for feature in data['features']:
        properties = feature['properties']

        countyFips = properties['STATE'] + properties['COUNTY']

        
        getDistrictNum = findCorrespondingKey(countyDict, countyFips)
        getDistrictNum = getDistrictNum['districtFIPS']

        temp = {
            'precinctFIPSCode' : properties['STATE'] + properties['COUNTY'] + properties['VTD'],
            'precinctName': properties['NAME'],
            'countyFIPSCode': countyFips,
            'districtNumber': getDistrictNum,
            'stateId':  state["Abbrev"],
        }

        newDict[properties['NAME']] = temp
      
    #newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

    return newDict
  
def getCountyDistrict(dictionary, countyToFind):
    
    for district in dictionary:
        for county in dictionary[district]:
            if county.lower() == countyToFind.lower():
                districtNum = district.split(' ')[1]
                return districtNum
    return None

def formatGACountyDistrictData(state):
  
    newDict = {}

    f = open(state["PrecinctDemographicFile"])
    data = json.load(f)

    # print(data['features'][0]['properties'])

    for feature in data['features']:
        properties = feature['properties']

        temp = {
            'districtFIPS' : properties['CD'],
            'stateId':  state["Abbrev"],
            'countyName': properties['CTYNAME'],
            'countyFIPS': properties['FIPS1']
        }

        newDict[properties['FIPS1']] = temp

    return newDict

def formatPACountyDistrictData(state):
      
    newDict = {}
    countyDict = {}

    f = open(state["CountyFile"])
    data = json.load(f)

    for feature in data['features']:
        properties = feature['properties']

        temp = {
            'countyName' : properties['NAME'],
            'countyFIPS': properties['COUNTY']
        }

        countyDict[properties['COUNTY']] = temp
      
    f = open(state["PrecinctDemographicFile"])
    data = json.load(f)

    for feature in data['features']:
        properties = feature['properties']

        countyName = countyDict[properties['COUNTYFP10']]['countyName']

        if countyName is None: continue

        temp = {
            'districtFIPS' : properties['CD_2011'],
            'stateId':  state["Abbrev"],
            'countyName': countyName,
            'countyFIPS': properties['STATEFP10'] + properties['COUNTYFP10']
        }

        newDict[properties['STATEFP10'] + properties['COUNTYFP10']] = temp
    

    return newDict

def formatMDCountyDistrictData(state):
    newDict = {}
    countyDict = {}

    f = open(state["CountyFile"])
    data = json.load(f)

    for feature in data['features']:
        properties = feature['properties']

        temp = {
            'countyName' : properties['NAME'],
            'countyFIPS': properties['COUNTY']
        }

        countyDict[properties['COUNTY']] = temp
      
    f = open(state["PrecinctDemographicFile"])
    data = json.load(f)

    for feature in data['features']:
        properties = feature['properties']

        countyName = countyDict[properties['COUNTY'][2:]]['countyName']
    
        if countyName is None: continue

        temp = {
            'districtFIPS' : properties['CD'],
            'stateId':  state["Abbrev"],
            'countyName': countyName,
            'countyFIPS': properties['COUNTY']
        }

        newDict[properties['COUNTY']] = temp
      

    return newDict

def findCorrespondingKey(dictionary, value):
    
    for key in dictionary:
        if(key[0] == value): return key[1]
    return None

def writeToFile(dictionary, file):
    with open(file, 'w') as jsonfile:
        jsonfile.write(json.dumps(dictionary, indent=4))

def formatPrecinctDemographicData(state):

    newDict = {}
  
    f = open(state["PrecinctDemographicFile"])
    data = json.load(f)

    # print(data['features'][0]['properties'])

    for feature in data['features']:
        properties = feature['properties']

        precinct = ''

        if state['Abbrev'] == 'PA':
            precinct = properties['GEOID10']
        elif state['Abbrev'] == 'GA':
            precinct = properties['FIPS1'] + properties['PRECINCT_I']
        else:
            precinct = properties['VTD']


        temp = {
            "total": properties['TOTPOP'],
            "white": properties['NH_WHITE'],
            "black": properties['NH_BLACK'],
            "american_indian": properties['NH_AMIN'],
            "asian": properties['NH_ASIAN'],
            "hispanic":properties['HISP'],
            "native_hawaiian": properties['NH_NHPI'],
            "other": properties['NH_OTHER'],
            "multiple": properties['NH_2MORE'],
            "vap": properties['VAP'],
            "white_vap": properties['WVAP'],
            "black_vap": properties['BVAP'],
            "hispanic_vap":properties['HVAP'],
            "american_indian_vap": properties['AMINVAP'],
            "asian_vap": properties['ASIANVAP'],
            "native_hawaiian_vap": properties['NHPIVAP'],
            "other_vap": properties['OTHERVAP'],
            "multiple_vap": properties['2MOREVAP'],
            "state": None,
            "district": None,
            "county": None,
            "precinct": precinct,
            "stateId": state['Abbrev']
            
        }

        newDict[precinct] = temp
      
    #newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

    return newDict

def convertToFeet(latitude, longitude):
    
    #Convert the longitude, latitude to km
    #Conversion factor 10,000 km per 90 deg
    longitude = longitude * (10000/90)
    latitude = latitude * (10000/90)

    #Convert the kilometers to feet
    #Conversion factor 3280.4 feet per km
    longitude *= 3280.4
    latitude *= 3280.4

    return (longitude, latitude)

def computePrecinctNeighbors(state):

    f = open(state["PrecinctFile"])

    precinct_dict = createAlteredPrecinctDictionary(f)
    precinct_neighbors = {}

    precincts_geometries, precinct_identifier = createPrecinctGeometryList(precinct_dict)

    #Feed precinct geometries 
    tree = STRtree(precincts_geometries)

    for precinct in precincts_geometries:

        #Get id 
        bufferPrecinctId = id(precinct)

        #Buffer the precinct polygon by 200 ft
        buffer_precinct = precinct.buffer(200)
        
        #Loop through list of geometries that intersect buffer_precinct
        for o in tree.query(buffer_precinct):

            #Make sure that the buffer precinct is not being checked
            #against itself
            if precinct_identifier[id(o)] != precinct_identifier[bufferPrecinctId]:
                
                #Get length from the representation of the intersection btwn the two object
                intersectionLength = buffer_precinct.intersection(o).length

                if intersectionLength >= 200:

                    #Add to buffer_precinct neighbor list
                    getPrecinctFIPS = precinct_identifier[id(o)]
                    getBufferPrecinctFIPS = precinct_identifier[bufferPrecinctId]

                    
                    if getBufferPrecinctFIPS not in precinct_neighbors:
                        precinct_neighbors[getBufferPrecinctFIPS] = []
                    else:
                        #Check if the precinct neighbor was already accounted and if not add to the
                        #precincts neighbor array
                        if not precinctNeighborAccountedFor(getPrecinctFIPS, precinct_neighbors[getBufferPrecinctFIPS]):
                           precinct_neighbors[getBufferPrecinctFIPS].append(getPrecinctFIPS)

                    if getPrecinctFIPS not in precinct_neighbors:
                        precinct_neighbors[getPrecinctFIPS] = []
                    else:
                        #Check if the precinct neighbor was already accounted and if not add to the
                        #precincts neighbor array
                        if not precinctNeighborAccountedFor(getBufferPrecinctFIPS, precinct_neighbors[getPrecinctFIPS]):
                           precinct_neighbors[getPrecinctFIPS].append(getBufferPrecinctFIPS)

    return precinct_neighbors
          
def createPrecinctGeometryList(precinct_dict):
    
    geometries = []
    precinctIdentifier = {}

    for precinct in precinct_dict:
        coord = precinct_dict[precinct]['coordinates']

        for poly in coord:
            geometries.append(poly)
            precinctIdentifier[id(poly)] = precinct_dict[precinct]['precinctFIPSCode']

    return (geometries, precinctIdentifier)

def createAlteredPrecinctDictionary(file):

    data = json.load(file)

    #Create Dictionary
    precinct_dict = {}
    
    #Loop through each precinct feature 
    for feature in data['features']:

        properties = feature['properties']
        geometry = feature['geometry']

        precinctFIPSCode = properties['STATE'] + properties['COUNTY'] + properties['VTD']

        temp = {
                "precinctFIPSCode": precinctFIPSCode,
                "coordinates" : []
        }

        #Loop through the coordinates in geometry attributes
        for polygonCoordinate in geometry['coordinates']:
             
            coordinate_array = []
            
            #convert the decimal degree values to feet and store
            for longitude, latitude in polygonCoordinate:

                x, y = convertToFeet(latitude, longitude)
                newPoint = Point(x,y)

                coordinate_array.append(newPoint)
            
            #construct a new coordinate with the newly converted points 
            temp['coordinates'].append(Polygon(coordinate_array))

        precinct_dict[precinctFIPSCode] = temp
      
    return precinct_dict

def precinctNeighborAccountedFor(precinct, precinctNeighbor_array):

    for neighbor in precinctNeighbor_array:
        if precinct == neighbor: return True
    
    return False





if __name__ == '__main__':
    main()


