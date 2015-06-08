package sk.lkcm.organizer.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@DiscriminatorValue("DEFAULT")
@Table(name="organizer_event")
public  class OrganizerEvent implements Serializable, Comparable<OrganizerEvent>{
    
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    int id;
    
    @Column(name="user",nullable = false)
    int userId;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP
            )
    private Date start;
    
    
    @Column(name="end")
    @Temporal(value =TemporalType.TIMESTAMP)
    private Date end;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    public int getId(){
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStart(Date dateTime) {
        start = dateTime;
    }
    public void setEnd(Date dateTime) {
        end  =  dateTime;
    }
    
    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
    
    
    public void setTime(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    
    
    public void setTime(long start, long end) {
        setTime(new Date(start), new Date(end));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString(){
        return this.getClass().getSimpleName() +"[ id:" +id + ", " + name + ", " + start + 
                " - " + end + ", user " + userId +"]";
    }

    @Override
    public int compareTo(OrganizerEvent o) {
        int result = getStart().compareTo(o.getStart());
        if (result == 0)
            result = getEnd().compareTo(o.getEnd());
        
        return result;
    }
    
}

