package by.dziashko.frm.ui.forms.cabin;

import by.dziashko.frm.backend.entity.cabin.CabinData;
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

public class CabinDataForm extends FormLayout {

    TextField modelName = new TextField(getTranslation("modelName"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<CabinData> binder = new Binder<>(CabinData.class);
    private CabinData cabinData;

    public CabinDataForm() {

        VerticalLayout lyt = new VerticalLayout(modelName, createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);

    }

//    public void setOrderName(CabinData cabinData) {
//        this.cabinData = cabinData;
//        binder.readBean(cabinData);
//    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new CabinDataForm.CloseEvent(this, cabinData)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(cabinData);
            fireEvent(new CabinDataForm.SaveEvent(this, cabinData));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCabinData(CabinData cabinData) {
        this.cabinData = cabinData;
        binder.readBean(cabinData);
    }

    // Events
    public static abstract class CabinDataFormEvent extends ComponentEvent<CabinDataForm> {
        private final CabinData cabinData;

        protected CabinDataFormEvent(CabinDataForm source, CabinData cabinData) {
            super(source, false);
            this.cabinData = cabinData;
        }

        public CabinData getCabinData() {
            return cabinData;
        }
    }

    public static class SaveEvent extends CabinDataForm.CabinDataFormEvent {
        SaveEvent(CabinDataForm source, CabinData cabinData) {
            super(source, cabinData);
        }
    }

    public static class CloseEvent extends CabinDataFormEvent {
        CloseEvent(CabinDataForm source, CabinData cabinData) {super(source, null); }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
