package com.ryccoatika.socketclient;

import androidx.annotation.NonNull;

public interface SocketClientCallback {
    void onConnected();

    void onConnectionFailure(@NonNull Exception e);

    void onDisconnected();
}
