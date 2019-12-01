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
                    default:
                        return "Я ничего не понимаю, пожлауйтса вызови помощь на /help";
                }
            }
            case "SETUP": {
                switch (input) {
                    case "/show":
                        return "Твои текущие настройки: \n" +
                                "Точка А" + state.getStartPoint() + "\n" +
                                "Точка Б" + state.getFinishPoint() + "\n" +
                                "Нужные трамваи: " + state.getReadableTramRoutes() + "\n" +
                                "";
                    case "/setStart":
                        return "Еще в разработке";
                    case "/setFinish":
                        return "Еще в разработке";
                    case "/setRoutes":
                        return "Еще в разработке";
                    default:
                        return "Ты находишься в режиме настройки бота: \n " +
                                "/setStart для установки пункта А \n " +
                                "/setFinish для установки пункта B \n" +
                                "/setRoutes для установки нужных тебе трамвайных маршрутов" +
                                "/show для просмотра текущих настроек";
                }
            }
            default:
                return "Что-то пошло не так..";
        }
    }
}
