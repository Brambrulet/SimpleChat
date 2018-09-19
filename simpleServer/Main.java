package study.inno.simpleChat.simpleServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ServerChat serverChat = new ServerChat();

        try {
            serverChat.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
