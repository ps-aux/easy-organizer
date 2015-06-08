package sk.lkcm.organizer.client;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import sk.lkcm.organizer.shared.EventDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Day {

    private List<EventDTO> events = new ArrayList<>();
    private List<EventDTO> eventsView = Collections.unmodifiableList(events);
    private Date date;

    public Day(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void addEvent(EventDTO event) {
        if (!CalendarUtil.isSameDate(date, event.getTimeStart()))
            throw new IllegalArgumentException("The event cannot be assigned to this day");
        events.add(event);
        Collections.sort(events);
    }

    public void removeEvent(EventDTO event) {
        if (!events.contains(event))
            throw new IllegalArgumentException("They day does not contain " + event);
        events.remove(event);
    }


    public List<EventDTO> getEvents() {
        return eventsView;
    }


}