package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.newProductionOrder.newProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class DateNormalizerService {
    private static final Logger LOGGER = Logger.getLogger(DateNormalizerService.class.getName());

    String datePattern = "yyyy-MM-dd";
    SimpleDateFormat format = new SimpleDateFormat(datePattern);
    String today = format.format(new Date());

    private String formatDate(String inputDate) {

        Pattern datePattern_1 = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat(datePattern);

        Pattern datePattern_2 = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");
        String pattern_2 = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat_2 = new SimpleDateFormat(pattern_2);

        Pattern datePattern_3 = Pattern.compile("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");
        String pattern_3 = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat_3 = new SimpleDateFormat(pattern_3);

        Pattern datePattern_4 = Pattern.compile("[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}");
        String pattern_4 = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat_4 = new SimpleDateFormat(pattern_4);

        Pattern datePattern_5 = Pattern.compile("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");
        String pattern_5 = "dd,MM,yyyy";
        SimpleDateFormat simpleDateFormat_5 = new SimpleDateFormat(pattern_5);

        if (datePattern_1.matcher(inputDate).matches()) {
            //System.out.println("Match_1");
            return printMatchedDate(simpleDateFormat_1, inputDate);
        } else if (datePattern_2.matcher(inputDate).matches()) {
            //System.out.println("Match_2");
            return printMatchedDate(simpleDateFormat_2, inputDate);
        } else if (datePattern_3.matcher(inputDate).matches()) {
            //System.out.println("Match_3");
            return printMatchedDate(simpleDateFormat_3, inputDate);
        } else if (datePattern_4.matcher(inputDate).matches()) {
            //System.out.println("Match_4");
            return printMatchedDate(simpleDateFormat_4, inputDate);
        } else if (datePattern_5.matcher(inputDate).matches()) {
            //System.out.println("Match_5");
            return printMatchedDate(simpleDateFormat_5, inputDate);
        }

        return " ";
    }

    private String printMatchedDate(SimpleDateFormat simpleDateFormat, String inputDate) {
        Date dateExample = null;
        try {
            dateExample = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE,
                    "Something is wrong with matchers in DateNormalizerService");
        }
        if (dateExample != null) {
            return normalizeDate(dateExample);
        }
        return " ";
    }

    private String normalizeDate(Date inputDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(inputDate);
    }

    public String getNormalizedDate(String inputDate) {

        return formatDate(inputDate);
    }

    public String delayCalcFromToday(String date, ProductionOrder.Readiness readiness) {
        if (readiness != ProductionOrder.Readiness.Wysłane) {
            if (date.equals(" ") || date.equals("harmonogram") || date.equals("")) {
                //System.out.println("Can't calculate: " + date+ ". Not a date");
                return " ";
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                Date deadLineDateParsed = null;
                Date todayParsed = null;
                try {
                    todayParsed = sdf.parse(today);
                    deadLineDateParsed = sdf.parse(date);
                } catch (ParseException e) {
                    System.out.println("Can't parse: " + date);
                }
                if (deadLineDateParsed == null) {
                    return " ";
                } else {
                    long diffInMillis = Math.subtractExact(deadLineDateParsed.getTime(), todayParsed.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

                    return Long.toString(diff);
                }
            }
        }else return " ";
    }

    public String calcDelayFromToday(String date, newProductionOrder.OrderStatus orderStatus) {
        if (orderStatus != newProductionOrder.OrderStatus.Wysłane) {
            if (date.equals(" ") || date.equals("harmonogram") || date.equals("")) {
                //System.out.println("Can't calculate: " + date+ ". Not a date");
                return " ";
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                Date deadLineDateParsed = null;
                Date todayParsed = null;
                try {
                    todayParsed = sdf.parse(today);
                    deadLineDateParsed = sdf.parse(date);
                } catch (ParseException e) {
                    System.out.println("Can't parse: " + date);
                }
                if (deadLineDateParsed == null) {
                    return " ";
                } else {
                    long diffInMillis = Math.subtractExact(deadLineDateParsed.getTime(), todayParsed.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

                    return Long.toString(diff);
                }
            }
        }else return " ";
    }

}
