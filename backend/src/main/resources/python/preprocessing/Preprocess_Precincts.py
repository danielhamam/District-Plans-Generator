from json import load, dumps


def format_coor(state):
    path = '../../system/states/' + state + "/"
    file = open(path + 'Precincts.json', 'r')
    outfile = open(path + 'PrecinctsCoordinates.json', 'w')
    geojson_file = load(file)
    outjson = dict()

    features_list = geojson_file['features']
    for feature in features_list:
        coor = feature['geometry']['coordinates'][0]
        # struct={'coordinates':coor}
        # properties['STATE'] + properties['COUNTY'] + properties['VTD'],
        fips = feature['properties']['STATE'] + feature['properties']['COUNTY'] + feature['properties']['VTD']
        outjson.setdefault(fips ,coor)

    # print(outjson)
    newjsonfile = dumps(outjson, indent=4)
    outfile.write(newjsonfile)


def precinctFIPSCounty(state):
    path = '../../system/states/' + state + "/"
    file = open(path + 'Precincts.json', 'r')
    outfile = open(path + 'PrecinctsCoordinates.json', 'w')
    geojson_file = load(file)
    outjson = dict()

    features_list = geojson_file['features']
    for feature in features_list:
        coor = feature['geometry']['coordinates'][0]
        # struct={'coordinates':coor}
        # properties['STATE'] + properties['COUNTY'] + properties['VTD'],
        fips = feature['properties']['STATE'] + feature['properties']['COUNTY'] + feature['properties']['VTD']
        outjson.setdefault(fips ,coor)

    # print(outjson)
    newjsonfile = dumps(outjson, indent=4)
    outfile.write(newjsonfile)



def main():
    states = ['md','pa','ga']
    for state in states:
        print(state)
        format_coor(state)



if __name__ == "__main__":
    main()