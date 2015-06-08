package sk.lkcm.organizer.client;



import java.util.Date;

import sk.lkcm.organizer.client.view.Controller;
import sk.lkcm.organizer.client.view.Footer;
import sk.lkcm.organizer.client.view.Header;
import sk.lkcm.organizer.client.view.LoginForm;
import sk.lkcm.organizer.client.view.LoginInfoPane;
import sk.lkcm.organizer.client.view.OrganizerView;
import sk.lkcm.organizer.shared.OrganizerService;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class OrganizerApp implements EntryPoint {
    
    private RootPanel root;
    private OrganizerServiceWrapper service = new OrganizerServiceWrapper();
    private LoginForm loginForm;
    private LoginInfoPane loginInfoPane;
    private Header header;
    private VerticalPanel verticalPanel;
    private FlowPanel bodyPanel;  
    private static int BODY_HEIGHT = 350;
    private Controller controller;
    private OrganizerView organizerView;
    
    public void onModuleLoad() {
        Resources.INSTANCE.css().ensureInjected();
        root = RootPanel.get("main");  
        verticalPanel = new VerticalPanel();
        bodyPanel = new FlowPanel();
        
        root.add(verticalPanel);


        loginForm = new LoginForm();
        loginInfoPane = new LoginInfoPane();
        header = new Header();

        header.setLogOutButtonClickHandler(new ClickHandler(){

            @Override
            public void onClick(ClickEvent event) {
                logOut();
            }
            
        });
        
        loginForm.setLoginButtonClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                loginInfoPane.hideMessage();
                logUser(loginForm.getNameInput(), loginForm.getPasswordInput());
            }
        });
        
        loginForm.setDemoButtonClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                runDemo();
            }
        });

        verticalPanel.add(header);
        verticalPanel.add(bodyPanel);
        verticalPanel.setCellHeight(bodyPanel, BODY_HEIGHT + "px");
        verticalPanel.add(new Footer());
        controller = new Controller(service, this);
        organizerView = new OrganizerView(controller);
        controller.setView(organizerView);
        checkSession();
    }
    
    private void showLogged(String name){
        bodyPanel.remove(loginForm);
        bodyPanel.remove(loginInfoPane);
        loginInfoPane.hideMessage();
        bodyPanel.add(organizerView);
        controller.loadWeek(new Date());
        header.setLoggedUser(name);
        header.setActive();
    }
    
    private void showUnlogged(){
        //To hide the circle indicating progress which is not removed 
        //when the view is removed.
        organizerView.enterReadyState();
        header.setNotLogged();
        bodyPanel.remove(organizerView);
        bodyPanel.add(loginForm);
        bodyPanel.add(loginInfoPane);
    }
    
    private void checkSession(){
        service.getLoggedUser(new AsyncCallback<String>() {
            
            @Override
            public void onSuccess(String result) {
                if (result.equals(OrganizerService.NO_USER)){
                    showUnlogged();}
                else{
                    showLogged(result);
                }
            }
            
            @Override
            public void onFailure(Throwable caught) {
                log("Error while checking sessionsin:\n" + caught);
            }
        });
    }
    
    
    private void logUser(String name, String password){
        service.logUserIn(name, password, new AsyncCallback<String>() {
            
            @Override
            public void onSuccess(String result) {
                if (result.equals(OrganizerService.BAD_PASSWORD) ||
                        result.equals(OrganizerService.NO_USER)){
                    loginInfoPane.setMessage("Bad user name or password");
                }else{
                    showLogged(result);
                }
            }
            
            @Override
            public void onFailure(Throwable caught) {
                loginInfoPane.setMessage("An attempt to log-in failed");
                log("Error while trying to login:\n" + caught);
            }
        });
    }
    
    
    private void logOut(){
        organizerView.enterBusyState();
        header.setBusy();
        service.logOut(new AsyncCallback<Void>() {
            
            @Override
            public void onSuccess(Void result) {
                showUnlogged();
            }
            
            @Override
            public void onFailure(Throwable caught) {
                String title ="Oops..";
                String msg ="Could not log out";
                organizerView.enterReadyState();
                organizerView.showInfoDialog(title, msg);
                log("Error while trying to log-out:\n" + caught);
            }
        });
    }
    
    
    private void runDemo(){
        AsyncCallback<String> callback = new AsyncCallback<String>(){

            @Override
            public void onFailure(Throwable t) {
                loginInfoPane.setMessage("Attempt to run demo failed");
                log("Error while trying to run demo:\n" + t);
                Window.alert("not success:\n" + t);
            }

            @Override
            public void onSuccess(String name) {
                showLogged(name);
                String title ="Demo mode";
                String msg ="You are currently in the demo mode. You can play around - edit,"
                        + " delete or create events. Upon logging out this account and its data"
                        + " will be discarded.";
                organizerView.showInfoDialog(title, msg);
            }
        };
        
        GWT.log("Running demo");
        service.runDemo(callback);
    }
    
    public void handleNoSessionException(){
        showUnlogged();
        loginInfoPane.setMessage("Seems that your session has been cancelled :/");
    }
    
    private void log(String msg){
        GWT.log(msg);
    }
}
    
    
