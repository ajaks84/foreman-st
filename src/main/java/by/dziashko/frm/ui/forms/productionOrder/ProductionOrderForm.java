package by.dziashko.frm.ui.forms.productionOrder;

import by.dziashko.frm.backend.entity.addOptions.AdditionalOptions;
import by.dziashko.frm.backend.entity.aspirator.AspiratorData;
import by.dziashko.frm.backend.entity.cabin.CabinData;
import by.dziashko.frm.backend.entity.cyclone.CycloneData;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.ui.forms.cyclone.CycloneDataForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class ProductionOrderForm extends FormLayout {

    ComboBox<Seller> seller = new ComboBox<>(getTranslation("Seller"));
    TextField client = new TextField(getTranslation("Client"));
    TextField orderNumber = new TextField(getTranslation("Order_Number"));
    TextField orderDate = new TextField();
    TextField orderDeadLine = new TextField();

    DatePicker orderDatePicker;
    DatePicker deadLineDatePicker;
    Locale normalizedLocale;

    ComboBox<CabinData> cabinData = new ComboBox<>(getTranslation("cabin_type"));
    ComboBox<AspiratorData> aspiratorData = new ComboBox<>(getTranslation("aspirator_type"));
    ComboBox<CycloneData> cycloneData = new ComboBox<>(getTranslation("Cyclone_Type"));
    //ComboBox<AdditionalOptions> additionalOptions = new ComboBox<>(getTranslation("Choose_Options"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    private static final long serialVersionUID = 4035101129026674226L;

    Binder<ProductionOrder> binder = new Binder<>(ProductionOrder.class);
    private ProductionOrder productionOrder;

    public ProductionOrderForm(List<Seller> sellers, List<AspiratorData> aspirators, List<CabinData> cabins, List<CycloneData> cyclones, List<AdditionalOptions> addOptions) {
        addClassName("contact-form");

        orderDatePicker = new DatePicker(getTranslation("Order_Date"));
        deadLineDatePicker = new DatePicker(getTranslation("Order_Deadline"));

        normalizedLocale = new Locale("fi", "FI");

        binder.bindInstanceFields(this);

        setOrderDatePickers();

        client.setAutofocus(true);

        orderDatePicker.addValueChangeListener(e -> setOrderDate(String.valueOf(e.getValue())));

        deadLineDatePicker.addValueChangeListener(e -> setDeadLineDate(String.valueOf(e.getValue())));

        seller.setItems(sellers);
        seller.setItemLabelGenerator(Seller::getName);

        cabinData.setItems(cabins);
        cabinData.setItemLabelGenerator(CabinData::getModelName);
        cabinData.addValueChangeListener(e -> setCabinData(e.getValue().getModelName()));


        aspiratorData.setItems(aspirators);
        aspiratorData.setItemLabelGenerator(AspiratorData::getModelName);
        aspiratorData.addValueChangeListener(e -> setAspiratorData(e.getValue().getModelName()));

        cycloneData.setItems(cyclones);
        cycloneData.setItemLabelGenerator(CycloneData::getModelName);
        cycloneData.addValueChangeListener(e -> setCycloneData(e.getValue().getModelName()));

//        additionalOptions.setItems(addOptions);
//        additionalOptions.setItemLabelGenerator(AdditionalOptions::getModelName);
//        additionalOptions.addValueChangeListener(e -> setOptionsData(e.getValue().getModelName()));

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout verticalLayout_1 = new VerticalLayout(seller, client, orderNumber, orderDatePicker, deadLineDatePicker, createButtonsLayout());
        VerticalLayout verticalLayout_2 = new VerticalLayout(cabinData, aspiratorData, cycloneData);

        horizontalLayout.add(verticalLayout_1,verticalLayout_2);

        add(horizontalLayout);
    }

    private void setOptionsData(String modelName) {
        this.productionOrder.setAdditionalOptions(modelName);
    }

    private void setCycloneData(String modelName) {
        this.productionOrder.setSeparatorType(modelName);
    }

    private void setOrderDate(String value) {
        this.orderDate.setValue(value);
    }

    private void setDeadLineDate(String value) {
        this.orderDeadLine.setValue(value);
    }

    private void setCabinData(String value) {
        this.productionOrder.setCabinType(value);
    }

    private void setAspiratorData(String value) {
        this.productionOrder.setAspiratorType(value);
    }

    public void setProductionOrder(ProductionOrder productionOrder) {
        this.productionOrder = productionOrder;
        binder.readBean(productionOrder);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new ProductionOrderForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save,close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(productionOrder);
            fireEvent(new SaveEvent(this, productionOrder));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void setOrderDatePickers(){
        orderDatePicker.setLocale(Locale.CANADA);
        deadLineDatePicker.setLocale(Locale.CANADA);
        orderDatePicker.setValue(LocalDate.now());
        deadLineDatePicker.setValue(LocalDate.now().plusWeeks(6));
    }

    // Events
    public static abstract class OrderNameFormEvent extends ComponentEvent<ProductionOrderForm> {
        private final ProductionOrder productionOrder;

        protected OrderNameFormEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
            super(source, false);
            this.productionOrder = productionOrder;
        }

        public ProductionOrder getOrderName() {
            return productionOrder;
        }
    }

    public static class SaveEvent extends OrderNameFormEvent {
        SaveEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
            super(source, productionOrder);
        }
    }

    public static class DeleteEvent extends OrderNameFormEvent {
        DeleteEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
            super(source, productionOrder);
        }

    }

    public static class CloseEvent extends OrderNameFormEvent {
        CloseEvent(ProductionOrderForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
