package sk.lkcm.organizer.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class InfoDialog extends DialogBox {


    interface MyBinder extends UiBinder<Widget, InfoDialog> {}
    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    @UiField
    Button buttonOk;
    @UiField
    Label label;

    public InfoDialog(String title) {
        Widget w = (uiBinder.createAndBindUi(this));
        setWidget(w);

        setModal(true);
        setAnimationEnabled(true);
        setGlassStyleName("myglass"); // Set glass style name to non-existent
                                        // one so it wont be shaded.
        getCaption().setText(title);

        buttonOk.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                okButtonPressed();
            }
        });
    }

    
    public void setMessage(String msg){
        label.setText(msg);
    }
    
    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);
        int typeInt = event.getTypeInt();
        if (typeInt != Event.ONKEYDOWN)
            return;

        int keyCode = event.getNativeEvent().getKeyCode();
        
        if (keyCode == KeyCodes.KEY_ESCAPE)
            okButtonPressed();
        else if (keyCode == KeyCodes.KEY_ENTER)
            okButtonPressed();
    }

    private void okButtonPressed(){
        hide();
    }
    


}