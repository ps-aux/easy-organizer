package sk.lkcm.organizer.shared;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("springGwtServices/organizer-service")
public interface OrganizerService extends RemoteService {
    
    static final String NO_USER = "__xxxxnoUserLoggedxxxxx";
    static final String BAD_PASSWORD = "__xxxxnoBadPasswordxxxxx";
    

    String getLoggedUser();
    String logUserIn(String username, String password);
    void logOut() throws NoSessionException;
    
    List<EventDTO> getEvents(Date start, Date end) throws NoSessionException;
    void saveEvent(EventDTO event) throws NoSessionException;
    int createNewEvent(EventDTO event) throws NoSessionException;
    void deleteEvent(int id) throws NoSessionException;
    
    String runDemo();
    
}
