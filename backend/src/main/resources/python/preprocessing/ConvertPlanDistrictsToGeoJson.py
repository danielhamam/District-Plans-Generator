import geopandas
import json
from shapely.ops import unary_union


States = {
    'MD' : {
        "Name": "Maryland",
        "Abbrev": "MD",
        "FIPS": "24",
        "PrecinctFile": "backend/src/main/resources/system/states/md/Precincts.json",
    },
    "PA": {
        "Name": "Pennsylvania",
        "Abbrev": "PA",
        "FIPS": "42",
        "PrecinctFile": "backend/src/main/resources/system/states/pa/Precincts.json"
    },
    "GA" :{
        "Name": "Georgia",
        "Abbrev": "GA",
        "FIPS": "13",
        "PrecinctFile": "backend/src/main/resources/system/states/ga/Precincts.json"
    }
}
def main():
    
    with open('backend/src/main/resources/python/preprocessing/tester_Algo_Output.json') as f:
        planDistrict = json.load(f)

        plans = planDistrict['plans']
        state = plans[0]['stateAbbrev']

        for plan in plans:
            createNewDistrictBoundaries(States[state]["PrecinctFile"], plan, States[state]["FIPS"])
        

def createNewDistrictBoundaries(file, plan, stateFips):

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
    filepath = 'backend/src/main/resources/python/preprocessing/' + plan['type'] +'Plan_Job'+ str(plan['jobId']) +  '_Plan' + str(plan['planId']) + '.json'
    districts_dataFrame.to_file(filepath, driver='GeoJSON')

  


    
  
if __name__ == "__main__":
    main()