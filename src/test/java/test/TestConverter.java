package test;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;
import helper.DateConvertor;
import helper.JalaliCalendar;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.junit.Test;
//import org.apache.log4j.Logger;

public class TestConverter {

    private static Logger logger = Logger.getLogger(TestConverter.class);

    @Test
    public void convertChack() throws ParseException {
//      System.out.println(CalendarUtil.dateStringToDate("1398/03/18", Locale.ENGLISH));
        ULocale locale = new ULocale("fa_IR@calendar=persian");
        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy/MM/dd", locale);
        Date d = df.parse("1398/03/20");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy/mm/dd", ULocale.ENGLISH);
//        System.out.println(df2.parse(df2.format(d)));
        int[] d2 = DateConvertor.jalali_to_gregorian(1398, 03, 20);
        String dtStr = String.valueOf(d2[0]) + "-" + String.valueOf(d2[1]) + "-" + String.valueOf(d2[2]);
        System.out.println("Date is: " + dtStr);

    }

    private String ConvertStrToDate(String s) {
        String[] tempArray;
        String delimiter = "/";
        tempArray = s.split(delimiter);
        int[] temp = {};

        temp = DateConvertor.jalali_to_gregorian(Integer.valueOf(tempArray[0]), Integer.valueOf(tempArray[1]),
                Integer.valueOf(tempArray[2]));

        if (temp.length == 3) {
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] < 10) {
                    tempArray[i] = "0" + String.valueOf(temp[i]);
                } else {
                    tempArray[i] = String.valueOf(temp[i]);
                }
            }
        }
        if (tempArray.length == 3) {
            return tempArray[0] + "-" + tempArray[1] + "-" + tempArray[2];
        }
        return "No Detect ....";
    }

    @Test
    public void stringToDateFormatFunction() {
        String valueField;
        valueField = ConvertStrToDate("1398/03/18");
        System.out.println("--------------------------------");
        System.out.println("Date is: " + valueField);
    }
}
