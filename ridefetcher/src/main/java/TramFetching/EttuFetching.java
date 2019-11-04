package TramFetching;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EttuFetching {
    public static Map<String, String> getTram(String url, String[] tramNumber) {
        Map<String, String> response = new HashMap<String,String>();
        Document html = null;
        try {
            html = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            response.put("Error", "Connection");
            return response;
        }
        Element body = html.body();
        String[] tramNumbers = body.select("b").text().split(" ");
        String connectionTime = tramNumbers[0];
        tramNumbers = Arrays.copyOfRange(tramNumbers, 1, tramNumbers.length);
        Pattern minRegex = Pattern.compile("\\d+\\sмин");
        Matcher matcher = minRegex.matcher(body.toString());
        List<String> allTimes = new ArrayList<String>();
        while (matcher.find()) {
            allTimes.add(matcher.group());
        }
        if (tramNumbers.length > 0) {
            for (int i = 0; i < tramNumber.length; i++){
                for (int j = 0; j < tramNumbers.length; j++) {
                    if (tramNumbers[j].equals(tramNumber[i])) {
                        if (!response.containsKey(tramNumbers[j])) {
                            response.put(tramNumbers[j], allTimes.get(j).split(" ")[0]);
                        }
                    }
                }
            }
        }
        if (allTimes.size() != 0) {response.put("last", allTimes.get(tramNumbers.length - 1));}
        return response;
    }


    public static String getTramString(Map<String, String> response) {
        StringBuilder responseString = new StringBuilder();
        if (response.size() == 1) {
            if (response.get("Error") == "Connection") {
                responseString.append("Мы не можем подключиться к сайту трамвайно-троллейбусного управления :(");
                return responseString.toString();
            }
            responseString.append("Мы не знаем когда точно приедет ваш трамвай, но не раньше чем через "
                    + response.get("last"));
        } else if (response.size() > 1) {
            response.remove("last");
            for (String elem : response.keySet()) {
                responseString.append("Следующий " + elem + " трамвай приедет через "
                        + response.get(elem) + " минут.\n");
            }
        } else if (response.size() == 0) {
            responseString.append("Похоже что сейчас трамваи не ходят :(");
        }
        return responseString.toString();
    }
}
