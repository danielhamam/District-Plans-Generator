from json import load, dumps
import argparse


def parser():
    parser = argparse.ArgumentParser(prog="Merge files from the output", description='Algorithm')
    parser.add_argument('directory', help='directory where files are located', type=str, nargs=1)
    return parser


def main(args):
    directory_path = args.directory[0]
    #TODO: CREATE




if __name__ == "__main__":
    parse = parser()
    main(parse.parse_args())