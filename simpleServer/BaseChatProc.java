package study.inno.simpleChat.simpleServer;

import javax.xml.parsers.ParserConfigurationException;
import java.net.Socket;

public class BaseChatProc extends Thread {
    protected final SimpleXmlMessageParser parser = new SimpleXmlMessageParser();
    protected Socket socket = null;
    protected String clientName = "";
    protected long sleepDuration = 20;
    protected boolean quit = false;

    public BaseChatProc(Socket socket) throws ParserConfigurationException {
        this.socket = socket;
    }

    public String getClientName() {
        return clientName;
    }
}
