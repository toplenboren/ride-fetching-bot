package UserState;

import java.util.HashMap;
import java.util.Map;

public class UserStateManager {
    private Map<Long, UserState> memory = new HashMap<Long, UserState>();

    private void registerNewUser(Long userId) {
        memory.put(userId, new UserState("INIT"));
    }

    public UserState findUserState(Long userId) {
        if (!userExists(userId)) {
            registerNewUser(userId);
        }
        return memory.get(userId);
    }

    //NOQA Not implemented
    public void changeUserState(Long userId, String state) {
        UserState user = findUserState(userId);
        user.setState(state);
    }

    private boolean userExists(Long userId) {
        return memory.containsKey(userId);
    }
}
