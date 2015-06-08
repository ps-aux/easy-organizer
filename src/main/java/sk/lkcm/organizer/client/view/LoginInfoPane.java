package sk.lkcm.organizer.client.view;

import sk.lkcm.organizer.client.extension.FadeAnimation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoginInfoPane extends Composite {
    
    interface MyBinder extends UiBinder<Widget, LoginInfoPane>{} 
    private static MyBinder uiBinder = GWT.create(MyBinder.class); 

    @UiField
    Label label;

    public LoginInfoPane(){
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w); 
        setVisible(false);
    }
    
    public void setMessage(String msg){
        setVisible(true);
        new FadeAnimation(this, true).run(500);
        label.setText(msg);
    }
    
    public void hideMessage(){
        setVisible(false);
    }

}
