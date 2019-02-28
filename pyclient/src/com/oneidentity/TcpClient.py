'''
Created on 2019. febr. 28.

@author: aperger
'''

import select
import socket
from time import sleep


class Socket(object):
    '''
    TCP socket handler
    '''

    TIMEOUT_SECONDS = 5

    def __init__(self, host, port):
        '''
        Constructor
        '''
        self.host = host
        self.port = int(port)
        self.socket = None
        
        
    def connect(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        self.socket.connect((self.host, self.port))
        self.socket.settimeout(self.TIMEOUT_SECONDS)
        self.socket.setblocking(0)
        
    def send(self, request):
        
        
        ready = select.select([self.socket], [], [],self.TIMEOUT_SECONDS)
        self.socket.sendall(request)
        while (not ready[0]):
            print("Sleep")
            sleep(1)
            
        # if ready[0]:
        data = self.socket.recv(4096)
        
        self.socket.close()
        return repr(data)
