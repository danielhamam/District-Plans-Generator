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


def test():
    file = open('testdistricts.json', 'r')
    geojson_file = load(file)
    outjson = dict()

    features_list = geojson_file['features']
    district_one_geojson = features_list[0]
    district_two_geojson = features_list[1]
    coor_one = district_one_geojson['geometry']['coordinates']
    coor_two = district_two_geojson['geometry']['coordinates']
    maxx = max(len(coor_one[0]), len(coor_two[0]))
    trash = []
    print(maxx, coor_two[0])
    if maxx == len(coor_one):
        for i in coor_one[0]:
            if i in coor_two[0]:
                trash.append(i)
                # coor_one.remove(i)
                # coor_two.remove(i)
    else:
        for i in coor_two[0]:
            if i in coor_one[0]:
                trash.append(i)
                # coor_one.remove(i)
                # coor_two.remove(i)
        pass
    print(trash)


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