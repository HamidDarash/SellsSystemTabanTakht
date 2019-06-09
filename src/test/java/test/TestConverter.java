package test;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;
import java.text.ParseException;
import java.util.Date;
import org.junit.Test;
//import org.apache.log4j.Logger;

public class TestConverter {

//    private static Logger logger = Logger.getLogger(TestConverter.class);

    @Test
    public void convertChack() throws ParseException {
//      System.out.println(CalendarUtil.dateStringToDate("1398/03/18", Locale.ENGLISH));
        ULocale locale = new ULocale("fa_IR@calendar=persian");
        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy/MM/dd", locale);
        Date d = df.parse("1398/03/18");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy/mm/dd", ULocale.ENGLISH);
        System.out.println(df2.parse(df2.format(d)));
    }
}
