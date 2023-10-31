package com.ryccoatika.socketserver.utils;

import com.ryccoatika.socketserver.models.Client;

import java.net.Socket;

public abstract class Mappers {
    static public Client map(Socket socket) {
        return new Client(
                socket.getInetAddress().getHostAddress(),
                socket.getPort(),
                socket.getLocalPort()
        );
    }
}
