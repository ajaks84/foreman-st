package by.dziashko.frm.ui.forms.newProductionOrder;

import by.dziashko.frm.backend.entity.newProductionOrder.NewProductionOrder;
import by.dziashko.frm.backend.entity.newProductionOrder.ResponsiblePerson;
import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import by.dziashko.frm.backend.entity.productionOrder.Seller;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class NewProductionOrderForm extends FormLayout {

    ComboBox<ResponsiblePerson> responsiblePersonComboBox = new ComboBox<>(getTranslation("Seller"));
    TextField client = new TextField(getTranslation("Client"));
    TextField orderNumber = new TextField(getTranslation("Order_Number"));
    TextField orderDate = new TextField();
    TextField orderDeadLine = new TextField();
    //ComboBox<ProductionOrder.Readiness> orderReadiness = new ComboBox<>(getTranslation("Readiness"));

    DatePicker orderDatePicker = new DatePicker(getTranslation("Order_Date"));
    DatePicker deadLineDatePicker = new DatePicker(getTranslation("Order_Deadline"));

    Button save = new Button(getTranslation("Save"));
    Button close = new Button(getTranslation("Cancel"));

    Binder<NewProductionOrder> binder = new Binder<>(NewProductionOrder.class);
    private NewProductionOrder newProductionOrder;

    public NewProductionOrderForm(List<ResponsiblePerson> sellers) {
        addClassName("contact-form");

//        binder.bindInstanceFields(this);
//        orderReadiness.setItems(ProductionOrder.Readiness.values());
//
//        setOrderDatePickers();

//        client.setAutofocus(true);
//
//        orderDatePicker.addValueChangeListener(e ->
//                setOrderDate(e.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))));
//
//
//        deadLineDatePicker.addValueChangeListener(e ->
//                setDeadLineDate(e.getValue().format( DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT) )));
//
//        responsiblePersonComboBox.setItems(sellers);
//        responsiblePersonComboBox.setItemLabelGenerator(Seller::getName);
//        VerticalLayout lyt = new VerticalLayout(responsiblePersonComboBox, client, orderNumber, orderDatePicker, deadLineDatePicker,
//                createButtonsLayout());
//        add(lyt);
    }


//    private void setOrderDate(String value) {
//        this.orderDate.setValue(value);
//    }
//
//    private void setDeadLineDate(String value) {
//        this.orderDeadLine.setValue(value);
//    }
//
//
//    public void setProductionOrder(ProductionOrder productionOrder) {
//        this.productionOrder = productionOrder;
//        setOrderDatePickers();
//        binder.readBean(productionOrder);
//    }
//
//    private Component createButtonsLayout() {
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//
//        save.addClickShortcut(Key.ENTER);
//        close.addClickShortcut(Key.ESCAPE);
//
//        save.addClickListener(click -> validateAndSave());
//        close.addClickListener(click -> fireEvent(new ProductionOrderForm.CloseEvent(this)));
//
//        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));
//
//        return new HorizontalLayout(save,close);
//    }
//
//    private void validateAndSave() {
//        try {
//            String orderNumberS = productionOrder.getOrderNumber();
//            binder.writeBean(productionOrder);
//            fireEvent(new SaveEvent(this, productionOrder));
//            navigateTo(orderNumberS);
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void setOrderDatePickers(){
//        orderDatePicker.setValue(LocalDate.now());
//        deadLineDatePicker.setValue(LocalDate.now().plusWeeks(6));
//    }
//
//    private void  navigateTo(String s) {
//        if (s == "") {
//        }else{
//            this.getUI().ifPresent(ui -> ui.navigate("order-details"+"/"+s));}
//    }
//
//    public void clearDatePickers(){
//        deadLineDatePicker.clear();
//        orderDatePicker.clear();
//    }
//
//    // Events
//    public static abstract class OrderNameFormEvent extends ComponentEvent<ProductionOrderForm> {
//        private final ProductionOrder productionOrder;
//
//        protected OrderNameFormEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
//            super(source, false);
//            this.productionOrder = productionOrder;
//        }
//
//        public ProductionOrder getOrderName() {
//            return productionOrder;
//        }
//    }
//
//    public static class SaveEvent extends OrderNameFormEvent {
//        SaveEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
//            super(source, productionOrder);
//        }
//    }
//
//    public static class DeleteEvent extends OrderNameFormEvent {
//        DeleteEvent(ProductionOrderForm source, ProductionOrder productionOrder) {
//            super(source, productionOrder);
//        }
//
//    }
//
//    public static class CloseEvent extends OrderNameFormEvent {
//        CloseEvent(ProductionOrderForm source) {
//            super(source, null);
//        }
//    }
//
//    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
//                                                                  ComponentEventListener<T> listener) {
//        return getEventBus().addListener(eventType, listener);
//    }
}
