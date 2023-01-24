package by.dziashko.frm.ui.forms.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
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

public class AspiratorBodyForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AspiratorBody> binder = new Binder<>(AspiratorBody.class);
    private AspiratorBody aspiratorBody;

    public AspiratorBodyForm() {
        addClassName("contact-form");
        VerticalLayout lyt = new VerticalLayout(modelName, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new AspiratorBodyForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(aspiratorBody);
            fireEvent(new AspiratorBodyForm.SaveEvent(this, aspiratorBody));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setEditEntry(AspiratorBody aspiratorBody) {
        this.aspiratorBody = aspiratorBody;
        binder.readBean(aspiratorBody);
    }

    // Events
    public static abstract class AspiratorBodyFormEvent extends ComponentEvent<AspiratorBodyForm> {
        private AspiratorBody aspiratorBody;

        protected AspiratorBodyFormEvent(AspiratorBodyForm source, AspiratorBody aspiratorBody) {
            super(source, false);
            this.aspiratorBody = aspiratorBody;
        }

        public AspiratorBody getEditEntry() { return aspiratorBody; }
    }

    public static class SaveEvent extends AspiratorBodyForm.AspiratorBodyFormEvent {
        SaveEvent(AspiratorBodyForm source, AspiratorBody aspiratorBody) {
            super(source, aspiratorBody);
        }
    }

    public static class CloseEvent extends AspiratorBodyForm.AspiratorBodyFormEvent {
        CloseEvent(AspiratorBodyForm source) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

