package com.ryccoatika.socketclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.Objects;

public class SocketClient {
    private Socket socket;
    private final String host;
    private final int port;

    volatile boolean keepProcessing = true;

    private SocketClientCallback socketClientCallback;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        Runnable connectHandler = () -> {
            try {
                socket = new Socket(host, port);

                handleIncomingMessageListener(socket);
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

    private void handleIncomingMessageListener(Socket socket) {
        Runnable listenMessageHandler = () -> {
            boolean keepProcessing = true;
            while (this.keepProcessing && keepProcessing) {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();
                    if (Objects.nonNull(socketClientCallback)) {
                        socketClientCallback.onMessageReceived(message);
                    }
                } catch (EOFException e) {
                    if (Objects.nonNull(socketClientCallback)) {
                        socketClientCallback.onConnectionFailure(e);
                    }
                    keepProcessing = false;
                } catch (Exception e) {
                    socketClientCallback.onConnectionFailure(e);
                    e.printStackTrace();
                }
            }
        };
        Thread listenMessageThread = new Thread(listenMessageHandler);
        listenMessageThread.start();
    }

    public void setSocketClientCallback(SocketClientCallback socketClientCallback) {
        this.socketClientCallback = socketClientCallback;
    }
}
