package by.dziashko.frm.ui.forms.aspirator;

import by.dziashko.frm.backend.entity.aspirator.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class AspiratorDataForm extends FormLayout {
    TextField modelName = new TextField(getTranslation("modelName"));
    ComboBox<AspiratorBody> aspiratorBody = new ComboBox<>(getTranslation("aspiratorBody"));
    ComboBox<AspiratorFan> aspiratorFan = new ComboBox<>(getTranslation("aspiratorFan"));
    ComboBox<AspiratorElectric> aspiratorElectric = new ComboBox<>(getTranslation("aspiratorElectric"));
    ComboBox<AspiratorOption> aspiratorOption = new ComboBox<>(getTranslation("aspiratorOption"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<AspiratorData> binder = new Binder<>(AspiratorData.class);
    private AspiratorData aspiratorData;

    public AspiratorDataForm(List<AspiratorFan> aspiratorFanList, List<AspiratorBody> aspiratorBodyList,
                             List<AspiratorElectric> aspiratorElectricList, List<AspiratorOption> aspiratorOptions) {
        addClassName("contact-form");
        this.aspiratorBody.setItems(aspiratorBodyList);
        this.aspiratorBody.setItemLabelGenerator(AspiratorBody::getModelName);
        this.aspiratorFan.setItems(aspiratorFanList);
        this.aspiratorFan.setItemLabelGenerator(AspiratorFan::getModelName);
        this.aspiratorElectric.setItems(aspiratorElectricList);
        this.aspiratorElectric.setItemLabelGenerator(AspiratorElectric::getModelName);
        this.aspiratorOption.setItems(aspiratorOptions);
        this.aspiratorOption.setItemLabelGenerator(AspiratorOption::getModelName);
        VerticalLayout lyt = new VerticalLayout(modelName, this.aspiratorBody, this.aspiratorFan, this.aspiratorElectric,
                                                           this.aspiratorOption, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);
    }

    public void setOrderName(AspiratorData aspiratorData) {
        this.aspiratorData = aspiratorData;
        binder.readBean(aspiratorData);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new AspiratorDataForm.CloseEvent(this, aspiratorData)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(aspiratorData);
            fireEvent(new AspiratorDataForm.SaveEvent(this, aspiratorData));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setAspiratorData(AspiratorData aspiratorData) {
        this.aspiratorData = aspiratorData;
        binder.readBean(aspiratorData);
    }

    // Events
    public static abstract class AspiratorDataFormEvent extends ComponentEvent<AspiratorDataForm> {
        private AspiratorData aspiratorData;

        protected AspiratorDataFormEvent(AspiratorDataForm source, AspiratorData aspiratorData) {
            super(source, false);
            this.aspiratorData = aspiratorData;
        }

        public AspiratorData getAspiratorData() {
            return aspiratorData;
        }
    }

    public static class SaveEvent extends AspiratorDataForm.AspiratorDataFormEvent {
        SaveEvent(AspiratorDataForm source, AspiratorData aspiratorData) {
            super(source, aspiratorData);
        }
    }

    public static class CloseEvent extends AspiratorDataForm.AspiratorDataFormEvent {
        CloseEvent(AspiratorDataForm source, AspiratorData aspiratorData) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
