package by.dziashko.frm.ui.forms.addOptions;

import by.dziashko.frm.backend.entity.addOptions.AdditionalOptions;
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

public class AdditionalOptionsForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AdditionalOptions> binder = new Binder<>(AdditionalOptions.class);
    private AdditionalOptions additionalOptions;

    public AdditionalOptionsForm() {

        VerticalLayout lyt = new VerticalLayout(modelName, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);

    }

    public void setOrderName(AdditionalOptions additionalOptions) {
        this.additionalOptions = additionalOptions;
        binder.readBean(additionalOptions);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new AdditionalOptionsForm.CloseEvent(this, additionalOptions)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(additionalOptions);
            fireEvent(new AdditionalOptionsForm.SaveEvent(this, additionalOptions));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setAdditionalOptions(AdditionalOptions additionalOptions) {
        this.additionalOptions = additionalOptions;
        binder.readBean(additionalOptions);
    }

    // Events
    public static abstract class AdditionalOptionsFormEvent extends ComponentEvent<AdditionalOptionsForm> {
        private final AdditionalOptions additionalOptions;

        protected AdditionalOptionsFormEvent(AdditionalOptionsForm source, AdditionalOptions additionalOptions) {
            super(source, false);
            this.additionalOptions = additionalOptions;
        }

        public AdditionalOptions getAdditionalOptions() {
            return additionalOptions;
        }
    }

    public static class SaveEvent extends AdditionalOptionsForm.AdditionalOptionsFormEvent {
        SaveEvent(AdditionalOptionsForm source, AdditionalOptions additionalOptions) {
            super(source, additionalOptions);
        }
    }

    public static class CloseEvent extends AdditionalOptionsFormEvent {
        CloseEvent(AdditionalOptionsForm source, AdditionalOptions additionalOptions) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
