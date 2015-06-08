package sk.lkcm.organizer.client.view;

import java.util.ArrayList;
import java.util.List;

import sk.lkcm.organizer.client.DateUtils;
import sk.lkcm.organizer.client.extension.FadeAnimation;
import sk.lkcm.organizer.shared.EventDTO;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EventView extends Composite {

    interface MyBinder extends UiBinder<Widget, EventView> {
    }

    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    @UiField
    Label time;
    @UiField
    Label text;
    @UiField
    Button buttonEdit, buttonRemove;
    private Animation fadeInAnimation;

    private EventDTO displayedEvent;

    public EventView(final Controller controller) {
        Widget w = (uiBinder.createAndBindUi(this));
        initWidget(w);

        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hideButtons();
                if (event.getSource() == buttonEdit) {
                    int left = getAbsoluteLeft();
                    int top = getAbsoluteTop();
                    controller.showEditEventDialog(displayedEvent, left, top);
                } else if (event.getSource() == buttonRemove)
                    controller.deleteEvent(displayedEvent);
            }
        };
        
        
        List<Button> buttons = new ArrayList<Button>();
        buttons.add(buttonEdit);
        buttons.add(buttonRemove);

        fadeInAnimation = new FadeAnimation(buttons, true);
        
        buttonEdit.addClickHandler(handler);
        buttonRemove.addClickHandler(handler);

        MouseOverHandler mouseOverHandler = new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                fadeInAnimation.run(300);
                buttonEdit.setVisible(true);
                buttonRemove.setVisible(true);
            }

        };

        MouseOutHandler mouseOutHandler = new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                hideButtons();
            }

        };

        w.addDomHandler(mouseOverHandler, MouseOverEvent.getType());
        w.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
        hideButtons();

    }

    private void hideButtons() {
        buttonEdit.setVisible(false);
        buttonRemove.setVisible(false);
    }

    public void showNewEvent(EventDTO eventDto) {
        text.setText(eventDto.getName());
        String timeStr = DateUtils.TIME_FORMAT.format(eventDto.getTimeStart());

        timeStr += eventDto.getTimeEnd() == null ? "" : " - "
                + DateUtils.TIME_FORMAT.format(eventDto.getTimeEnd());
        time.setText(timeStr);
        /*
         * if (displayedEvent != null)
         * controller.registerEventView(displayedEvent.getId()); //Remove
         * relation to old event id. //Tell controller that this view is
         * displaying the view with this id.
         * controller.unregisterEventView(eventDto.getId(),this); //Remove
         * relation to old event id.
         */
        displayedEvent = eventDto;
    }

}
