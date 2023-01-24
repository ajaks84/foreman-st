package by.dziashko.frm.ui.forms.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
import by.dziashko.frm.backend.entity.aspirator.AspiratorElectric;
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

public class AspiratorElectricForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AspiratorElectric> binder = new Binder<>(AspiratorElectric.class);
    private AspiratorElectric aspiratorElectric;

    public AspiratorElectricForm() {
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
        close.addClickListener(click -> fireEvent(new AspiratorElectricForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(aspiratorElectric);
            fireEvent(new AspiratorElectricForm.SaveEvent(this, aspiratorElectric));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setEditEntry(AspiratorElectric aspiratorElectric) {
        this.aspiratorElectric = aspiratorElectric;
        binder.readBean(aspiratorElectric);
    }

    // Events
    public static abstract class AspiratorElectricFormEvent extends ComponentEvent<AspiratorElectricForm> {
        private AspiratorElectric aspiratorElectric;

        protected AspiratorElectricFormEvent(AspiratorElectricForm source, AspiratorElectric aspiratorElectric) {
            super(source, false);
            this.aspiratorElectric = aspiratorElectric;
        }

        public AspiratorElectric getEditEntry() { return aspiratorElectric; }
    }

    public static class SaveEvent extends AspiratorElectricForm.AspiratorElectricFormEvent {
        SaveEvent(AspiratorElectricForm source, AspiratorElectric aspiratorElectric) {
            super(source, aspiratorElectric);
        }
    }

    public static class CloseEvent extends AspiratorElectricForm.AspiratorElectricFormEvent {
        CloseEvent(AspiratorElectricForm source) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
