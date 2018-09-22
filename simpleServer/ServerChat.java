package study.inno.simpleChat.simpleServer;

//import study.inno.simpleChat.SimpleClient.ClientChat;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class ServerChat extends Thread {
    public final static Integer SERVER_PORT = 5999;
    private ServerSocket socket = null;
    private HashMap<ListenProc, LinkedList<String>> messagesQueue = new HashMap<>();
    private String newMessage = null;
    private boolean quit = false;

    public static String unescapeXmlStr(String src) {
        return src.replaceAll("&quot;", "\"").
                replaceAll("&apos;", "\'").
                replaceAll("&lt;", "<").
                replaceAll("&gt;", ">").
                replaceAll("&amp;", "&").trim();
    }

    public static String escapeXmlStr(String src) {
        return src.replaceAll("&", "&amp;").
                replaceAll("\"", "&quot;").
                replaceAll("\'", "&apos;").
                replaceAll("<", "&lt;").
                replaceAll(">", "&gt;").
                trim();
    }

    public void run() {
        try {
            if (socket == null &&
                    (socket = new ServerSocket(SERVER_PORT)) != null) {
                System.out.println("server started");

                while (!quit && !socket.isClosed()) {
                    addClient(socket.accept());
                }

                if (!socket.isClosed()) {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void addClient(Socket socket) throws ParserConfigurationException {
        ListenProc client = new ListenProc(this, socket);

        synchronized (messagesQueue) {
            messagesQueue.put(client, new LinkedList<>());
        }

        requestName(client);
        client.start();
    }

    void addMessage(String message, String from) {
        if (from == null || (from = from.trim()).equals("")) {
            from = "Server";
        }

        message = "<message message=\"" + escapeXmlStr(message) + "\" from=\"" + escapeXmlStr(from) + "\"/>";
        synchronized (messagesQueue) {
            newMessage = message;
            messagesQueue.forEach((k, v) -> v.add(newMessage));
            newMessage = null;
        }
    }

    void onDisconnect(ListenProc client) {
        String clientName = null;
        synchronized (messagesQueue) {
            if (messagesQueue.containsKey(client)) {
                messagesQueue.remove(client);
                clientName = client.getClientName();
            }
        }

        if (clientName != null && !clientName.equals("")) {
            System.out.println(clientName + " disconnected");
            addMessage(clientName + " disconnected", "");
        } else {
            System.out.println("anonymous disconnected");
        }
    }

    String pollMessage(ListenProc client) {
        synchronized (messagesQueue) {
            if (messagesQueue.containsKey(client)) {
                return messagesQueue.get(client).poll();
            }
        }

        return "";
    }

    private void requestName(ListenProc client) {
        LinkedList<String> queue = messagesQueue.getOrDefault(client, null);

        if (queue != null) {
            queue.add("<nameRequest from=\"Server\"/>");
        }
    }

    public void quit() {
        quit = true;

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
