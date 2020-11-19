from json import load, dumps


def main():
    file = open('Precincts.json', 'r')
    outfile = open('PreprocessedPrecinct.json', 'w')
    geojson_file = load(file)
    outjson = dict()

    features_list = geojson_file['features']
    for feature in features_list:
        coor = feature['geometry']['coordinates']
        struct={'coordinates':coor}
        outjson.setdefault(feature['properties']['VTD'],struct)

    # print(outjson)
    newjsonfile = dumps(dict(precincts=outjson), indent=4)
    outfile.write(newjsonfile)


if __name__ == "__main__":
    main()