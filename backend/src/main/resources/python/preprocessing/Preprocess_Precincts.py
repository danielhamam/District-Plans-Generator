from json import load, dumps


def main():
    path = '../../system/states/pa/'
    file = open(path + 'Precincts.json', 'r')
    outfile = open(path + 'PreprocessedPrecinct.json', 'w')
    geojson_file = load(file)
    outjson = dict()

    features_list = geojson_file['features']
    for feature in features_list:
        coor = feature['geometry']['coordinates']
        struct={'coordinates':coor}
        outjson.setdefault(feature['properties']['NAME'],struct)

    # print(outjson)
    newjsonfile = dumps(dict(precincts=outjson), indent=4)
    outfile.write(newjsonfile)


if __name__ == "__main__":
    main()