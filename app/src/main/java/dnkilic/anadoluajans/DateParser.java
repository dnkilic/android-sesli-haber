package dnkilic.anadoluajans;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateParser {


    Date date;

    public String parse(String pubDate)  {



        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.UK);



        try {
            date = format.parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        String dayOfTheWeek = (String) DateFormat.format("EEE", date);
        String day          = (String) DateFormat.format("dd",   date);
        String monthString  = (String) DateFormat.format("MMM",  date);
        String year         = (String) DateFormat.format("yyyy", date);
        String hour         = (String) DateFormat.format("HH",date);
        String minute         = (String) DateFormat.format("mm",date);
        String second         = (String) DateFormat.format("ss",date);

/*
        String ay="";

        switch (monthString){
            case "Jan":
                ay="Ocak";
                break;
            case "Feb":
                ay="Şubat";
                break;
            case "Mar":
                ay="Mart";
                break;
            case "Apr":
                ay="Nisan";
                break;
            case "May":
                ay="Mayıs";
                break;
            case "Jun":
                ay="Haziran";
                break;
            case "July":
                ay="Temmuz";
                break;
            case "Aug":
                ay="Ağustos";
                break;
            case "Sep":
                ay="Eylül";
                break;
            case "Oct":
                ay="Ekim";
                break;
            case "Nov":
                ay="Kasım";
                break;
            case "Dec":
                ay="Aralık";
                break;
            default:
                ay="";
                break;
        }

        String gun="";

        switch (dayOfTheWeek){
            case "Mon":
                gun="Pazartesi";
                break;
            case "Tue":
                gun="Salı";
                break;
            case "Wed":
                gun="Çarşamba";
                break;
            case "Thu":
                gun="Perşembe";
                break;
            case "Fri":
                gun="Cuma";
                break;
            case "Sat":
                gun="Cumartesi";
                break;
            case "Sun":
                gun="Pazar";
                break;
            default:
                gun="";
                break;
        }
        */

        String tarih=day +" "+ monthString + " " + year + " , "+dayOfTheWeek+" , "+ hour +":"+minute+":"+second;


        return tarih;

    }
}
