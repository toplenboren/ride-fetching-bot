package Bot;

import TramFetching.TramFetching;
import UserState.UserState;

class BotLogic {
    static String processCommand(String input, UserState state) {
        switch (state.getState()) {
            case "INIT": {
                switch (input) {
                    case "/help":
                        return BotUtility.getHelpMessage();
                    case "/fetch":
                        return "Следующий трамвай приедет на \"Оперный\" через " +
                                TramFetching.getTram() +
                                " минут";
                    default:
                        return "Я ничего не понимаю, пожлауйтса вызови помощь на /help";
                }
            }
            default:
                return "Что-то пошло не так..";
        }
    }
}
