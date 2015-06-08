package sk.lkcm.organizer.client.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.lkcm.organizer.client.DateUtils;
import sk.lkcm.organizer.client.Day;
import sk.lkcm.organizer.client.OrganizerApp;
import sk.lkcm.organizer.client.OrganizerServiceWrapper;
import sk.lkcm.organizer.shared.EventDTO;
import sk.lkcm.organizer.shared.NoSessionException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Controller {

    private OrganizerView mainView;
    private CreateEditEventDialog createEditDialog;
    
    private LoadWeekCallback loadWeekCallback;
    private DeleteEventCallback deleteEventCallback;
    private CreateEventCallback createEventCallback;
    private EditEventCallback editEventCallback;
    
    
    private Date weekStart, weekEnd;
    //private Map<Integer,EventView> eventViews = new HashMap<Integer,EventView>();
    private Map<Date,DayView> dayViews = new HashMap<Date,DayView>();
    private Map<Date,Day> daysMap = new HashMap<Date,Day>();
    private OrganizerServiceWrapper organizerService;
    private OrganizerApp app;
   
    /**
     * TODO maybe not best way to couple it to OrganizerApp,
     * consider using listener
     */
    public Controller(OrganizerServiceWrapper organizerService,
            OrganizerApp app){
        this.organizerService = organizerService;
        this.app = app;
        createEditDialog = new CreateEditEventDialog(this);
        loadWeekCallback = new LoadWeekCallback();
        deleteEventCallback = new DeleteEventCallback();
        createEventCallback = new CreateEventCallback();
        editEventCallback = new EditEventCallback();
    }
    
    public void setView(OrganizerView view) {
        this.mainView = view;
    }
    
    public void setCreateEditDialog(CreateEditEventDialog dialog) {
        createEditDialog = dialog;
    }
    

    /*
    void unregisterEventView(int eventId, EventView view){
        eventViews.put(eventId, view);
    }
    
    void registerEventView(int eventId){
        eventViews.remove(eventId);
    }*/
    
    void registerDayView(Date date, DayView view){
        dayViews.put(date, view);
    }
    
    void unregisterDayView(Date date){
        dayViews.remove(date);
    }
    
   
    public void loadWeek(Date date){
        if (loadWeekCallback.inProgress){
            Window.alert("One query already in progress. Please wait..."); //TODO maybe remove
            return;
        }
        Date first = DateUtils.getStartOfTheWeek(date);
        Date last = new Date(first.getTime() + 7 * DateUtils.MILLIS_PER_DAY -1);
        
        weekStart = first;
        weekEnd = last;
        
        log("Gettings events from  " + first + " - " + last);
        loadWeekCallback.prepareToCall(first,last);
        loadingStarting();
        organizerService.getEvents(first, last, loadWeekCallback);
    }
    
    public void loadNextWeek(){
        Date date = new Date (weekStart.getTime() + DateUtils.MILLIS_PER_DAY * 7);
        loadWeek(date);
    }
    
    public void loadPreviousWeek(){
        Date date = new Date (weekStart.getTime() - DateUtils.MILLIS_PER_DAY * 7);
        loadWeek(date);
    }
    
    
    public void showCreateEventDialog(Day day, int left, int top) {
        createEditDialog.showAddNew(day, left, top);
    }

    public void showEditEventDialog(EventDTO event, int left, int top) {
        createEditDialog.showEdit(event, left, top);
    }

    
    public void deleteEvent(EventDTO event) {
        if (deleteEventCallback.inProgress){
            Window.alert("One query already in progress. Please wait..."); //TODO maybe remove
            return;
        }
        deleteEventCallback.prepareToCall(event);
        log("Deleting event" + event);
        loadingStarting();
        organizerService.deleteEvent(event.getId(), deleteEventCallback);
    }
    
    public void createEvent(String name, Date dateStart, Date dateEnd){
        if (createEventCallback.inProgress){
            Window.alert("One query already in progress. Please wait..."); //TODO maybe remove
            return;
        }
        
        log("Creating event:'" + name + " " + dateStart + " - " + dateEnd);
        EventDTO dto = new EventDTO();
        dto.setName(name);
        dto.setTimeStart(dateStart);
        dto.setTimeEnd(dateEnd);
        createEventCallback.prepareToCall(dto);
        loadingStarting();
        organizerService.createEvent(dto, createEventCallback);
    }
    
    public void editEvent(EventDTO oldEvent, EventDTO newEvent){
        if (oldEvent.equals(newEvent))
            throw new IllegalArgumentException("The events are exactly the same");
        if (editEventCallback.inProgress){
            Window.alert("One query already in progress. Please wait..."); //TODO maybe remove
            return;
        }
        
        editEventCallback.prepareToCall(oldEvent, newEvent);
        log("Editing event. Old: " + oldEvent + ", new: " + newEvent);
        loadingStarting();
        organizerService.saveEvent(newEvent, editEventCallback);
        
    }
    
    private void log(String msg){
        GWT.log(msg);
    }
    
    private boolean isDisplayed(Date date){
        return date.getTime() > weekStart.getTime() &&
                    date.getTime() < weekEnd.getTime();
    }
    
    private void loadingStarting(){
        mainView.enterBusyState();
    }
    
    private void loadingFinished(){
        mainView.enterReadyState();
    }
    
    private void addNewEvent(EventDTO event){
        
        if (!isDisplayed(event.getTimeStart()))
            throw new IllegalArgumentException("The event does not belong to any displayed day");
        
        //Get the day where the event was added.
        Date date = DateUtils.zeroedTimeDate(event.getTimeStart());
        Day day = daysMap.get(date);
        day.addEvent(event); 
        //Display the day anew in the day view.
        dayViews.get(date).showDay(day);
    }
    
    private class LoadWeekCallback implements AsyncCallback<List<EventDTO>> {
        
        private boolean inProgress;
        private Date start;
        
        void prepareToCall(Date start, Date end){
            this.start = start;
            inProgress = true;
        }
        
        @Override
        public void onFailure(Throwable t){
            loadingFinished();
           GWT.log("ex: " + t.getClass());
            
            if (t instanceof NoSessionException)
                //TODO use listener?
                app.handleNoSessionException();
            else
                mainView.showInfoDialog("Oops..",""
                        + "Sorry, the week could not be loaded");
            log("Events could not be loaded. Error: " + t);
            inProgress = false;
        }

        @Override
        public void onSuccess(List<EventDTO> result) {
            loadingFinished();
            
            log("Events retrieved:");
            for (EventDTO event : result)
                log(event.toString());
            
            List<Day> days = ControllerHelper.buildDays(start,7,result);
            
            //Save the days in map for later easy access.
            for (Day day : days)
                daysMap.put(day.getDate(), day);
            
            mainView.showDays(days);
            
            //Set the date to be somewhere in the middle to prevent the bugs related to calculation.
            Date date = new Date(start.getTime() + DateUtils.MILLIS_PER_DAY *2);
            String text = DateUtils.MONTH_YEAR_FORMAT.format(date) + " - week " + DateUtils.getWeekOfTheYear(date);
            mainView.setToolbarLabelText(text);
            inProgress = false;
        }
    };
    
    
    private class DeleteEventCallback implements AsyncCallback<Void>{
        
        private boolean inProgress;
        private EventDTO event;
        
        void prepareToCall(EventDTO event){
            this.event = event;
            inProgress = true;
        }
        
        @Override
        public void onFailure(Throwable t) {
            loadingFinished();

            mainView.showInfoDialog("Oops..", ""
                    + "Sorry, the event could not be deleted");
            inProgress = false;
        }

        @Override
        public void onSuccess(Void result) {
            loadingFinished();
            //Success, so remove it from day.
            Date date = DateUtils.zeroedTimeDate(event.getTimeStart());
            Day day = daysMap.get(date);
            day.removeEvent(event);
            //And reload the day view again.
            dayViews.get(date).showDay(day);
            inProgress = false;
        }
        
    }
    
    private class CreateEventCallback implements AsyncCallback<Integer>{
        
        private boolean inProgress;
        private EventDTO event;
        
        void prepareToCall(EventDTO event){
            inProgress = true;
            this.event = event;
        }
        
        @Override
        public void onFailure(Throwable t) {
            loadingFinished();

            mainView.showInfoDialog("Oops..", ""
                    + "Sorry, the event could not be created");
            inProgress = false;
        }

        @Override
        public void onSuccess(Integer result) {
            loadingFinished();
            log("Event " + event + "successfully created and has been assigned id " + result);
            //If the event added falls into the displayed week.
            if (isDisplayed(event.getTimeStart())){
                event.setId(result);
                addNewEvent(event);
            }
            inProgress = false;
        }
    }
    
    private class EditEventCallback implements AsyncCallback<Void>{
        
        private boolean inProgress;
        private EventDTO oldEvent;
        private EventDTO newEvent;
        
        void prepareToCall(EventDTO oldEvent, EventDTO newEvent){
            inProgress = true;
            this.oldEvent = oldEvent;
            this.newEvent = newEvent;
        }
        
        @Override
        public void onFailure(Throwable t) {
            loadingFinished();
            mainView.showInfoDialog("Oops..", ""
                    + "Sorry, the edited event could not be saved");
            inProgress = false;
        }

        @Override
        public void onSuccess(Void result) {
            loadingFinished();
            Date oldDate = DateUtils.zeroedTimeDate(oldEvent.getTimeStart());
            Day oldDay = daysMap.get(oldDate);
            //If the event stayed in the same day just remove the old one from the day,
            //add new one and reload the view.
            if (DateUtils.areSameDay(oldEvent.getTimeStart(), newEvent.getTimeStart())){
                log("same");
                oldDay.removeEvent(oldEvent);
                oldDay.addEvent(newEvent); 
                dayViews.get(oldDate).showDay(oldDay);
            }else{ //It was removed from the the day.
                log("not same");
                //Remove it from the old day & refresh the view.
                oldDay.removeEvent(oldEvent);
                dayViews.get(oldDate).showDay(oldDay);
                log("fff");
                //If it was moved into a day within the displayed week then make it part of that day and show it.
                if (isDisplayed(newEvent.getTimeStart()))
                    addNewEvent(newEvent);
                
            }
           
            inProgress = false;
        }

    }

}
