package Bot;

import Fetching.EttuFetching;
import Fetching.EttuFetchingSetup;
import UserState.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class BotLogic {

    private static String setAddress(String type, String input, UserState state) throws Exception {
        int FILTERING_THRESHOLD = 2;
        if (EttuFetchingSetup.addressIsValid(input)) {
            if (type.equals("STARTING_POINT"))
                state.setStartPoint(input);
            else if (type.equals("FINISHING_POINT"))
                state.setFinishPoint(input);
            state.setState("SETUP");
            return "Успешно! Проверь свои настройки по /show";
        } else if (input.length() < FILTERING_THRESHOLD) {
            return "Напиши начало названия своей остановки — мы дадим тебе варианты выбора";
        } else {
            Set filteredAddresses = EttuFetchingSetup.getAllAddresses(input);
            if (filteredAddresses.isEmpty()) {
                return "Мы не знаем такой остановки, попробуй, может быть в нашей регистратуре она по-другому записана";
            }
            String beautyfiedAddressesSet = (String) filteredAddresses.stream().collect(Collectors.joining("\n"));
            return "Мы не знаем такой остановки. Попробуй один из этих вариантов: \n\n" + beautyfiedAddressesSet;
        }
    }

    static String processCommand(String input, UserState state) throws Exception {
        switch (state.getState()) {
            case "INIT": {
                switch (input) {
                    case "/help":
                        return BotUtility.getHelpMessage();
                    case "/fetch":
                        if (state.isReadyToFetch()) {
                            return EttuFetching.getTramString(EttuFetching.getTram(state.getTramRoutesAsStrings(),
                                    EttuFetching.getHtmlDoc(EttuFetchingSetup.getAddressURL(state.getStartPoint()))));
                        }
                        return "Эй, я не знаю о тебе совсем ничего! Попробуй установить нужные параметры /setup";
                    case "/demo":
                        return "Добраться от матмеха УрГУ до физкультуры в манеже: \n" +
                                EttuFetching.getTramString(EttuFetching.getTram(new String[]{"13", "15"},
                                        EttuFetching.getHtmlDoc("http://m.ettu.ru/station/3417")));
                    case "/setup":
                        state.setState("SETUP");
                        return BotLogic.processCommand("", state);
                    default:
                        return "Привет! \n " +
                                "\uD83E\uDDE0 /fetch для того, чтобы улететь \n " +
                                "\uD83D\uDE0E /demo для демо \n " +
                                "❓ /help для вызова помощи \n " +
                                "⚙ /setup для входа в режим настройки бота \n ";
                }
            }
            case "SETUP": {
                switch (input) {
                    case "/show":
                        return "Твои текущие настройки: \n " +
                                "\uD83C\uDD70 Остановка:" + state.getStartPoint() + "\n " +
//                                "\uD83C\uDD71 Точка Б:" + state.getFinishPoint() + "\n " +
                                "\uD83D\uDE8B Нужные трамваи: " + state.getReadableTramRoutes() + "\n " +
                                "";
                    case "/setStart":
                        state.setState("SET_START");
                        return BotLogic.processCommand("", state);
//                    case "/setFinish":
//                        state.setState("SET_FINISH");
//                        return BotLogic.processCommand("", state);
                    case "/setRoutes":
                        state.setState("SET_ROUTES");
                        return BotLogic.processCommand("", state);
                    case "/start":
                        state.setState("INIT");
                        return "\uD83D\uDC4C";
                    default:
                        return "Ты находишься в режиме настройки бота: \n " +
                                "\uD83C\uDD70 /setStart для установки пункта А \n " +
//                                "\uD83C\uDD71 /setFinish для установки пункта B \n " +
                                "\uD83D\uDE8B /setRoutes для установки нужных тебе трамвайных маршрутов \n " +
                                "\uD83D\uDE49 /show для просмотра текущих настроек \n " +
                                "⬅ /start для выхода из режима настройки \n ";
                }
            }
            case "SET_START": {
                switch (input) {
                    case "/start":
                        state.setState("INIT");
                        return "\uD83D\uDC4C";
                    case "":
                        return "Пожалуйста введи свой начальный адрес: \n " +
                                "⬅ /start для выхода из режима настройки \n ";
                    default:
                        return setAddress("STARTING_POINT", input, state);
                }
            }
            case "SET_FINISH": {
                switch (input) {
                    case "/start":
                        state.setState("INIT");
                        return "\uD83D\uDC4C";
                    case "":
                        return "Пожалуйста введи свой конечный адрес: \n " +
                                "⬅ /start для выхода из режима настройки \n ";
                    default:
                        return setAddress("FINISHING_POINT", input, state);
                }
            }
            case "SET_ROUTES": {
                switch (input) {
                    case "/start":
                        state.setState("INIT");
                        return "\uD83D\uDC4C";
                    case "":
                        return "Введи нужные тебе маршруты трамваев через пробел: \n " +
                                "⬅ /start для выхода из режима настройки \n ";
                    default:
                        int ROUTES_THRESHOLD = 1;
                        List<Integer> tramRoutes = new ArrayList<>();
                        if (input.length() < ROUTES_THRESHOLD) {
                            return "Мы не экстрасенсы, пожалуйста введи что-нибудь";
                        }
                        String[] inputProcessed = input.split("\\s+");
                        for (String userInputNumber : inputProcessed) {
                            try {
                                tramRoutes.add(Integer.parseInt(userInputNumber));
                            } catch (NumberFormatException e) {
                                return "Эй, мы так не договаривались — вводи только числа";
                            }
                        }
                        state.setTramRoutes(tramRoutes);
                        state.setState("SETUP");
                        return "Успешно! Проверь свои настройки по /show";
                }
            }
            default:
                state.setState("INIT");
                return "Что-то пошло не так..";
        }
    }
}
