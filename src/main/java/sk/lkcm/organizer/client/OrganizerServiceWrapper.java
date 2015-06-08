package sk.lkcm.organizer.client;

import java.util.Date;
import java.util.List;

import sk.lkcm.organizer.shared.EventDTO;
import sk.lkcm.organizer.shared.OrganizerService;
import sk.lkcm.organizer.shared.OrganizerServiceAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrganizerServiceWrapper {
    
    /* TODO add loging??*/
    
    private  OrganizerServiceAsync service;

    public OrganizerServiceWrapper() {
        service = GWT.create(OrganizerService.class);
    }

    public void getEvents(Date start, Date end,
            AsyncCallback<List<EventDTO>> callback) {
        service.getEvents(start, end, callback);
    }

    public void deleteEvent(int eventId, AsyncCallback<Void> callback) {
        service.deleteEvent(eventId, callback);
    }

    public void createEvent(EventDTO dto, AsyncCallback<Integer> callback) {
        service.createNewEvent(dto, callback);
    }
    
    public void saveEvent(EventDTO event, AsyncCallback<Void> callback) {
        service.saveEvent(event,callback);
    }
    
    public void getLoggedUser(AsyncCallback<String> callback) {
        service.getLoggedUser(callback);
    }
    
    public void logUserIn(String username, String password, AsyncCallback<String> callback) {
        service.logUserIn(username, password, callback);
    }

    public void runDemo(AsyncCallback<String> callback) {
        service.runDemo(callback);
    }

    public void logOut(AsyncCallback<Void> callback) {
        service.logOut(callback);
    }

}
