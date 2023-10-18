package com.ryccoatika.socketserver;

import androidx.annotation.NonNull;

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

public class SocketServer {
    private final ServerSocket serverSocket;
    @NonNull
    private final SocketServerCallback socketServerCallback;
    volatile boolean keepProcessing = true;
    volatile HashMap<Client, Socket> connectedClients = new HashMap<>();

    public SocketServer(@NonNull SocketServerCallback socketServerCallback) throws IOException {
        serverSocket = new ServerSocket();
        this.socketServerCallback = socketServerCallback;
    }

    public SocketServer(int port, @NonNull SocketServerCallback socketServerCallback) throws IOException {
        serverSocket = new ServerSocket(port);
        this.socketServerCallback = socketServerCallback;
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
                    socketServerCallback.onClientConnected(client);
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
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleIncomingMessageListener(Client client, Socket socket) {
        Runnable listenMessageHandler = () -> {
            boolean keepProcessing = true;
            while (this.keepProcessing && keepProcessing) {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();
                    socketServerCallback.onMessageReceived(client, message);
                } catch (EOFException e) {
                    socketServerCallback.onClientDisconnected(client);
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

/*
* class SocketServer(port: Int = 0) {
    private var serverSocket: ServerSocket = ServerSocket(port)
    @Volatile
    private var keepProcessing = true

    private fun startServer(

    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    try {
                        val socket = serverSocket.accept()
                        listenMessage(socket)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    delay(2000)
                }
            }
        }
    }

    private fun stopServer() {

    }

    private fun listenMessage(socket: Socket) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = DataInputStream(socket.getInputStream())
                    while (true) {
                        if (inputStream.available() > 0) {
                            incomingMessages.value = Message(
                                hostAddress = socket.inetAddress.hostAddress ?: "",
                                message = inputStream.readUTF(),
                            )
                            delay(2000)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val messages: StateFlow<Message?> = incomingMessages.asStateFlow()

    val address: String?
        get() = serverSocket.inetAddress.hostAddress

    val port: Int
        get() = serverSocket.localPort

    fun shutdownServer() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                serverSocket.close()
            }
        }
    }
}
* */