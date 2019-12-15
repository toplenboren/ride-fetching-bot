package Bot;

import Fetching.EttuFetchingSetup;
import UserStateManagement.UserStateManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotCore extends TelegramLongPollingBot {

    private UserStateManager userStates = new UserStateManager();
    private EttuFetchingSetup databaseSync = new EttuFetchingSetup();

    public static void main(String[] args) {

    // System.getenv returns string
    if(System.getenv("DEBUG").equals("True")) {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "127.0.0.1");
        System.getProperties().put("socksProxyPort", "9150");
    }

        ApiContextInitializer.init();

        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BotCore());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public void onUpdateReceived(Update e) {
        Message msg = e.getMessage();
        String txt = msg.getText();
        try {
            sendMsg(msg, BotLogic.processCommand(txt, msg.getChatId()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return  System.getenv("BOT_TOKEN");
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}