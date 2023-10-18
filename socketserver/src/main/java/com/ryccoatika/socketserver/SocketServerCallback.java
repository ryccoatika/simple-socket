package com.ryccoatika.socketserver;

import androidx.annotation.NonNull;

import com.ryccoatika.socketserver.models.Client;

public interface SocketServerCallback {
    void onClientConnected(@NonNull Client client);

    void onClientDisconnected(@NonNull Client client);

    void onMessageReceived(@NonNull Client client, @NonNull String message);
}
