package by.dziashko.frm.ui.forms.aspirator;

import by.dziashko.frm.backend.entity.aspirator.AspiratorOption;
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

public class AspiratorOptionForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AspiratorOption> binder = new Binder<>(AspiratorOption.class);
    private AspiratorOption aspiratorOption;

    public AspiratorOptionForm() {
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
        close.addClickListener(click -> fireEvent(new AspiratorOptionForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(aspiratorOption);
            fireEvent(new AspiratorOptionForm.SaveEvent(this, aspiratorOption));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setEditEntry(AspiratorOption aspiratorOption) {
        this.aspiratorOption = aspiratorOption;
        binder.readBean(aspiratorOption);
    }

    // Events
    public static abstract class AspiratorOptionFormEvent extends ComponentEvent<AspiratorOptionForm> {
        private AspiratorOption aspiratorOption;

        protected AspiratorOptionFormEvent(AspiratorOptionForm source, AspiratorOption aspiratorOption) {
            super(source, false);
            this.aspiratorOption = aspiratorOption;
        }

        public AspiratorOption getEditEntry() { return aspiratorOption; }
    }

    public static class SaveEvent extends AspiratorOptionForm.AspiratorOptionFormEvent {
        SaveEvent(AspiratorOptionForm source, AspiratorOption aspiratorOption) {
            super(source, aspiratorOption);
        }
    }

    public static class CloseEvent extends AspiratorOptionForm.AspiratorOptionFormEvent {
        CloseEvent(AspiratorOptionForm source) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

