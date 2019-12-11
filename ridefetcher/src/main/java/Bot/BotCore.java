package Bot;

import Fetching.EttuFetchingSetup;
import UserState.UserState;
import UserState.UserStateManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;


public class BotCore extends TelegramLongPollingBot {

    private UserStateManager userStates = new UserStateManager();
    private EttuFetchingSetup databaseSync = new EttuFetchingSetup();

    public static void main(String[] args) {

        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "127.0.0.1");
        System.getProperties().put("socksProxyPort", "9150");

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
        return LocalSettings.botName;
    }

    @Override
    public void onUpdateReceived(Update e) {
        Message msg = e.getMessage();
        String txt = msg.getText();
        UserState user = userStates.findUserState(msg.getChatId());
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(msg.getChatId()).setText("");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/fetch");
        row.add("/demo");
        row.add("/setup");
        row.add("/help");
        keyboard.add(row);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message.setChatId(msg.getChatId()).setText(BotLogic.processCommand(txt, user)));
        } catch (TelegramApiException ec) {
            ec.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return LocalSettings.botToken;
    }

}