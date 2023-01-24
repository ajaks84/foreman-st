package by.dziashko.frm.ui.forms.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorFan;
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

public class AspiratorFanForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AspiratorFan> binder = new Binder<>(AspiratorFan.class);
    private AspiratorFan aspiratorFan;

    public AspiratorFanForm() {
        addClassName("contact-form");
        VerticalLayout lyt = new VerticalLayout(modelName, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new AspiratorFanForm.DeleteEvent(this, aspiratorFan)));
        close.addClickListener(click -> fireEvent(new AspiratorFanForm.CloseEvent(this, aspiratorFan)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(aspiratorFan);
            fireEvent(new AspiratorFanForm.SaveEvent(this, aspiratorFan));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setFanMotorData(AspiratorFan aspiratorFan) {
        this.aspiratorFan = aspiratorFan;
        binder.readBean(aspiratorFan);
    }

    // Events
    public static abstract class FanMotorDataFormEvent extends ComponentEvent<AspiratorFanForm> {
        private AspiratorFan aspiratorFan;

        protected FanMotorDataFormEvent(AspiratorFanForm source, AspiratorFan aspiratorFan) {
            super(source, false);
            this.aspiratorFan = aspiratorFan;
        }

        public AspiratorFan getFanMotorData() { return aspiratorFan; }
    }

    public static class SaveEvent extends AspiratorFanForm.FanMotorDataFormEvent {
        SaveEvent(AspiratorFanForm source, AspiratorFan aspiratorFan) {
            super(source, aspiratorFan);
        }
    }

    public static class DeleteEvent extends AspiratorFanForm.FanMotorDataFormEvent {
        DeleteEvent(AspiratorFanForm source, AspiratorFan aspiratorFan) {
            super(source, aspiratorFan);
        }
    }

    public static class CloseEvent extends AspiratorFanForm.FanMotorDataFormEvent {
        CloseEvent(AspiratorFanForm source, AspiratorFan aspiratorFan) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}



