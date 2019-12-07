package Fetching;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Set;
import java.util.stream.Collectors;


public class EttuFetchingSetup {

    private static JSONObject tramStationsArray;

    public EttuFetchingSetup() {
        try {
            JSONParser parser = new JSONParser();
            tramStationsArray = (JSONObject) parser.parse(new FileReader("src/main/resources/ettuTrams.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Boolean addressIsValid(String address) {
        return tramStationsArray.containsKey(address);
    }

    public static Set<? extends String> getAllAddressesByFilter(String filterString) {
        Set<String> filteredSet = (Set<String>) tramStationsArray.keySet()
                .stream()
                .filter(s -> ((String) s).toLowerCase()
                        .startsWith(filterString.toLowerCase()))
                .collect(Collectors.toSet());
        return filteredSet;
    }

    public static String getAddressURL(String address) throws Exception {
        if (!addressIsValid(address)) {
            throw new Exception("Bad argument in getAddressURL: " + address);
        }
        return (String) tramStationsArray.get(address);
    }
}
