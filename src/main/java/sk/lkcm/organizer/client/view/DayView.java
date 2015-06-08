package sk.lkcm.organizer.client.view;

import java.util.ArrayList;
import java.util.List;

import sk.lkcm.organizer.client.DateUtils;
import sk.lkcm.organizer.client.Day;
import sk.lkcm.organizer.client.extension.FadeAnimation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


class DayView extends Composite {
    interface MyBinder extends UiBinder<Widget, DayView> {}

    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    @UiField
    Label dayOfWeek;
    @UiField(provided = true)
    VerticalPanel panel;
    @UiField
    Button buttonAdd;

    private final List<EventView> eventViews = new ArrayList<>();
    private final Controller controller;
    private Day displayedDay;
    private static final int OFFSET = 40;


    public DayView(final Controller controller) {
        this.controller = controller;
        panel = new VerticalPanel();

        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w);
        /*
         * dayOfWeek.setText(dayOfWeekStr); date.setText(dateStr);
         */

        buttonAdd.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int left = getAbsoluteLeft();
                int top = getAbsoluteTop();

                controller.showCreateEventDialog(displayedDay, left + OFFSET,
                        top + OFFSET);
            }
        });
        
    }
   
   void showDay(Day day){
       
        FadeAnimation animation = new FadeAnimation(panel, true);
        animation.run(400);
        
       //Register this view with new date in controller (and remove the old registration).
       if (displayedDay != null)
           controller.unregisterDayView(displayedDay.getDate());
       controller.registerDayView(day.getDate(), this);
       
       for (EventView view : eventViews)
           view.removeFromParent();
       
       //Ensure we have enough event views to display all views.
       int diff = day.getEvents().size() - eventViews.size();
       
       if (diff > 0)
           //Not enough - create the needed number.
           for ( int i = 0; i < diff; i++)
               eventViews.add(new EventView(controller));
       
       for (int i = 0; i < day.getEvents().size(); i++){
           EventView view = eventViews.get(i);
           view.showNewEvent(day.getEvents().get(i));
           panel.add(view);
       }
       
       String dayOfWeekStr = DateUtils.DAY_OF_WEEK_FORMAT.format(day.getDate());
       String dateStr = DateUtils.DATE_FORMAT.format(day.getDate());
       
       dayOfWeek.setText(dayOfWeekStr);
       buttonAdd.setText(dateStr);
       
       displayedDay = day;
   }
}
