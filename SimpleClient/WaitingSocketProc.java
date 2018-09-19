package study.inno.simpleChat.SimpleClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class WaitingSocketProc extends Thread {
    private Socket socket = null;

    public WaitingSocketProc(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            String message = "";
            while ((message = serverInput.readLine()) != null) {
                System.out.println(message);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
