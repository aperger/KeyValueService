#!/usr/local/bin/python3.7
# encoding: utf-8
'''
com.oneidentity.Client -- client application for Key-Value server

com.oneidentity.Client is a description

It defines classes_and_methods

@author:     Attila Perger

@copyright:  2019 Attila Perger. All rights reserved.

@license:    license

@contact:    perger.attila.ps@gmail.com
@deffield    updated: Updated
'''

import sys
import os

from argparse import ArgumentParser
from argparse import RawDescriptionHelpFormatter
from builtins import Exception
from com.oneidentity.TcpClient import Socket

__all__ = []
__version__ = 0.1
__date__ = '2019-02-27'
__updated__ = '2019-02-27'

DEBUG = 1
TESTRUN = 0
PROFILE = 0
MSG_LENGTH = 1;

class CLIError(Exception):
    '''Generic exception to raise and log different fatal errors.'''
    def __init__(self, msg):
        super(CLIError).__init__(type(self))
        self.msg = "E: %s" % msg
    def __str__(self):
        return self.msg
    def __unicode__(self):
        return self.msg

def startProgram(host, port):
    
    print("Connecting to server '{}' on port :{}".format(host, port))
    socket = Socket(host, port)


    readNext = True
    while readNext:
        
        try:
            socket.connect()
        except Exception as e:
            sys.stderr.write("Error: " + str(e))
            sys.exit(1)
            
        request = input("> ")
        if (request == '' or request == '\quit'):
            readNext = False
        else:
            response = socket.send(request)
            print(response)


def main(argv=None): # IGNORE:C0111
    '''Command line options.'''

    if argv is None:
        argv = sys.argv
    else:
        sys.argv.extend(argv)

    program_name = os.path.basename(sys.argv[0])
    program_version = "v%s" % __version__
    program_build_date = str(__updated__)
    program_version_message = '%%(prog)s %s (%s)' % (program_version, program_build_date)
    program_shortdesc = __import__('__main__').__doc__.split("\n")[1]
    program_license = '''%s

  Created by Attila Perger on %s.
  Copyright 2019 Attila Perger. All rights reserved.
  Version: %s

  Licensed under the Apache License 2.0
  http://www.apache.org/licenses/LICENSE-2.0

  Distributed on an "AS IS" basis without warranties
  or conditions of any kind, either express or implied.

USAGE
''' % (program_shortdesc, str(__date__), program_version_message)

    try:
        # Setup argument parser
        parser = ArgumentParser(description=program_license, formatter_class=RawDescriptionHelpFormatter, )
        # parser.add_argument("-r", "--recursive", dest="recurse", action="store_true", help="recurse into subfolders [default: %(default)s]")
        # parser.add_argument("-v", "--verbose", dest="verbose", action="count", help="set verbosity level [default: %(default)s]")
        # parser.add_argument("-i", "--include", dest="include", help="only include paths matching this regex pattern. Note: exclude is given preference over include. [default: %(default)s]", metavar="RE" )
        # parser.add_argument("-e", "--exclude", dest="exclude", help="exclude paths matching this regex pattern. [default: %(default)s]", metavar="RE" )
        # parser.add_argument('-V', '--version', action='version', version=program_version_message)
        # parser.add_argument(dest="paths", help="paths to folder(s) with source file(s) [default: %(default)s]", metavar="path", nargs='+')
        parser.add_argument(dest="host", help="IP or hostname of the server", metavar="host")
        parser.add_argument(dest="port", help="Service/port number of the server", metavar="port")

        # Process arguments
        args = parser.parse_args()

        host = args.host
        port = int(args.port)
        startProgram(host, port)
        
        return 0
    except KeyboardInterrupt:
        ### handle keyboard interrupt ###
        return 0
    except Exception as e:
        if DEBUG or TESTRUN:
            raise(e)
        indent = len(program_name) * " "
        sys.stderr.write(program_name + ": " + repr(e) + "\n")
        sys.stderr.write(indent + "  for help use --help")
        return 2

if __name__ == "__main__":
    if DEBUG:
        pass
    if TESTRUN:
        import doctest
        doctest.testmod()
    if PROFILE:
        import cProfile
        import pstats
        profile_filename = 'com.oneidentity.Client_profile.txt'
        cProfile.run('main()', profile_filename)
        statsfile = open("profile_stats.txt", "wb")
        p = pstats.Stats(profile_filename, stream=statsfile)
        stats = p.strip_dirs().sort_stats('cumulative')
        stats.print_stats()
        statsfile.close()
        sys.exit(0)
    sys.exit(main())