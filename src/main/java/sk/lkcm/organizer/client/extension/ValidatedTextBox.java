package sk.lkcm.organizer.client.extension;

import java.util.Date;

import sk.lkcm.organizer.client.DateUtils;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.TextBox;

public class ValidatedTextBox extends TextBox{
    
    DateTimeFormat format;
    String pattern;
    String oldBcgColor;
    Date validatedDate;
    Boolean isValid;
    
    public ValidatedTextBox(String pattern){
        this();
        this.pattern = pattern;

    }
    
    
    public ValidatedTextBox(DateTimeFormat format){
        this();
        this.format = format;
    }
    
    private ValidatedTextBox(){
        
        addValueChangeHandler(new ValueChangeHandler<String>(){
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                validate();
            }
        });
        
        addKeyUpHandler(new KeyUpHandler(){
            @Override
            public void onKeyUp(KeyUpEvent event) {
                validate();
            }
        });
    }
    
    
    public void reset(){
        resetBcg();
        isValid = null;
    }
    
    private void setError(){
        if (oldBcgColor == null)
            oldBcgColor = this.getElement().getStyle().getBackgroundColor();
        this.getElement().getStyle().setBackgroundColor("pink");
    }
    
    private void resetBcg(){
        
        if (oldBcgColor!= null){
            this.getElement().getStyle().setBackgroundColor(oldBcgColor);
            oldBcgColor = null;
        }
    }
    
    
    private void validate(){
        isValid = false;
        String value = getValue();
        
        
        if (pattern != null)
            isValid = value.matches(pattern);
        else{
            try{
                validatedDate = format.parseStrict(value);
                isValid = true;
            }
            catch(IllegalArgumentException e){}
        }
        
        if (isValid)
            resetBcg();
        else
            setError();
    }
    
    public boolean isValueValid(){
        
        if (isValid == null)
            validate();
        return isValid;
    }
    
    
    public long getMillisOfDay(){
        
        Date zeroHourDay = DateUtils.setToMidnight(validatedDate); 
        
        long millis = validatedDate.getTime() - zeroHourDay.getTime();
        
        return millis;
    }
    
}
