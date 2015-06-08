package sk.lkcm.organizer.shared;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DayDTO implements Serializable {
    

    private static final long serialVersionUID = 1L;
    private Date date;

    private List<EventDTO> events;
    
    //Set the OrgEventDTO objects and date for the daty - should be MIDNIGHT TIME.
    public void setDate(Date midnightDate){
        date = midnightDate;
    }
    
    public List<EventDTO> getEntries(){
        return events;
    }
    
    public void  setEvents(List<EventDTO> entries){
        this.events = entries;
    }
    
    public Date getDate(){
        return date;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("DayDTO  " + date + "\n");
        
        if (events!= null)
            for (EventDTO event : events)
                sb.append("\t\t- " + event + "\n");
        
        return sb.toString();
    }
    
}
