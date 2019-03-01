'''
Created on 2019. febr. 28.

@author: aperger
'''

import socket

class Socket(object):
    '''
    TCP socket handler
    '''

    TIMEOUT_SECONDS = 5
    MESSAGE_SEPARATOR = "\n\t"
    MESSAGE_SEPARATOR_LEN = MESSAGE_SEPARATOR.__len__()


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
        
    def send(self, request):
        
        if (request.rfind(Socket.MESSAGE_SEPARATOR))<0:
            request += Socket.MESSAGE_SEPARATOR
        
        self.socket.sendall(request.encode("utf-8"))
        
        result = ""
        while True:
            data = self.socket.recv(4096)
            if (data == None):
                break
            msg = data.decode("utf-8")
            result += msg
            
            # check the end of th emessage
            endOfMsg = result[(-1*Socket.MESSAGE_SEPARATOR_LEN):];
            if endOfMsg == Socket.MESSAGE_SEPARATOR:
                result = result[:(-1*Socket.MESSAGE_SEPARATOR_LEN)]
                break                
        
        self.socket.close()
        return result
