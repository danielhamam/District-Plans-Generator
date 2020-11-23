from json import load, JSONEncoder, dumps
from argparse import ArgumentParser, FileType
from re import compile
import argparse, os, signal, sys

interfaceOptionHelp = "Listen on network device <interface> (e.g., eth0). If not specified, dnspoison should select a default interface to listen on. The sameinterface should be used for packet injection."
hostnameOptionHelp = "Read a list of IP address and hostname pairs specifying the hostnames to be hijacked. If '-f' is not specified, dnspoison should forge replies to all observed requests with the local machine's IP address as an answer."
helpMenuDescription = "Covert python dictionary of districts to the corresponding geojson"

i_option = False
f_option = False
whitelist = {}
forged_packet = None
qrname = ""


def is_valid_geojson(geojson_file, file_name):
    if geojson_file.get('type', None) != 'FeatureCollection':
        raise Exception('Sorry, "%s" does not look like GeoJSON' % file_name)
    if type(geojson_file.get('features', None)) != list:
        raise Exception('Sorry, "%s" does not look like GeoJSON' % file_name)


def main(args):
    args = parser.parse_args()
    print(args)
    infiles = args.infiles
    outfile = args.outfile
    dict_district_file = load(infiles[0])
    preprocessed_precincts_file = load(infiles[1])
    geojson_file = load(infiles[2])
    is_valid_geojson(geojson_file, infiles[2])


    outjson = dict(type='FeatureCollection', features=[])
    dict_district = dict_district_file['districts']
    preprocessed_precincts = dict_district_file['precincts']
    try:
        outjson['features'] += geojson_file['features']
    except:
        outjson['features'] += geojson_file

    print(dict_district)
    print(dict_district_file['districts'])
    for district in dict_district.key():
        dict_district[district]['precincts']
    newjsonfile = dumps(dict(precincts=outjson), indent=4)
    outfile.write(newjsonfile)
    # encoder = JSONEncoder(separators=(',', ':'))
    # encoded = encoder.iterencode(outjson)
    # format = '%.' + str(args.precision) + 'f'
    # output = outfile
    # for token in encoded:
    #     output.write(token)

    # encoder = JSONEncoder(separators=(',', ':'))
    # encoded = encoder.iterencode(outjson)

    # try:
    #     outjson['features'] += injson['features']
    # except:
    #     outjson['features'] += injson
    #
    # encoder = JSONEncoder(separators=(',', ':'))
    # encoded = encoder.iterencode(outjson)

    pass
    #print(args)
    #print(sys.argv)

def commandline():
    parser = argparse.ArgumentParser(prog="district-geojson", description=helpMenuDescription)
    parser.add_argument('infiles', help='district-dictionary.json precinct.json', type=argparse.FileType('r'), nargs=3,
                        default=sys.stdin)
    parser.add_argument('outfile',  help='name of output summary', nargs=1, type=str)
    # parser.add_argument('-i', help=interfaceOptionHelp, nargs = "?")
    # parser.add_argument('-f', help=hostnameOptionHelp, type=argparse.FileType('r'))
    return parser


def feature_struct():
    geometry_struct = dict(type='Polygon', coordinates=[])
    feature_struct = dict(type='Feature', geometry=geometry_struct, properties={})
    print(feature_struct)
    return feature_struct


def featurecollection_struct():
    output_json = dict(type='FeatureCollection', features=[])
    return output_json


def shared_coordinates(coordinate_one:list, coordinate_two:list):
    coordinates = []
    for coordinate in coordinate_one:
        if coordinate in coordinate_two:
            coordinates.append(coordinate)
    return coordinates


def remove_shared_coordinates(shared_coordinates:list, coordinates:list):
    for coordinate in shared_coordinates:
        while True:
            try:
                coordinates.remove(coordinate)
            except:
                break
    if coordinates[0] != coordinates[-1]:
        coordinates.append(coordinates[0])



def merge_coordinates(coordinate_one:list, coordinate_two:list):
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
    merged_coordinates.sort(key=sort_coordinate_by_x, reverse=True)
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
        # remove_shared_coordinates(share_coor,district_one_coordinates)
        # remove_shared_coordinates(share_coor,district_two_coordinates_)
        print(district_one_coordinates)

        print(district_one_coordinates)
        print(district_two_coordinates_.sort())
        merged_coor = merge_coordinates(district_one_coordinates, district_two_coordinates_)
        merged_coor = []
        for i in district_one_coordinates:
            if i not in share_coor:
                merged_coor.append(i)

        for i in district_two_coordinates_:
            if i not in share_coor:
                merged_coor.append(i)

        merged_coor.append(merged_coor[0])



        outjson = featurecollection_struct()
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



if __name__ == "__main__":
    # parser = commandline()
    # if len(sys.argv) <= 1:
    #     parser.print_help()
    # else:
    #     main(parser)
    test()