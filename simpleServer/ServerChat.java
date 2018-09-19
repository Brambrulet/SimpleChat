package study.inno.simpleChat.simpleServer;

import study.inno.simpleChat.SimpleClient.ClientChat;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerChat {
    public final static Integer SERVER_PORT = 5999;
    private ServerSocket socket = null;

    public void start() throws Exception {
        System.out.println("server started");
        if (socket != null) throw new Exception("Server already running");
        else {
            socket = new ServerSocket(SERVER_PORT);
            while (!socket.isClosed()) {
                new ClientChat(socket.accept());
            }
        }
    }

    public void stop() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
