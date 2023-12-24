Simple Socket
-------
[![Android CI](https://github.com/ryccoatika/simple-socket/actions/workflows/android.yml/badge.svg)](https://github.com/ryccoatika/simple-socket/actions/workflows/android.yml)
[![Release](https://img.shields.io/github/v/release/ryccoatika/simple-socket.svg?include_prereleases)](https://github.com/ryccoatika/simple-socket/releases)

<img src="art/video_sample.gif" title="Animated example."/>

---

Usage
-------

Don't worry about exception, This library has handle all of the exceptions and throw it through callback.

### Socket Server

Passing port to the constructor argument if you want to specify the port. 

```kotlin
val socketServer = SocketServer()
val port = socketServer.port
```

Set listener for socket server
```kotlin
val socketServerCallback = object : SocketServerCallback {
    override fun onClientConnected(client: Client) {
        // you may want to store all of the connected client
    }

    override fun onClientDisconnected(client: Client) {
        // when client disconnected
    }

    override fun onMessageReceived(client: Client, message: String) {
        // message received from client
    }
}
socketServer.setSocketServerCallback(socketServerCallback)
```

Start the server
```kotlin
socketServer.startServer()
```

### Socket Client

Create an object from class `SocketClient` with server host and port as arguments
```kotlin
val socketClient = SocketClient(host, port)
```

Set listener for socket client
```kotlin
val socketClientCallback = object : SocketClientCallback {
    override fun onConnected() {
        // when connected to the server
    }

    override fun onConnectionFailure(e: Exception) {
        // any failure occurs would be thrown here
    }

    override fun onDisconnected() {
        // called when client call disconnect() or server has gone
    }

    override fun onMessageReceived(message: String) {
        // message received from server
    }
}
socketClient.setSocketClientCallback(socketClientCallback)
```

Connect the client to server
```kotlin
socketClient.connect()
```

---

Sample apps
-------

I have built [sample app](https://github.com/ryccoatika/simple-socket/blob/main/sample-app) using jetpack compose.

---

Download
-------
All artifacts are up on Maven Central

For socket server library
```
com.ryccoatika.simplesocket:socket-server:<version>
```
For socket client library
```
com.ryccoatika.simplesocket:socket-client:<version>
```

latest stable version is [![Release](https://img.shields.io/github/v/release/ryccoatika/simple-socket.svg?include_prereleases)](https://github.com/ryccoatika/simple-socket/releases)

### Snapshots

Snapshots are also available by adding the repository
```kotlin
maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
```
Letting you specify
```
com.ryccoatika.simplesocket:socket-server:0.6.9-SNAPSHOT
com.ryccoatika.simplesocket:socket-client:0.6.9-SNAPSHOT
```

---

Contributing
-------
Pull requests are welcome.

TODO:
- Generate mkdocs
- Thread safe optimization (have to make sure for thread safe)

---

License
-------
    MIT License
    
    Copyright (c) 2023 Rycco Atika
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
