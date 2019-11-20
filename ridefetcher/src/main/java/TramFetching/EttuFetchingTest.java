package TramFetching;

import com.google.inject.internal.asm.$ClassTooLargeException;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import org.jsoup.nodes.Document;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EttuFetchingTest {

    static Document getDoc(String filename) {
        try {
            File file = new File("C:\\Users\\Serial_Flexer\\IdeaProjects\\mm-oop-task\\ridefetcher\\src\\test\\"
                    + filename);
            Document doc = Jsoup.parse(file, "UTF-8");
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return new Document("Error");
        }
    }

    @Test
    void parseZeroTrams() {
        Document doc = getDoc("0tram.txt");
        Map<String, String> actual = EttuFetching.getTram(new String[] {"13", "15"}, doc);
        Map<String,String> expected = new HashMap<String, String>(Map.of("last", "8"));
        assertEquals(expected, actual);
    }

    @Test
    void parseOneTram() {
        Document doc = getDoc("1tram.txt");
        Map<String, String> actual = EttuFetching.getTram(new String[] {"13", "15"}, doc);
        Map<String,String> expected = new HashMap<String, String>(Map.of("15", "3" ,"last", "8"));
        assertEquals(expected, actual);
    }
    @Test
    void parseTwoTrams() {
        Document doc = getDoc("2tram.txt");
        Map<String, String> actual = EttuFetching.getTram(new String[] {"13", "15"}, doc);
        Map<String,String> expected = new HashMap<String, String>(Map.of
                ("15", "3", "13", "0", "last", "8"));
        assertEquals(expected, actual);
    }

    @Test
    void parseMoreTrams() {
        Document doc = getDoc("moretram.txt");
        Map<String, String> actual = EttuFetching.getTram(new String[] {"13", "15"}, doc);
        Map<String,String> expected = new HashMap<String, String>(Map.of
                ("15", "3", "13", "0", "last", "8"));
        assertEquals(expected, actual);
    }

    @Test
    void parseNoTramsAtAll() {
        Document doc = getDoc("notram.txt");
        Map<String, String> actual = EttuFetching.getTram(new String[] {"13", "15"}, doc);
        Map<String,String> expected = new HashMap<String, String>(Map.of());
        assertEquals(expected, actual);
    }

    @Test
    void buildStringWithoutNeededTrams() {
        Document doc = getDoc("0tram.txt");
        String actual = EttuFetching.getTramString(EttuFetching.getTram(new String[] {"13", "15"}, doc));
        String expected = "Не понятно когда точно приедет твой трамвай, но не раньше чем через 8 минут.";
        assertEquals(expected, actual);
    }

    @Test
    void buildStringWithOneNeededTrams() {
        Document doc = getDoc("1tram.txt");
        String actual = EttuFetching.getTramString(EttuFetching.getTram(new String[] {"13", "15"}, doc));
        String expected = "Ближайшие трамваи, которые тебе нужны:\n    15 - через 3 минут.\n";
        assertEquals(expected, actual);
    }

    @Test
    void buildStringWithTwoNeededTrams() {
        Document doc = getDoc("2tram.txt");
        String actual = EttuFetching.getTramString(EttuFetching.getTram(new String[] {"13", "15"}, doc));
        String expected = "Ближайшие трамваи, которые тебе нужны:\n" +
                "    13 - через 0 минут.\n" +
                "    15 - через 3 минут.\n";
        assertEquals(expected, actual);
    }

//    @Test
//    void noConnection() {
//        Document actual = EttuFetching.getHtmlDoc("http://m.ettu.ru/station/3417");
//        Document expected = new Document("Error");
//        assertEquals(expected, actual);
//
//    }
}