package Bot;

import TramFetching.EttuFetching;
import UserState.UserState;

class BotLogic {
    static String processCommand(String input, UserState state) {
        switch (state.getState()) {
            case "INIT": {
                switch (input) {
                    case "/help":
                        return BotUtility.getHelpMessage();
                    case "/fetch":
                        return EttuFetching.getTramString(EttuFetching.getTram(new String[] {"13", "15"},
                                EttuFetching.getHtmlDoc("http://m.ettu.ru/station/3417")));
                    case "/setup":
                        state.setState("SETUP");
                        return BotLogic.processCommand("", state);
                    default:
                        return "Привет! \n " +
                                "❓ /help для вызова помощи \n " +
                                "⚙ /setup для входа в режим настройки бота \n ";
                }
            }
            case "SETUP": {
                switch (input) {
                    case "/show":
                        return "Твои текущие настройки: \n " +
                                "\uD83C\uDD70 Точка А:" + state.getStartPoint() + "\n " +
                                "\uD83C\uDD71 Точка Б:" + state.getFinishPoint() + "\n " +
                                "\uD83D\uDE8B Нужные трамваи: " + state.getReadableTramRoutes() + "\n " +
                                "";
                    case "/setStart":
                        return "Еще в разработке";
                    case "/setFinish":
                        return "Еще в разработке";
                    case "/setRoutes":
                        return "Еще в разработке";
                    case "/start":
                        state.setState("INIT");
                        return "\uD83D\uDC4C";
                    default:
                        return "Ты находишься в режиме настройки бота: \n " +
                                "\uD83C\uDD70 /setStart для установки пункта А \n " +
                                "\uD83C\uDD71 /setFinish для установки пункта B \n " +
                                "\uD83D\uDE8B /setRoutes для установки нужных тебе трамвайных маршрутов \n " +
                                "\uD83D\uDE49 /show для просмотра текущих настроек \n " +
                                "⬅ /start для выхода из режима настройки \n ";
                }
            }
            default:
                return "Что-то пошло не так..";
        }
    }
}
