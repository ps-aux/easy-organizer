package sk.lkcm.organizer.shared;

import java.io.Serializable;
import java.util.List;


public class WeekDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int weekOfYear;
    private int year;
    private List<DayDTO> days;
    
    
    public int getWeekOfYear() {
        return weekOfYear;
    }
    public void setWeekOfYear(int weekOfYyear) {
        this.weekOfYear = weekOfYyear;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    
    public List<DayDTO> getDays() {
        return days;
    }
    public void setDays(List<DayDTO>days) {
        this.days = days;
    }
    
    public DayDTO getDay(int dayOfWeek){
        return days.get(dayOfWeek);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("WeekDTO " + weekOfYear + "/" + year + "\n");
        
        for (DayDTO day : days)
            sb.append("\t " + day);
        
        return sb.toString();
    }

}
