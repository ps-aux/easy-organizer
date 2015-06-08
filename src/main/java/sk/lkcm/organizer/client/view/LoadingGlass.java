package sk.lkcm.organizer.client.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Sets invisible panel on top of a widget.
 */
public class LoadingGlass extends PopupPanel{
    
    private static final int IMAGE_WIDTH = 48;
    private static final int IMAGE_HEIGHT = 48;
    private Widget widget;
    
    public LoadingGlass(Widget widget) {
        setStyleName("");
        this.widget = widget;

        SimplePanel sp = new SimplePanel();
        Image img = new Image("img/loading_b.gif");
        add(sp);
        
        
        int h = widget.getOffsetHeight();
        int w = widget.getOffsetWidth();
        
        Style style = img.getElement().getStyle();
        int imgL = (w - IMAGE_WIDTH)/2;
        int imgT = (h - IMAGE_HEIGHT)/2;
        style.setPosition(Position.ABSOLUTE);
        style.setTop(imgT, Unit.PX);
        style.setLeft(imgL, Unit.PX);
        sp.add(img);
        setHeight(h + "px");
        setWidth(w + "px");
        
        getElement().getStyle().setOpacity(0.2);
    }
    
    private void calculatePosition(){
        
        int l = widget.getAbsoluteLeft();
        int t = widget.getAbsoluteTop();
        
        setPopupPosition(l, t);
    }
    
    /**
     * Displays the loading glass exactly in the middle of the underlying widget.
     */
    public void showCentered(){
        calculatePosition();
        super.show();
    }
    
    /**
     * Overrides normal show and implements it with call to {@link #showCentered()} to
     * prevent bad uses.
     */
    @Override
    public void show(){
        showCentered();
    }
    
    
}

