package com.ryccoatika.socketserver.models;

public class Client {
    private final String hostAddress;
    private final int port;
    private final int localPort;

    public Client(String hostAddress, int port, int localPort) {
        this.hostAddress = hostAddress;
        this.port = port;
        this.localPort = localPort;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public int getPort() {
        return port;
    }

    public int getLocalPort() {
        return localPort;
    }
}
