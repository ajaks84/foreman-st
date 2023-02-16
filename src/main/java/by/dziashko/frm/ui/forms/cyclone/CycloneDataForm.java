package by.dziashko.frm.ui.forms.cyclone;

import by.dziashko.frm.backend.entity.cabin.CabinData;
import by.dziashko.frm.backend.entity.cyclone.CycloneData;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class CycloneDataForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<CycloneData> binder = new Binder<>(CycloneData.class);
    private CycloneData cycloneData;

    public CycloneDataForm() {

        VerticalLayout lyt = new VerticalLayout(modelName, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);

    }

    public void setOrderName(CycloneData cycloneData) {
        this.cycloneData = cycloneData;
        binder.readBean(cycloneData);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new CycloneDataForm.CloseEvent(this, cycloneData)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(cycloneData);
            fireEvent(new CycloneDataForm.SaveEvent(this, cycloneData));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCycloneData(CycloneData cycloneData) {
        this.cycloneData = cycloneData;
        binder.readBean(cycloneData);
    }

    // Events
    public static abstract class CycloneDataFormEvent extends ComponentEvent<CycloneDataForm> {
        private final CycloneData cycloneData;

        protected CycloneDataFormEvent(CycloneDataForm source, CycloneData cycloneData) {
            super(source, false);
            this.cycloneData = cycloneData;
        }

        public CycloneData getCycloneData() {
            return cycloneData;
        }
    }

    public static class SaveEvent extends CycloneDataForm.CycloneDataFormEvent {
        SaveEvent(CycloneDataForm source, CycloneData cycloneData) {
            super(source, cycloneData);
        }
    }

    public static class CloseEvent extends CycloneDataFormEvent {
        CloseEvent(CycloneDataForm source, CycloneData cycloneData) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
