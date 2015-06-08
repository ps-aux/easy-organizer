package sk.lkcm.organizer.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginForm extends Composite {
    
    interface MyBinder extends UiBinder<Widget, LoginForm>{} 
    private static MyBinder uiBinder = GWT.create(MyBinder.class); 
    
    @UiField
    Button buttonLogIn, demoButton;
    @UiField
    TextBox inputName;
    @UiField
    PasswordTextBox  inputPassword;
    
    private LoadingGlass loadingGlass;
    
    public LoginForm(){
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w); 
        
        EnterListener enterListener = new EnterListener();
        inputName.addKeyDownHandler(enterListener);
        inputPassword.addKeyDownHandler(enterListener);
        buttonLogIn.setEnabled(false);
    }
    
    /*
     * No need to maintain list of handlers.
     */
    public void setLoginButtonClickHandler(ClickHandler handler){
        buttonLogIn.addClickHandler(handler);
    }
    
    public void setDemoButtonClickHandler(ClickHandler handler){
        demoButton.addClickHandler(handler);
    }

    public String getPasswordInput(){
        return inputPassword.getText();
    }
    
    public String getNameInput(){
        return inputName.getText();
    }
    
    
    public void setBusyState(){
        inputName.setEnabled(false);
        inputPassword.setEnabled(false);
        buttonLogIn.setEnabled(false);
        // Show loading glass.
        if (loadingGlass == null)
            loadingGlass = new LoadingGlass(LoginForm.this);
        loadingGlass.showCentered();
    }
    
    public void setActiveState(){
        inputName.setEnabled(true);
        inputPassword.setEnabled(true);
        buttonLogIn.setEnabled(true);
    }
    
    
    private void validateIfCanLogin(){
        if (inputName.getText().length() > 1 &&
                inputPassword.getText().length() > 1)
            buttonLogIn.setEnabled(true);
        else
            buttonLogIn.setEnabled(false);

    }
    
    /**
     * Simulates click on the login button if enter is pressed.
     */
    private class EnterListener implements KeyDownHandler{

        @Override
        public void onKeyDown(KeyDownEvent e) {
            
            validateIfCanLogin();
            
            if(e.getNativeKeyCode() == KeyCodes.KEY_ENTER 
                    && buttonLogIn.isEnabled())
                buttonLogIn.click();
    
            
        }
    }

}
