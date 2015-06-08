package sk.lkcm.organizer.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sk.lkcm.organizer.client.DateUtils;
import sk.lkcm.organizer.client.Day;
import sk.lkcm.organizer.shared.EventDTO;

public class ControllerHelper {
    
    
    public static List<Day> buildDays(Date firstDay, int numberOfDays, List<EventDTO> eventDtos){
        Map<Date,Day> days = new TreeMap<>();
        
        Date date = firstDay;
        //Insert all days to a map.
        for (int i = 0; i < numberOfDays; i++) {
            Date zeroTimeDate =  DateUtils.zeroedTimeDate(date);
            Day day = new Day(zeroTimeDate);
            days.put(zeroTimeDate, day);
            date = DateUtils.addHours(date, 24);
        }
        
        for (EventDTO event : eventDtos){
            Date zeroTimeDate =  DateUtils.zeroedTimeDate(event.getTimeStart());
            Day day = days.get(zeroTimeDate);
            if (day != null) //Ignore events which do not fall within the day range specified.
                day.addEvent(event);
        }
        
        return new ArrayList<Day>(days.values());
    }
    
}
