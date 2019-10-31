package TramFetching;

import java.net.URL;
import java.util.regex.Pattern;
import java.io.File;
import kotlin.collections.*;
import kotlin.Array;


fun GetHTML(url: String): String {
    val urlConnection = URL(url).openConnection();
    val content = urlConnection.getInputStream().bufferedReader().readText();
    return content;
}

fun GetTramArrivalTime(tramNumer: Int): String {
    val html = GetHTML("http://m.ettu.ru/station/3417");
    val regex = Regex(pattern = """<b>.+</b>""");
    val reg = Regex(pattern = """\d+\hмин""");
    val a = reg.findAll(html);
    val b = (regex.findAll(html));
    val tramNumbers = (b.map { it.value }).toList();
    val tramTimes = (a.map { it.value }).toList();
    var tramArrival: String;
    val indexOfTram = tramNumbers.indexOf("<b>"+tramNumber+"</b>");
    if (tramTimes.size == 0) {
        tramArrival = "Видимо не стоит ждать трамвая.";
    } else if (indexOfTram > -1) {
        tramArrival = tramTimes[indexOfTram - 1];
    } else {
        tramArrival = "Трамвай приедет не раньше чем через " + tramTimes[tramTimes.lastIndex];
    }

    return tramArrival;
}

println(GetTramArrivalTime(7));
