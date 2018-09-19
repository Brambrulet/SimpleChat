package study.inno.simpleChat.SimpleClient;

import study.inno.simpleChat.simpleServer.ServerChat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientChat {
    private Socket socket = null;
    private WaitingSocketProc readProc = null;

    public ClientChat() {
        try {
            socket = new Socket("127.0.0.1", ServerChat.SERVER_PORT);
            start();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("не фурычит!");
    }

    public ClientChat(Socket socket) {
        this.socket = socket;
        start();
    }

    public void start() {
        readProc = new WaitingSocketProc(socket);
        readProc.start();

        try (BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
            System.out.println("welcome чатитца!");
            String message = "";
            Scanner scanner = new Scanner(System.in);
            do {
                if (!message.isEmpty()) {
                    serverOutput.write(message);
                    serverOutput.newLine();
                    serverOutput.flush();
                }

                message = scanner.nextLine();
            } while (!message.equals(""));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
