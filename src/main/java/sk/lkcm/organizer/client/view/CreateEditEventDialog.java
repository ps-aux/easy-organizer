package sk.lkcm.organizer.client.view;

import java.util.Date;

import sk.lkcm.organizer.client.DateUtils;
import sk.lkcm.organizer.client.Day;
import sk.lkcm.organizer.shared.EventDTO;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

/**
 * Dialog for adding a new event or editing existing one. Before being shown
 * coordinates should be explicitely set (via ancestor class methods).
 * 
 */
public class CreateEditEventDialog extends DialogBox {

    enum Mode {
        CREATE, // Create mode.
        EDIT // Editing the event already existing and displayed
    };

    private static final String EDIT_CAPTION = "Edit the event";
    private static final String ADD_CAPTION = "Add new event";
    private static final String COLOR_BAD = "pink"; // TODO move to style
    private static final String COLOR_DEFAULT = "white"; // TODO move to style

    @UiField
    TextArea txtAreaDescription;
    @UiField
    DateBox dateBox;
    @UiField
    UTCTimeBox timeBoxFrom, timeBoxTo;
    @UiField
    CheckBox chBoxStartOnly;
    @UiField
    Button buttonOk, buttonCancel;
    @UiField
    Label lblSeparator, lblError;

    private String origDesc;
    private EventDTO currentEvent;

    private Mode mode;
    private boolean isBeingShown;
    private Controller controller;

    interface MyBinder extends UiBinder<Widget, CreateEditEventDialog> {
    }

    private static MyBinder uiBinder = GWT.create(MyBinder.class);

    public CreateEditEventDialog(Controller controller) {
        Widget w = (uiBinder.createAndBindUi(this));

        setWidget(w);
        setText("--- error ? -------");

        this.controller = controller;

        setModal(true);
        setAnimationEnabled(true);
        setGlassStyleName("myglass"); // Set glass style name to non-existent
                                        // one so it wont be shaded.

        dateBox.setFormat(new DateUtils.StrictDateBoxFormat(
                DateUtils.DATE_FORMAT));
        dateBox.getDatePicker().setStyleName("day-date-picker");
        lblError.getElement().getStyle().setOpacity(0);

        buttonOk.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                okButtonPressed();
            }
        });

        buttonCancel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                exit();
            }
        });

        chBoxStartOnly.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                setIsStartOnly(event.getValue());
            }
        });

    }

    
    private void setIsStartOnly(boolean b){
        if (b) {
            timeBoxTo.getElement().getStyle().setOpacity(0);
            lblSeparator.getElement().getStyle().setOpacity(0);
        } else {
            timeBoxTo.getElement().getStyle().setOpacity(100);
            lblSeparator.getElement().getStyle().setOpacity(100);
        }    
    }
    
    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);
        int typeInt = event.getTypeInt();
        if (typeInt != Event.ONKEYDOWN)
            return;

        int keyCode = event.getNativeEvent().getKeyCode();
        
        if (keyCode == KeyCodes.KEY_ESCAPE)
            exit();
        else if (keyCode == KeyCodes.KEY_ENTER)
            okButtonPressed();
    }

    private void okButtonPressed(){
        if (!verify())
            return;
        saveAndExit();
    }
    
    
    private void checkBeforeShowing() {
        if (isBeingShown)
            throw new IllegalStateException("The dialog is already shown");
    }

    public void showAddNew(int left, int top) {
        checkBeforeShowing();
        setPopupPosition(left, top);

        getCaption().setText(ADD_CAPTION);
        mode = Mode.CREATE;
        super.show();
    }

    /**
     * Shows 'add new event' dialog for a given day.
     * 
     * @param day
     */
    public void showAddNew(Day day, int left, int top) {
        if (isBeingShown)
            throw new IllegalStateException("The dialog is already shown");
        setPopupPosition(left, top);

        getCaption().setText(ADD_CAPTION);

        if (day != null) {
            dateBox.setValue(day.getDate());
            dateBox.setEnabled(false);
        }
        mode = Mode.CREATE;

        super.show();
        txtAreaDescription.setFocus(true);
        isBeingShown = true;
    }

    /**
     * Shows edit dialog for a given entry.
     * 
     * @param entry
     */
    public void showEdit(EventDTO entry, int left, int top) {
        if (isBeingShown)
            throw new IllegalStateException("The dialog is already shown");
        setPopupPosition(left, top);

        mode = Mode.EDIT;

        getCaption().setText(EDIT_CAPTION);

        currentEvent = entry;

        origDesc = entry.getName();
        txtAreaDescription.setText(origDesc);

        dateBox.setValue(entry.getTimeStart());

        timeBoxFrom.setText(DateUtils.TIME_FORMAT.format(entry.getTimeStart()));

        if (entry.getTimeEnd() != null)
            timeBoxTo.setText(DateUtils.TIME_FORMAT.format(entry.getTimeEnd()));
        else{
            setIsStartOnly(true);
            chBoxStartOnly.setValue(true);
        }

        super.show();
        txtAreaDescription.setFocus(true);
        txtAreaDescription.selectAll();
        isBeingShown = true;
    }

    /*
     * Not support operation for this class. Throws {@link
     * UnsupportedOperationException};
     * 
     * @see com.google.gwt.user.client.ui.DialogBox#show()
     */
    @Override
    public void show() {
        throw new UnsupportedOperationException();
    }

    /*
     * Not support operation for this class. Throws {@link
     * UnsupportedOperationException};
     * 
     * @see com.google.gwt.user.client.ui.DialogBox#show()
     */
    @Override
    public void center() {
        throw new UnsupportedOperationException();
    }

    /**
     * Verifies that all inputs and returns either true (all valid) or false
     * (something is not valid). Inputs are verified one by one and all which
     * are invalid are marked.
     */
    private boolean verify() {

        boolean valid = true;

        if (txtAreaDescription.getText().equals("")) {
            valid = false;
            txtAreaDescription.getElement().getStyle()
                    .setBackgroundColor(COLOR_BAD);
        }

        if (dateBox.getValue() == null) {
            valid = false;
            dateBox.getElement().getStyle().setBackgroundColor(COLOR_BAD);
        }

        if (timeBoxFrom.getValue() == null) {
            valid = false;
            timeBoxFrom.getElement().getStyle().setBackgroundColor(COLOR_BAD);
        }

        if (!chBoxStartOnly.getValue() && timeBoxTo.getValue() == null) {
            valid = false;
            timeBoxTo.getElement().getStyle().setBackgroundColor(COLOR_BAD);
        }

        //Check if end date is not before start date
        if (!chBoxStartOnly.getValue()){ //Only when NOT star only
            if (timeBoxTo.getValue() <= timeBoxFrom.getValue()){
                valid = false;
                timeBoxTo.getElement().getStyle().setBackgroundColor(COLOR_BAD);
            }
        }

        if (!valid)
            showError("Some inputs are invalid");

        return valid;
    }

    private void clearInputs() {
        txtAreaDescription.setText("");
        dateBox.setValue(null);
        dateBox.setEnabled(true);
        timeBoxFrom.setValue(null);
        timeBoxTo.setValue(null);
        chBoxStartOnly.setValue(false);
    }

    private void saveAndExit() {

        long dayMillis = dateBox.getValue().getTime();
        Date dateFrom = new Date(dayMillis + timeBoxFrom.getValue());
        Date dateTo = null;
        if (!chBoxStartOnly.getValue())
            dateTo = new Date(dayMillis + timeBoxTo.getValue());
        String name = txtAreaDescription.getText();

        if (mode == Mode.EDIT) { // Save the event with newly edited values.
            EventDTO changedEvent = new EventDTO();
            changedEvent.setName(name);
            changedEvent.setTimeStart(dateFrom);
            changedEvent.setTimeEnd(dateTo);
            changedEvent.setId(currentEvent.getId());
            // See if anything has changed.
            if (changedEvent.equals(currentEvent)) {
                showError("Nothing modified");
                return;
            }
            controller.editEvent(currentEvent, changedEvent);

        } else if (mode == Mode.CREATE) {
            controller.createEvent(name, dateFrom, dateTo);
        } else
            throw new AssertionError();
        exit();
    }

    private void showError(String msg){
        lblError.setText(msg);
        lblError.getElement().getStyle().setOpacity(100);
    }
    
    /**
     * Resets the dialog to the initial state - clearing all inputs and styles.
     */
    private void reset() {
        clearInputs();
        txtAreaDescription.getElement().getStyle()
                .setBackgroundColor(COLOR_DEFAULT);
        dateBox.getElement().getStyle().setBackgroundColor(COLOR_DEFAULT);
        timeBoxTo.getElement().getStyle().setBackgroundColor(COLOR_DEFAULT);
        timeBoxFrom.getElement().getStyle().setBackgroundColor(COLOR_DEFAULT);
        // Make 'time-to' time box visible.
        timeBoxTo.getElement().getStyle().setOpacity(100);
        // Hide error message.
        lblError.getElement().getStyle().setOpacity(0);
    }

    private void exit() {

        // Inform that editing has finished - so the style can be set back to
        // normal.
        if (currentEvent != null)
            // currentEvent.finishedEditing();
            currentEvent = null;
        mode = null;
        reset();
        hide();
        isBeingShown = false;
    }

}