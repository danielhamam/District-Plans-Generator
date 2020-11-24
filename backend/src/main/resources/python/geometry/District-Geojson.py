from json import load, dumps
import argparse
import sys

system_state_path = '../../system/states'
dict_district = None
dict_preprocessed_precincts = None
output_file_name = None


def feature_collection_struct():
    output_json = dict(type='FeatureCollection', features=[])
    return output_json


def feature_struct():
    geometry_struct = dict(type='Polygon', coordinates=[])
    feature_struct = dict(type='Feature', geometry=geometry_struct, properties={})
    print(feature_struct)
    return feature_struct


def set_global_variable(args):
    global system_state_path
    global dict_district
    global dict_preprocessed_precincts
    global output_file_name
    args = parser.parse_args()
    output_file_name = args.outfile_name
    path = system_state_path + '/' + args.state[0].lower()
    preprocessed_precincts_file = load(open(path + '/' + 'PreprocessedPrecinct.json', 'r'))
    precincts_geojson_file = load(open(path + '/' + 'Precincts.json', 'r'))
    dict_district_file = load(args.infile[0])
    dict_preprocessed_precincts = preprocessed_precincts_file['precincts']
    dict_district = dict_district_file['districts']


def combine_precinct_geojson(district:dict):
    global dict_preprocessed_precincts
    feature_geojson = feature_struct()
    precincts = district['precincts']
    district_coordinates = []
    for precinct_name in precincts:
        district_coordinates = district_coordinates + dict_preprocessed_precincts[precinct_name]['coordinates']
    district_coordinates.append(district_coordinates[0])
    feature_geojson['geometry']['coordinates'] = district_coordinates
    return feature_geojson


def write_output(dict_output:dict):
    global output_file_name
    output_file = open(output_file_name + '.json', 'w')
    output_file.write(dumps(dict_output, indent=4))


def main(args):
    global dict_district
    global dict_preprocessed_precincts
    set_global_variable(args)
    output_geojson = feature_collection_struct()
    for district_name in dict_district.keys():
        district = dict_district[district_name]
        new_feature = combine_precinct_geojson(district)
        output_geojson['features'].append(new_feature)
    write_output(output_geojson)


def shared_coordinates(coordinate_one:list, coordinate_two:list):
    coordinates = []
    for coordinate in coordinate_one:
        if coordinate in coordinate_two:
            coordinates.append(coordinate)
    return coordinates


def lowest_ycoordinate(coordinates:list):
    y_coordinate = coordinates[0]
    for coordinate in coordinates:
        if coordinate[1] < y_coordinate[1]:
            y_coordinate = coordinate
    return y_coordinate



def remove_shared_coordinates(shared_coordinates:list, coordinates:list):
    for coordinate in shared_coordinates:
        while True:
            try:
                coordinates.remove(coordinate)
            except:
                break
    if coordinates[0] != coordinates[-1]:
        coordinates.append(coordinates[0])


def re(shared_coordinates:list, coordinate_one:list, coordinate_two:list):
    pass


def merge_coor(coordinate_one:list, coordinate_two:list):
    coordinate_one.pop()
    coordinate_two.pop()
    merged_coordinates = []
    merged_coordinates = coordinate_one + coordinate_two
    merged_coordinates.append(merged_coordinates[0])
    return merged_coordinates



def merge_coordinates(coordinate_one:list, coordinate_two:list, coordinate_shared:list):
    coordinate_one.pop()
    coordinate_two.pop()

    print(coordinate_one)
    print(coordinate_two)
    merged_coordinates = []
    is_merge = False
    # for coordinate in coordinate_one:
    #     merged_coordinates.append(coordinate)
    #     if coordinate[0] > coordinate_two[0][0] and not is_merge:
    #         for coordinat in coordinate_two:
    #             merged_coordinates.append(coordinat)
    #         # merged_coordinates.append(coordinate)
    #         is_merge = True
    #     else:
    #         pass


    # print(coordinate_one[:5], "||||",coordinate_one[-1])
    # print(coordinate_two[:5], "||||",coordinate_two[-1])
    merged_coordinates = coordinate_two + coordinate_one
    merged_coordinates.append(merged_coordinates[0])
    #merged_coordinates = [coordinate_two[0]] + coordinate_one + coordinate_two[1:]
    # merged_coordinates = [coordinate_one[0]] + coordinate_two + coordinate_one[1:]
    return merged_coordinates


def sort_coordinate_by_x(val):
    return val[0]


def test():
    file = open('testdistricts.json', 'r')
    geojson_file = load(file)
    outjson = dict()
    features_list = geojson_file['features']
    district_one_geojson = features_list[0]
    district_two_geojson = features_list[1]
    district_one_coordinates = district_one_geojson['geometry']['coordinates'][0]
    district_two_coordinates_ = district_two_geojson['geometry']['coordinates'][0]
    maxx = max(len(district_one_coordinates), len(district_two_coordinates_))
    trash = []

    outfile = open('s.json', 'w')

    # geometry_struct = dict(type='Polygon', coordinates=[])
    # feature_struct = = dict(type='Feature', geometry=geometry_struct)
    # outjson = dict(type='FeatureCollection', features=[feature_struct])
    # newjsonfile = dumps(outjson, indent=4)
    # outfile.write(newjsonfile)

    if maxx == len(district_one_coordinates):
        share_coor = shared_coordinates(district_one_coordinates, district_two_coordinates_)
        remove_shared_coordinates(share_coor,district_one_coordinates)
        remove_shared_coordinates(share_coor,district_two_coordinates_)
        #merged_coor = merge_coordinates(district_one_coordinates, district_two_coordinates_)
        merged_coor = merge_coor(district_one_coordinates, district_two_coordinates_)

        outjson = feature_collection_struct()
        outfeature = feature_struct()
        outfeature['properties'] = district_one_geojson['properties']
        outfeature['geometry']['coordinates']=[merged_coor]
        outjson['features'].append(outfeature)
        wfile = dumps(outjson, indent=4)
        outfile.write(wfile)

    else:
        # share_coor = shared_coordinates(district_one_coordinates, district_two_coordinates_)
        # remove_shared_coordinates(share_coor,district_one_coordinates)
        pass




# for feature in features_list:
    #     coor = feature['geometry']['coordinates']
    #     struct={'coordinates':coor}
    #     outjson.setdefault(feature['properties']['VTD'],struct)

    # print(outjson)


def commandline():
    parser = argparse.ArgumentParser(prog="district-geojson",
                                     description='Used to generate district geojson from algorithm-output')
    parser.add_argument('state',  help='State abbreviation', nargs=1, type=str)
    parser.add_argument('infile', help='Algo-output file', type=argparse.FileType('r'), nargs=1,
                        default=sys.stdin)
    parser.add_argument('outfile_name',  help='Name of output file', nargs=1, type=str)
    # parser.add_argument('-i', help=interfaceOptionHelp, nargs = "?")
    # parser.add_argument('-f', help=hostnameOptionHelp, type=argparse.FileType('r'))
    return parser


if __name__ == "__main__":
    parser = commandline()
    if len(sys.argv) <= 1:
        parser.print_help()
    else:
        main(parser)
    # test()