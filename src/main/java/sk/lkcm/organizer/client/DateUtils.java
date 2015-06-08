package sk.lkcm.organizer.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.DateTimeFormatInfo;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateUtils {

    public static final int MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
    
    public static final DateTimeFormat TIME_FORMAT = DateTimeFormat
            .getFormat("HH:mm");
    public static final DateTimeFormat DATE_FORMAT = DateTimeFormat
            .getFormat("dd.MM.yyyy");
    public static final DateTimeFormat DAY_HEADER_FORMAT = DateTimeFormat
            .getFormat("d MMMM");
    public static final DateTimeFormat DAY_OF_WEEK_FORMAT = DateTimeFormat
            .getFormat("cccc");
    public static final DateTimeFormat MONTH_YEAR_FORMAT = DateTimeFormat
            .getFormat("LLLL yyyy");

    private int unused;
    
    public static Date zeroedTimeDate(Date date) {
        @SuppressWarnings("deprecation")
        long millis = ((date.getTime() - date.getTimezoneOffset() * 60 * 1000) / MILLIS_PER_DAY)
                * MILLIS_PER_DAY + date.getTimezoneOffset() * 60 * 1000;

        return new Date(millis);
    }

    public static boolean areSameDay(Date a, Date b){
        return CalendarUtil.isSameDate(a, b);
    }
    
    @SuppressWarnings("deprecation")
    public static Date getStartOfTheWeek(Date date){
        DateTimeFormatInfo dateInfo = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo();
        
        int daysOffset = date.getDay() -  dateInfo.firstDayOfTheWeek();
        if (daysOffset < 0)
            daysOffset += 7;
        
        long startMillis = date.getTime() - daysOffset * MILLIS_PER_DAY;
        startMillis = ((startMillis  - date.getTimezoneOffset()*60*1000) / MILLIS_PER_DAY)*MILLIS_PER_DAY 
                + date.getTimezoneOffset() *60*1000;
        Date first = new Date(startMillis);
        
        return first;
    }
    
    public static Date setToMidnight(Date date) {

        DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy");

        String dateS = dtf.format(date);

        return dtf.parse(dateS);
    }

    public static Date addHours(Date date, int hours) {
        return new Date(date.getTime() + hours * 60 * 60 * 1000);
    }
    
    public static int getWeekOfTheYear(Date date){
        @SuppressWarnings("deprecation")
        Date yearStart = new Date(date.getYear(), 0, 0);
        @SuppressWarnings("deprecation")
        long diff = date.getTime() - yearStart.getTime() + date.getTimezoneOffset() * 60 * 1000;
        long millisPerWeek = 7 * 24 * 60* 60 *1000; 

        int week = (int)(diff/millisPerWeek) +1;
        return week;
    }
    
    public static class StrictDateBoxFormat extends DateBox.DefaultFormat {

        public StrictDateBoxFormat(DateTimeFormat dateFormat) {
            super(dateFormat);
        }

        @Override
        public Date parse(DateBox dateBox, String dateText, boolean reportError) {

            Date date = null;
            try {
                if (!dateText.isEmpty()) {
                    date = getDateTimeFormat().parseStrict(dateText);
                }
            } catch (IllegalArgumentException iae) {
                if (reportError) {
                    dateBox.addStyleName("dateBoxFormatError");
                }
            }
            return date;
        }
    }

}
