package study.inno.simpleChat.SimpleClient;

import org.xml.sax.SAXException;
import study.inno.simpleChat.simpleServer.BaseChatProc;
import study.inno.simpleChat.simpleServer.ServerChat;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat extends BaseChatProc {

    public ClientChat() throws IOException, ParserConfigurationException {
        super(new Socket("127.0.0.1", ServerChat.SERVER_PORT));
        System.out.println("simple chat started");
    }

    public void run() {
        try (BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));) {
            String message = "";

            while (!quit &&
                    !socket.isClosed()) {
                if (fromConsole.ready() &&
                        (message = fromConsole.readLine()) != null &&
                        (message = message.trim()).length() > 0) {
                    if (message.charAt(0) == ':') {
                        execCommand(message);
                    } else {
                        if (clientName.equals("")) {
                            clientName = message;
                            toServer.write("<name message=\"" + ServerChat.escapeXmlStr(message) + "\"/>");
                        } else {
                            toServer.write("<message message=\"" + ServerChat.escapeXmlStr(message) + "\"/>");
                        }
                        toServer.newLine();
                        toServer.flush();
                    }
                }

                if (fromServer.ready() &&
                        (message = fromServer.readLine()) != null &&
                        !(message = message.trim()).equals("")) {
                    try {
                        if (parser.parse(message)) {
                            switch (parser.tagName) {
                                case "message":
                                    System.out.println(parser.from + ": " + parser.message);
                                    break;

                                case "nameRequest":
                                    System.out.println(parser.from + ": представьтесь");
                                    break;

                                case "ping":
                                    toServer.write("<pong/>\r\n");
                                    toServer.flush();
                                    break;

                                default:
                                    break;
                            }
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }

                Thread.currentThread().sleep(sleepDuration);
            }
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void execCommand(String command) {
        switch (command.substring(1)) {
            case "quit":
                quit = true;
                break;
        }
    }
}
