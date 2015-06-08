package sk.lkcm.organizer.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Footer extends Composite{
    interface MyBinder extends UiBinder<Widget, Footer> {}
    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    public Footer() {
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w);
    }
}
