package sk.lkcm.organizer.client.view;

import java.util.List;

import sk.lkcm.organizer.client.Day;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class WeekPanel extends Composite{

    private final HorizontalPanel panel;
    private final DayView[] dayViews = new DayView[7];

    public WeekPanel(Controller controller){
        panel = new HorizontalPanel();
        ScrollPanel scrollPanel = new ScrollPanel(panel);
        //panel.getElement().getStyle().setBorderColor("black");
        scrollPanel.setPixelSize(970, 400);
        initWidget(scrollPanel);
        for (int i = 0; i <7 ;i++){
            dayViews[i] = new DayView(controller);
            panel.add(dayViews[i]);
        }
    }
    
    public void showNewDays(List<Day> days){
        
        if (days.size() != 7)
            throw new IllegalArgumentException("The number of days must be 7");
        
        for (int i = 0; i <7 ;i++)
            dayViews[i].showDay(days.get(i));
    }
    
    
    
}
