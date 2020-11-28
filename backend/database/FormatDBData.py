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
        "PrecinctDemographicFile": "client/src/json/MARYLAND/md_precincts_dem.json"
    },
    "PA": {
        "Name": "Pennsylvania",
        "Abbrev": "PA",
        "DistrictFile": "client/src/json/PENNSYLVANIA/pa_congressionalDistrict.json",
        "PrecinctFile": "client/src/json/PENNSYLVANIA/pa_precincts.json",
        "CountyFile": "client/src/json/PENNSYLVANIA/pa_county.json",
        "PrecinctDemographicFile": "client/src/json/PENNSYLVANIA/pa_precincts_dem.json"
    },
    "GA" :{
        "Name": "Georgia",
        "Abbrev": "GA",
        "DistrictFile": "client/src/json/GEORGIA/ga_congressionalDistrict.json",
        "PrecinctFile": "client/src/json/GEORGIA/ga_precincts.json",
        "CountyFile": "client/src/json/GEORGIA/ga_county.json",
        "PrecinctDemographicFile": "client/src/json/GEORGIA/ga_precincts_dem.json"
    }
}

def main():
    state = 'MD'
    path = 'backend/src/main/java/resourses/state/' + state.lower() + '/PrecinctsNeighbors.json'
    neighbor = computePrecinctNeighbors(States[state])
    demographic = formatPrecinctDemographicData(States[state])
    getDict = formatAlgoritm(neighbor, demographic)
    writeToFile(getDict, 'backend/database/PrecinctsNeighbors.json')

def formatAlgoritm(dictPrecinctNeighbors, dictPrecinctDemographicData):
    algorithmDict = {}
    included = []
    # a = list(dictPrecinctNeighbors.keys())[0]
    # print(a)
    # print(dictPrecinctNeighbors[a])
    # a = list(dictPrecinctNeighbors.keys())[0]
    # print(a)
    # print(dictPrecinctNeighbors[a])
    for precinctKey in dictPrecinctDemographicData:
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
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

    return newDict

def formatCountyData(state):
    newDict = {}

    f = open(state["CountyFile"])
    data = json.load(f)

    # print(data['features'][0]['properties'])

    for feature in data['features']:

        properties = feature['properties']
       
        temp = {
            'countyFIPSCode' : properties['STATE'] + properties['COUNTY'],
            'stateId': state["Abbrev"]
        }

        newDict[properties['NAME']] = temp
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)
  
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
      
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

    return newDict
  
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
      
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

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
      
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

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
      
    newDict = sorted(newDict.items(), key=lambda x: x[1], reverse=False)

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

    precinct_dict = createPrecinctDictionary(f)
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
                        precinct_neighbors[getBufferPrecinctFIPS].append(getPrecinctFIPS)

                    
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

def createPrecinctDictionary(file):

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

# def createNewPrecinctFile(state):

#     #Create the file to dictionary
#     newDict = extractFileGeometryAttribute(state['PrecinctFile'])

#     # writeToFile(newDict, '/Users/kadiatoudiallo/Documents/GitHub/CSE-416-Project/client/src/json/testPrecinct.json')
    
#     f = open(state['PrecinctDemographicFile'])
#     data = json.load(f)

#     for feature in data['features']:

#         precinctFIPS = ''

#         if state['Abbrev'] == 'MD': 
#             precinctFIPS = feature['properties']['VTD_1']
#         elif state['Abbrev'] == 'GA': 
#             precinctFIPS = feature['properties']['PRECINCT_I']
#         else: 
#             precinctFIPS = feature['properties']['VTDST10']

#         if precinctFIPS not in newDict: continue

#         properties = feature['properties']

#         newDict[precinctFIPS]['properties']['TOTPOP'] = properties['TOTPOP']
#         newDict[precinctFIPS]['properties']['NH_WHITE'] = properties['NH_WHITE']
#         newDict[precinctFIPS]['properties']['NH_BLACK'] = properties['NH_BLACK']
#         newDict[precinctFIPS]['properties']['NH_AMIN'] = properties['NH_AMIN']
#         newDict[precinctFIPS]['properties']['NH_ASIAN'] = properties['NH_ASIAN']
#         newDict[precinctFIPS]['properties']['NH_NHPI'] = properties['NH_NHPI']
#         newDict[precinctFIPS]['properties']['NH_OTHER'] = properties['NH_OTHER']
#         newDict[precinctFIPS]['properties']['NH_2MORE'] = properties['NH_2MORE']
#         newDict[precinctFIPS]['properties']['HISP'] = properties['HISP']
#         newDict[precinctFIPS]['properties']['VAP'] = properties['VAP']
#         newDict[precinctFIPS]['properties']['HVAP'] = properties['HVAP']
#         newDict[precinctFIPS]['properties']['WVAP'] = properties['WVAP']
#         newDict[precinctFIPS]['properties']['BVAP'] = properties['BVAP']
#         newDict[precinctFIPS]['properties']['AMINVAP'] = properties['AMINVAP']
#         newDict[precinctFIPS]['properties']['ASIANVAP'] = properties['ASIANVAP']
#         newDict[precinctFIPS]['properties']['NHPIVAP'] = properties['NHPIVAP']
#         newDict[precinctFIPS]['properties']['OTHERVAP'] = properties['OTHERVAP']
#         newDict[precinctFIPS]['properties']['2MOREVAP'] = properties['2MOREVAP']

#     featureCollection = {
#         'type': 'FeatureCollection',
#         'features': []
#     }

#     for feature in newDict:
#         featureCollection['features'].append(newDict[feature])
    
#     with open('client/src/json/testPrecinct.json', 'w') as jsonfile:
#         jsonfile.write(json.dumps(featureCollection))
       



# def extractFileGeometryAttribute(file):
#     f = open(file)
#     data = json.load(f)

#     dictionary = {}

#     for feature in data['features']:
        
#         temp = {
#             "type": feature['type'],
#             "geometry": feature['geometry'],
#             "properties" : feature['properties']
#         }
#         #Add new Attributes
#         properties = feature['properties']

#         properties['TOTPOP'] = 0
#         properties['NH_WHITE'] = 0
#         properties['NH_BLACK'] = 0
#         properties['NH_AMIN'] = 0
#         properties['NH_ASIAN'] = 0
#         properties['NH_NHPI'] = 0
#         properties['NH_OTHER'] = 0
#         properties['NH_2MORE'] = 0
#         properties['HISP'] = 0
#         properties['VAP'] = 0
#         properties['HVAP'] = 0
#         properties['WVAP'] = 0
#         properties['BVAP'] = 0
#         properties['AMINVAP'] = 0
#         properties['ASIANVAP'] = 0
#         properties['NHPIVAP'] = 0
#         properties['OTHERVAP'] = 0
#         properties['2MOREVAP'] = 0


#         dictionary[feature['properties']['VTD']] = temp

#     return dictionary





if __name__ == '__main__':
    main()


