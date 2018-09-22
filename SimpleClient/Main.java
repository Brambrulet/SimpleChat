package study.inno.simpleChat.SimpleClient;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        //Какого либо управления чатом не предполагалось
        //поэтому стартуем его прямо в этом потоке
        try {
            ClientChat chat = new ClientChat();
            chat.run();
//            chat.start();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
