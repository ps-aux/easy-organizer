package sk.lkcm.organizer.shared;

import java.io.Serializable;
import java.util.Date;

public class EventDTO implements Serializable, Comparable<EventDTO>{
    

    private static final long serialVersionUID = 1L;
    
    private Date timeStart;
    private Date timeEnd;
    private String name;
    private int id;
    
    public Date getTimeStart() {
        return timeStart;
    }
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }
    public Date getTimeEnd() {
        return timeEnd;
    }
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return  getClass().getSimpleName() + "[id: " + id + 
                " " + name + " " + timeStart +"-" + timeEnd + "]";
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((timeEnd == null) ? 0 : timeEnd.hashCode());
        result = prime * result
                + ((timeStart == null) ? 0 : timeStart.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventDTO other = (EventDTO) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (timeEnd == null) {
            if (other.timeEnd != null)
                return false;
        } else if (!timeEnd.equals(other.timeEnd))
            return false;
        if (timeStart == null) {
            if (other.timeStart != null)
                return false;
        } else if (!timeStart.equals(other.timeStart))
            return false;
        return true;
    }
    
    @Override
    public int compareTo(EventDTO other) {
        
        if (this == other)
            return 0;
        
        //First compare by start date.
        int res = timeStart.compareTo(other.getTimeStart());
        if (res != 0)
            return res;
        
        if (timeEnd == null && other.getTimeEnd() == null)
            return 0;
        if (timeEnd == null)
            return 1;
        if (other.getTimeEnd() == null)
            return -1;

        //If start date is the same, then by the last date.
        return timeEnd.compareTo(other.getTimeEnd());
        
    }
    
    
    
}
