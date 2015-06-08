package sk.lkcm.organizer.client.extension;

import java.util.Collection;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.Widget;

public class FadeAnimation extends Animation{

    private Widget widget;
    private Collection<? extends Widget> widgets;
    private boolean fadeIn; 
    
    public FadeAnimation(Widget widget, boolean fadeIn) {
        this.widget = widget;
        this.fadeIn = fadeIn;
    }
    
    public FadeAnimation(Collection<? extends Widget> widgets, boolean fadeIn) {
        this.widgets = widgets;
        this.fadeIn = fadeIn;
    }

    @Override
    protected void onUpdate(double progress) {
        if (fadeIn)
            fadeInUpdate(progress);
        else
            fadeOutUpdate(progress);
    }
    
    private void fadeInUpdate(double progress){
        if (widgets == null)
            widget.getElement().getStyle().setOpacity(progress);
        else
            for (Widget w : widgets)
                w.getElement().getStyle().setOpacity(progress);
    }

    private void fadeOutUpdate(double progress){
        if (widgets == null)
            widget.getElement().getStyle().setOpacity(1 - progress);
        else
            for (Widget w : widgets)
                w.getElement().getStyle().setOpacity(1 - progress);
    }
}
