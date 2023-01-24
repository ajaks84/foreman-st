package by.dziashko.frm.i18n;

import com.vaadin.flow.component.html.Div;

public class MyLocale extends Div {

    public MyLocale() {
        setText(getTranslation("my.translation", getUserId()));
    }

    private Object getUserId() {
        return null;
    }
}