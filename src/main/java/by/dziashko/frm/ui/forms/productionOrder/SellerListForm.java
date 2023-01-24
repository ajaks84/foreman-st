package by.dziashko.frm.ui.forms.productionOrder;

import by.dziashko.frm.backend.entity.productionOrder.Seller;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class SellerListForm extends FormLayout {

    TextField name = new TextField(getTranslation("Name"));
    TextField lastName = new TextField(getTranslation("LastName"));
    IntegerField phoneNumber = new IntegerField(getTranslation("phoneNumber"));
    Button save = new Button(getTranslation("Save"));
    Button delete = new Button(getTranslation("Delete"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<Seller> binder = new Binder<>(Seller.class);
    private Seller seller;

    public SellerListForm() {
        addClassName("contact-form");
        VerticalLayout lyt = new VerticalLayout(name, lastName, phoneNumber,createButtonsLayout());
        binder.bindInstanceFields(this);
        add(lyt);
    }

    public void setOrderName(Seller seller) {
        this.seller = seller;
        binder.readBean(seller);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new SellerListForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(seller);
            fireEvent(new SellerListForm.SaveEvent(this, seller));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
        binder.readBean(seller);
    }

    // Events
    public static abstract class SellerListFormEvent extends ComponentEvent<SellerListForm> {
        private Seller seller;

        protected SellerListFormEvent(SellerListForm source, Seller seller) {
            super(source, false);
            this.seller = seller;
        }

        public Seller getSeller() {
            return seller;
        }
    }

    public static class SaveEvent extends SellerListForm.SellerListFormEvent {
        SaveEvent(SellerListForm source, Seller seller) {
            super(source, seller);
        }
    }

    public static class DeleteEvent extends SellerListForm.SellerListFormEvent {
        DeleteEvent(SellerListForm source, Seller seller) {
            super(source, seller);
        }
    }

    public static class CloseEvent extends SellerListForm.SellerListFormEvent {
        CloseEvent(SellerListForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
