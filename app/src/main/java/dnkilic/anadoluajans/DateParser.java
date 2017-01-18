package dnkilic.anadoluajans;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser {
    public static String parse(String pubDate)  {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.UK);

        try {
            date = format.parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date != null)
        {
            String dayOfTheWeek = (String) DateFormat.format("EEE", date);
            String day          = (String) DateFormat.format("dd",   date);
            String monthString  = (String) DateFormat.format("MMM",  date);
            String year         = (String) DateFormat.format("yyyy", date);
            String hour         = (String) DateFormat.format("HH",date);
            String minute       = (String) DateFormat.format("mm",date);
            String second       = (String) DateFormat.format("ss",date);

            return day + " " + monthString + " " + year + " , " + dayOfTheWeek + " , " + hour + ":" + minute + ":" + second;
        }
        else
        {
            return pubDate;
        }
    }
}
