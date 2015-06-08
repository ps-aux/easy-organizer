package sk.lkcm.organizer.client.view;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class ToolBar extends Composite{
    
    interface MyBinder extends UiBinder<Widget, ToolBar>{} 
    private static MyBinder uiBinder = GWT.create(MyBinder.class); 
    
    private static final int OFFSET = 100;
    
    @UiField
    Button previousButton, nextButton, addNewButton, calendarButton;
    @UiField
    HTML lblWeek;
    
    private Controller controller;
    private PopupPanel datePickerPane;
    
    public ToolBar(final Controller controller){
        this.controller = controller;
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w); 
        lblWeek.setText("--");
        
        ClickHandler handler = new ButtonClickHandler();
        nextButton.addClickHandler(handler);
        previousButton.addClickHandler(handler);
        addNewButton.addClickHandler(handler);
        calendarButton.addClickHandler(handler);
        
        

        datePickerPane = new PopupPanel(true);
        datePickerPane.setAnimationEnabled(true);
        
        
        DatePicker datePicker = new DatePicker();
        datePickerPane.setWidget(datePicker);
        
        datePicker.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
        
        
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                datePickerPane.hide();
                controller.loadWeek(event.getValue());
            }
        });
        
    }
    
    
    public Button getButtonAddEvent(){
        return addNewButton;
    }
    
    public Button getButtonCalendar(){
        return calendarButton;
    }
    
    
    public void setLabelText(String text){
        lblWeek.setHTML(text);
    }
    
    /**
     * Single instance of click handler for performance reasons.
     */
    private class ButtonClickHandler implements ClickHandler{

        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == nextButton)
                controller.loadNextWeek();
            else if (event.getSource() == previousButton)
                controller.loadPreviousWeek();
            else if (event.getSource() == calendarButton){
                int left = calendarButton.getAbsoluteLeft() +
                        calendarButton.getOffsetWidth();
                int top = calendarButton.getAbsoluteTop();
                datePickerPane.setPopupPosition(left, top);
                datePickerPane.show();
            }else if (event.getSource() == addNewButton){
                int left = addNewButton.getAbsoluteLeft();
                int top = addNewButton.getAbsoluteTop();
                
                controller.showCreateEventDialog(null, left + OFFSET , top + OFFSET);
            }
            else
                throw new AssertionError();
        }
        
    }
    
    
}
