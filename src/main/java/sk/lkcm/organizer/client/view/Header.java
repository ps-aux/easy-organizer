package sk.lkcm.organizer.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Header extends Composite {
    interface MyBinder extends UiBinder<Widget, Header> {}
    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    @UiField
    Label userNameLabel;
    
    @UiField
    Button logOutButton;
    
    public Header() {
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w);
        setNotLogged();
    }
   
    public void setLoggedUser(String name){
        userNameLabel.setText(name);
        logOutButton.setVisible(true);
    }
    
    public void setNotLogged(){
        userNameLabel.setText("not logged in");
        logOutButton.setVisible(false);
    }
    
    public void setBusy(){
        logOutButton.setEnabled(false);
    }
    
    public void setActive(){
        logOutButton.setEnabled(true);
    }
    
    /**
     * No need for having list of handlers.
     */
    public void setLogOutButtonClickHandler(ClickHandler handler){
        logOutButton.addClickHandler(handler);
    }
    
}
