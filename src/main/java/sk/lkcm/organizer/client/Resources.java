package sk.lkcm.organizer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface Resources extends ClientBundle {
    public static final Resources INSTANCE = GWT
            .create(Resources.class);

    @Source("style.css")
    Css css();
}
