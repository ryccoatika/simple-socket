package com.ryccoatika.socketserver;

import com.ryccoatika.socketserver.models.Client;
import com.ryccoatika.socketserver.utils.Mappers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class SocketServer {
    private final ServerSocket serverSocket;
    private SocketServerCallback socketServerCallback;
    volatile boolean keepProcessing = true;
    volatile HashMap<Client, Socket> connectedClients = new HashMap<>();

    public SocketServer() throws IOException {
        serverSocket = new ServerSocket();
    }

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Start server on new thread
     */
    public void startServer() {
        Runnable acceptClientHandler = () -> {
            while (keepProcessing) {
                try {
                    Socket socket = serverSocket.accept();
                    Client client = Mappers.map(socket);
                    connectedClients.put(client, socket);
                    if (Objects.nonNull(socketServerCallback)) {
                        socketServerCallback.onClientConnected(client);
                    }
                    handleIncomingMessageListener(client, socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread acceptClientThread = new Thread(acceptClientHandler);
        acceptClientThread.start();
    }

    public void stopServer() {
        keepProcessing = false;
        try {
            for (Socket socket : connectedClients.values()) {
                try {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSocketServerCallback(SocketServerCallback socketServerCallback) {
        this.socketServerCallback = socketServerCallback;
    }


    private void handleIncomingMessageListener(Client client, Socket socket) {
        Runnable listenMessageHandler = () -> {
            boolean keepProcessing = true;
            while (this.keepProcessing && keepProcessing) {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();
                    if (Objects.nonNull(socketServerCallback)) {
                        socketServerCallback.onMessageReceived(client, message);
                    }
                } catch (EOFException e) {
                    if (Objects.nonNull(socketServerCallback)) {
                        socketServerCallback.onClientDisconnected(client);
                    }
                    keepProcessing = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread listenMessageThread = new Thread(listenMessageHandler);
        listenMessageThread.start();
    }

    public void sendMessage(Client client, String message) {
        Runnable sendMessageHandler = () -> {
            try {
                Socket socket = connectedClients.get(client);
                assert socket != null;
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread sendMessageThread = new Thread(sendMessageHandler);
        sendMessageThread.start();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public String getHostAddress() {
        InetSocketAddress inetAddress = (InetSocketAddress) serverSocket.getLocalSocketAddress();
        return inetAddress.getAddress().getHostAddress();
    }
}
