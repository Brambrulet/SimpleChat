package study.inno.simpleChat.simpleServer;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ListenProc extends BaseChatProc {
    private static int pingPeriod = 200;
    private ServerChat server = null;
    private long lastActivity = 0L;

    public ListenProc(ServerChat server, Socket socket) throws ParserConfigurationException {
        super(socket);
        this.server = server;
    }

    @Override
    public void run() {
        long cuttentTime;
        lastActivity = System.currentTimeMillis();
        try (BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
            String message = "";
            while (!quit &&
                    !interrupted() &&
                    !socket.isClosed()) {

                if (fromClient.ready() &&
                        (message = fromClient.readLine()) != null &&
                        (message = message.trim()).length() > 0) {
                    lastActivity = System.currentTimeMillis();

                    try {
                        if (parser.parse(message)) {
                            switch (parser.tagName) {
                                case "message":
                                    if (!clientName.isEmpty()) {
                                        System.out.println(parser.from + ": " + parser.message);
                                        server.addMessage(parser.message, clientName);
                                    }
                                    break;

                                case "name":
                                    clientName = parser.message;
                                    System.out.println(clientName + " connected to chat");
                                    server.addMessage(clientName + " connected to chat", "");
                                    break;

                                case "pong":
                                default:
                                    break;
                            }
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }

                if ((message = server.pollMessage(this)) != null &&
                        message.length() > 0) {
                    toClient.write(message);
                    toClient.newLine();
                    toClient.flush();
                    lastActivity = System.currentTimeMillis();
                }

                //Проверка состояния соединения.
                //Если клиент отвалился, получим исключение.
                cuttentTime = System.currentTimeMillis();
                if (pingPeriod < (cuttentTime - lastActivity)) {
                    lastActivity = cuttentTime;
                    toClient.write("<ping/>\r\n");
                    toClient.flush();
                }

                sleep(sleepDuration);
            }
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        server.onDisconnect(this);
    }
}
