package study.inno.simpleChat.simpleServer;

public class Main {
    public static void main(String[] args) {
        ServerChat serverChat = new ServerChat();

        //Какое либо управление сервером не предполагалось
        //поэтому запускаем прямо в основном потоке
        serverChat.run();
    }
}
