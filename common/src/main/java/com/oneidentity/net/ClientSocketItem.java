package com.oneidentity.net;

import java.net.Socket;

public class ClientSocketItem {
    
    private Socket socket;
    private Long timestamp;
    
    public ClientSocketItem(Socket socket, Long timestamp) {
        this.socket = socket;
        this.timestamp = timestamp;
    }

    public Socket getSocket() {
        return socket;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
