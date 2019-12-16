package Bot;

import Fetching.EttuFetching;
import Fetching.EttuFetchingSetup;
import UserStateManagement.UserState;
import UserStateManagement.UserStateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class BotLogic {

    /**
     * Handles address set call by /setStart or /setFinish (would be added in the
     * future updates). Returns a string — an answer to the user
     *
     * @param type  "STARTING_POINT" made to implement and handle
     *              the finishing point functionality in the later stages of development
     * @param input user's input
     * @param state the current user's state
     * @return an answer to the user
     */
    private static String handleAddressSet(String type, String input, UserState state, Long userId) throws Exception {
        int FILTERING_THRESHOLD = 1;
        if (EttuFetchingSetup.addressIsValid(input)) {
            if (type.equals("STARTING_POINT"))
                state.setStartPoint(input);
            state.setState("INIT");
            UserStateManager.updateUser(userId, state);
            return "Успешно! Проверь свои настройки по /show";
        } else if (input.length() < FILTERING_THRESHOLD) {
            return "Напиши начало названия своей остановки — мы дадим тебе варианты выбора";
        } else {
            Set filteredAddresses = EttuFetchingSetup.getAllAddressesByFilter(input);
            if (filteredAddresses.isEmpty()) {
                return "Мы не знаем такой остановки, попробуй, может быть в нашей регистратуре она записана по-другому";
            }
            String beautyfiedAddressesSet = (String) filteredAddresses.stream().collect(Collectors.joining("\n"));
            return "Мы не знаем такой остановки. Попробуй один из этих вариантов: \n\n" + beautyfiedAddressesSet;
        }
    }

    /**
     * Handles a command processing and returns appropriate string on user's state & input
     *
     * @param  input user's input
     * @param userId the current user's ID
     * @return an answer to the user
     */
    static String processCommand(String input, Long userId) throws Exception {

        UserState state = UserStateManager.getOrRegisterUserState(userId);

        switch (state.getState()) {
            case "INIT": {
                switch (input) {
                    case "/help":
                        return "Привет! \n\n" +
                                "Я подскажу тебе, когда приедет твой трамвай! \n\n" +
                                "Пожалуйста укажи трамвайную остановку, с которой ты часто куда-то уезжаешь (в Университет, школу или на работу) /setStart и нужные тебе номера трамваев /setRoutes \n\n" +
                                " Теперь просто используй /fetch :) \n\n" +
                                "Репозиторий: https://github.com/toplenboren/ride-fetching-bot";
                    case "/fetch":
                        if (state.isReadyToFetch()) {
                            return EttuFetching.getTramString(EttuFetching.getTram(state.viewTramRoutesAsStrings(),
                                    EttuFetching.getHtmlDoc(EttuFetchingSetup.getAddressURL(state.getStartPoint()))));
                        }
                        return "Эй, я не знаю о тебе совсем ничего! Попробуй установить нужные параметры /setState /setRoutes";
                    case "/demo":
                        return "Добраться от матмеха УрГУ до физкультуры в манеже: \n \n" +
                                EttuFetching.getTramString(EttuFetching.getTram(new String[]{"13", "15"},
                                        EttuFetching.getHtmlDoc("http://m.ettu.ru/station/3417")));
                    case "/show":
                        return "Твои текущие настройки: \n " +
                                "\uD83C\uDD70 Остановка: " + state.getStartPoint() + "\n " +
                                "\uD83D\uDE8B Нужные трамваи: " + state.viewReadableTramRoutes() + "\n " +
                                "";
                    case "/setStart":
                        state.setState("SET_START");
                        UserStateManager.updateUser(userId, state);
                        return BotLogic.processCommand("", userId);
                    case "/setRoutes":
                        state.setState("SET_ROUTES");
                        UserStateManager.updateUser(userId, state);
                        return BotLogic.processCommand("", userId);
                    default:
                        return "Привет! \n " +
                                "\uD83E\uDDE0 /fetch для того, чтобы улететь \n " +
                                "\uD83D\uDE0E /demo для демо \n " +
                                "❓ /help для вызова помощи \n " +
                                "\n" +
                                "\uD83C\uDD70 /setStart для установки пункта А \n " +
                                "\uD83D\uDE8B /setRoutes для установки нужных тебе трамвайных маршрутов \n " +
                                "\uD83D\uDE49 /show для просмотра текущих настроек \n " +
                                state.getState();
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
                        return handleAddressSet("STARTING_POINT", input, state, userId);
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
                        return handleAddressSet("FINISHING_POINT", input, state, userId);
                }
            }
            case "SET_ROUTES": {
                switch (input) {
                    case "/start":
                        state.setState("INIT");
                        UserStateManager.updateUser(userId, state);
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
                        state.setState("INIT");
                        UserStateManager.updateUser(userId, state);
                        return "Успешно! Проверь свои настройки по /show";
                }
            }
            default:
                state.setState("INIT");
                UserStateManager.updateUser(userId, state);
                return "Что-то пошло не так.. ";
        }
    }
}
