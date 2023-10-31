package com.ryccoatika.socketclient;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Objects;

public class SocketClient {
    private Socket socket;
    private final String host;
    private final int port;
    private SocketClientCallback socketClientCallback;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        Runnable connectHandler = () -> {
            try {
                socket = new Socket(host, port);
                if (Objects.nonNull(socketClientCallback)) {
                    socketClientCallback.onConnected();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (Objects.nonNull(socketClientCallback)) {
                    socketClientCallback.onConnectionFailure(e);
                }
            }
        };
        Thread connectThread = new Thread(connectHandler);
        connectThread.start();
    }

    public void disconnect() {
        try {
            socket.close();
            if (Objects.nonNull(socketClientCallback)) {
                socketClientCallback.onDisconnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        Runnable sendMessageHandler = () -> {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                if (Objects.nonNull(socketClientCallback)) {
                    socketClientCallback.onConnectionFailure(e);
                }
            }
        };
        Thread sendMessageThread = new Thread(sendMessageHandler);
        sendMessageThread.start();
    }

    public void setSocketClientCallback(SocketClientCallback socketClientCallback) {
        this.socketClientCallback = socketClientCallback;
    }
}
