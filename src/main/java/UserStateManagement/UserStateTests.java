package UserStateManagement;

import jodd.json.JsonParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserStateTests {

    @Test
    void userStateSerialization() {
        UserState testing = new UserState("INIT");
        testing.setStartPoint("ДК Лаврова (На кузнецова)");

        List<Integer> tramRoutes = new ArrayList<Integer>();
        tramRoutes.add(2);
        tramRoutes.add(28);

        testing.setTramRoutes(tramRoutes);
        String actual = testing.serialize();
        String expected =
                "{" +
                        "\"readyToFetch\":true," +
                        "\"startPoint\":\"ДК Лаврова (На кузнецова)\"" +
                        ",\"state\":\"INIT\"" +
                        ",\"tramRoutes\":[2,28]" +
                        "}";
        assertEquals(expected, actual);
    }

    @Test
    void objectSurvivesDeserialization() {
        UserState testing = new UserState();
        testing.setStartPoint("ДК Лаврова (На кузнецова)");

        List<Integer> tramRoutes = new ArrayList<Integer>();
        tramRoutes.add(2);
        tramRoutes.add(28);

        testing.setTramRoutes(tramRoutes);
        String serialized = testing.serialize();
        UserState deserialized = JsonParser.create().parse(serialized, UserState.class);
        boolean expected = deserialized.equals(testing);
        assertTrue(expected);
    }
}
