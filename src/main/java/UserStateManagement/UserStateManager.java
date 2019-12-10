package UserStateManagement;


import jodd.json.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class UserStateManager {

    private JsonParser jsonParser = new JsonParser();

    public static void updateUser(Long userId, UserState state) {
        if (userExists(userId)) {
            memory.replace(userId, state.serialize());
        }
    }

    private static Map<Long, String> memory = new HashMap<Long, String>();

    private static void registerNewUser(Long userId) {
        memory.put(userId, new UserState().serialize());
    }

    public static UserState getOrRegisterUserState(Long userId) {
        if (!userExists(userId)) {
            registerNewUser(userId);
        }
        return JsonParser.create().parse(memory.get(userId), UserState.class);
    }

    public UserState getUserState(Long userId) throws Exception {
        if (userExists(userId)) {
            return jsonParser.parse(memory.get(userId), UserState.class);
        }
        throw new Exception("Bad key");
    }

    private static boolean userExists(Long userId) {
        return memory.containsKey(userId);
    }
}
