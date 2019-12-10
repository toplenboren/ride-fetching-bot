package UserStateManagement;


import jodd.json.JsonParser;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;

public class UserStateManager {

    private JsonParser jsonParser = new JsonParser();
    private static Jedis jedis;

    {
        try {
            jedis = getConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static boolean userExists(Long userId) {
        return jedis.exists(String.valueOf(userId));
    }

    private static Jedis getConnection() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        return new Jedis(redisURI);
    }

    //private static Map<Long, String> memory = new HashMap<Long, String>();

    private static void registerNewUser(Long userId) {
        jedis.set(String.valueOf(userId), new UserState().serialize());
    }

    public static void updateUser(Long userId, UserState state) {
        if (userExists(userId)) {
            jedis.set(String.valueOf(userId), state.serialize());
        }
    }

    public static UserState getOrRegisterUserState(Long userId) {
        if (!userExists(userId)) {
            registerNewUser(userId);
        }
        return JsonParser.create().parse(jedis.get(String.valueOf(userId)), UserState.class);
    }
}
