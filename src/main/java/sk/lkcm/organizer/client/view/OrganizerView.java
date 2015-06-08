package sk.lkcm.organizer.client.view;


/**
 * Aggregates {@link WeekPanel} and {@link ToolBarWidget} ( and in the future maybe other parts)
 * into one entity.
 */
import java.util.List;

import sk.lkcm.organizer.client.Day;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class OrganizerView extends Composite{
    
    interface MyBinder extends UiBinder<Widget, OrganizerView>{} 
    private static MyBinder uiBinder = GWT.create(MyBinder.class); 
    
    private ToolBar toolBar;
    private WeekPanel daysPane;
    
    @UiField
    VerticalPanel panel;
    //private LoadWeekCallback callBack;
    private LoadingGlass loadingGlass;
    private final Widget w;
    
    
    public  OrganizerView(Controller controller){
        w = (uiBinder.createAndBindUi(this));
        initWidget(w); 
        
        
        daysPane = new WeekPanel(controller);
        toolBar = new ToolBar(controller);
        
        panel.add(toolBar);
        panel.add(daysPane);
    
    }
    
    void showDays(List<Day> days){
        daysPane.showNewDays(days);
    }

    
    public void showInfoDialog(String title, String msg){
        InfoDialog info = new InfoDialog(title);
        //Set the width programatically. If it is set from ui:binder for content,
        //the centering does not work properly.
        info.setWidth("400px");
        info.setMessage(msg);
        info.center();
        info.setPopupPosition(info.getAbsoluteLeft(), 150);
        
        
    }
    
    void setToolbarLabelText(String text){
        toolBar.setLabelText(text);
    }
    

    public void enterBusyState(){
        
        GWT.log("entering busy state");
        if (loadingGlass == null)
            loadingGlass = new LoadingGlass(w);
        
        if (loadingGlass != null)
            loadingGlass.showCentered();
        
    }
    
    public void enterReadyState(){
        GWT.log("leaving busy state");
        if (loadingGlass != null)
            loadingGlass.hide();
    }

}
