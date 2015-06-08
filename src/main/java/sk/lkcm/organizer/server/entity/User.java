package sk.lkcm.organizer.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @GeneratedValue
    @Id 
    private int id;
    private String name;
    @Column(unique=true)
    private String password;
    
    
    public User(){
        
    }
    public User(int id){
        this.id = id;
    }
    
    public User(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString(){
        return this.getClass().getSimpleName() +  " [id: " + id + ", name: " + name + "]";
    }
}
